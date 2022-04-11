package project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javafx.scene.chart.PieChart;
import project.Board.Chessboard;
import project.Board.Tile;
import project.Files.SaveGames;
import project.Movement.CheckLegalMoves;
import project.Movement.TileCheckLegalMoves;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Pawn;
import project.Pieces.Piece;
import project.Pieces.Queen;
import project.Pieces.Rook;

public class Game implements Serializable, Iterable<String[]> {

    //Chessboard                            chessboard;
    //private Tile[][]                        boardTiles;
    private final int[]                      columnLetters = {0, 1, 2, 3, 4, 5, 6, 7};
    private Tile[][]                         boardTiles = new Tile[8][8];
    private CheckLegalMoves                  checkLegalMoves;
    private HashMap<int[], ArrayList<int[]>> allLegalMovesAfterControl;

    public Game() {
        makeBoard();
        placePieces();
        this.checkLegalMoves = new CheckLegalMoves(); 
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(this.boardDeepCopyUsingSerialization()); //allLegalMovesAfterControl is initialized with whites available moves
    }

    public int isGameOver() {
        return checkLegalMoves.getGameStatus();
    }

    public Tile[][] getBoardTiles() {
        return boardTiles;
    }

    public Tile getTile(int row, int col) {
        return boardTiles[row][col]; 
    }
    
    //TODO: error handling for parameters
    public String[] getPieceInfoFromTile(int row, int col) {
        Tile tile = boardTiles[row][col];
        return (tile.isOccupied()) ? new String[] {tile.coordinatesToString(), tile.getPiece().getSpriteId()} 
                                   : new String[] {tile.coordinatesToString(), ""};
    }

    private void makeBoard() {
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                
                char firstColor;
                char secondColor; 
                char tileColor;

                if (row % 2 == 1) {
                    firstColor = 'w';
                    secondColor = 'b';
                } 
                else {
                    firstColor = 'b';
                    secondColor = 'w';
                }

                tileColor = (col % 2 == 0) ? firstColor : secondColor;

                Tile tile = new Tile(row, col, tileColor);
                boardTiles[row][col] = tile;
            }
        }
    }


    private void placePieces() {
        
        //White

        for (Tile tile : boardTiles[1]) {
            tile.setOccupied(true);

            String name = "wP" + tile.getCol();
            Pawn pawn = new Pawn(name, 'w');
            tile.setPiece(pawn);
        }
        
        for (Tile tile : boardTiles[0]) {
            //TODO: se på om hvordan vi håndterer setOccupied
            tile.setOccupied(true);

            if (tile.getCol() == 0 || tile.getCol() == 7) {
                String name = "wR" + tile.getCol();
                Rook rook = new Rook(name, 'w');
                tile.setPiece(rook);
            }
            if (tile.getCol() == 1 || tile.getCol() == 6) {
                String name = "wK" + tile.getCol();
                Knight knight = new Knight(name, 'w');
                tile.setPiece(knight);
            }
            if (tile.getCol() == 2 || tile.getCol() == 5) {
                String name = "wB" + tile.getCol();
                Bishop bishop = new Bishop(name, 'w');
                tile.setPiece(bishop);
            }
            
            if (tile.getCol() == 3) {
                String name = "wQ" + tile.getCol();
                Queen queen = new Queen(name, 'w');
                tile.setPiece(queen);
            }
            
            if (tile.getCol() == 4) {
                String name = "wX" + tile.getCol();
                King king = new King(name, 'w');
                tile.setPiece(king);        
            }
        }

        //Black

        for (Tile tile : boardTiles[6]) {
            tile.setOccupied(true);

            String name = "bP" + tile.getCol();
            Pawn pawn = new Pawn(name, 'b');
            tile.setPiece(pawn);
            }

        for (Tile tile : boardTiles[7]) {
            //TODO: se på om hvordan vi håndterer setOccupied
            tile.setOccupied(true); 

            if (tile.getCol() == 0 || tile.getCol() == 7) {
                String name = "bR" + tile.getCol();
                Rook rook = new Rook(name, 'b');
                tile.setPiece(rook);
            }
            if (tile.getCol() == 1 || tile.getCol() == 6) {
                String name = "bK" + tile.getCol();
                Knight knight = new Knight(name, 'b');
                tile.setPiece(knight);
            }
            if (tile.getCol() == 2 || tile.getCol() == 5) {
                String name = "bB" + tile.getCol();
                Bishop bishop = new Bishop(name, 'b');
                tile.setPiece(bishop);
            }
            
            if (tile.getCol() == 3) {
                String name = "bQ" + tile.getCol();
                Queen queen = new Queen(name, 'b');
                tile.setPiece(queen);
            }
            
            if (tile.getCol() == 4) {
                String name = "bX" + tile.getCol();
                King king = new King(name, 'b');
                tile.setPiece(king);        
            }
        }
    }

    
    public void printBoard() {
    
        for (int row = 7; row >= 0; --row) {
            String ofRow = (row) + " ";
            for (int col = 0; col < 8; ++col) {
                
                Tile tile = boardTiles[row][col];
                try {
                    String piece = tile.getPiece().getName();
                    ofRow += "| " + piece + ' ';
                }
                catch(Exception e) {
                    ofRow += "| --- "; 
                }
            }

            System.out.println(ofRow);
        }

        String colLetters = "  ";

        for (int columnLetter : columnLetters) {
            colLetters += "   " + columnLetter + "  ";
        }
        System.out.println(colLetters + "\n\n");

    }

    //https://www.studytonight.com/java-examples/how-to-make-a-deep-copy-of-an-object-in-java

    public Tile[][] boardDeepCopyUsingSerialization()
	{
		try
		{
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			objectOutput.writeObject(boardTiles);
			
			ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
			ObjectInputStream objectInput = new ObjectInputStream(byteInput);
			
			return (Tile[][])objectInput.readObject();
		}
		catch(Exception e)
		{
			return null;
		}	
	}

    public ArrayList<String> getLegalMoves(int row, int col) {

        //Piece pieceToMove                                       = this.boardTiles[row][col].getPiece();
        int[]            tilePieceToMoveIsOn      = boardTiles[row][col].getCoordinates();            
        ArrayList<int[]> legalMovesForPieceToMove = new ArrayList<int[]>();
        Set<int[]>       allPiecesThatCanMove     = this.allLegalMovesAfterControl.keySet();
        boolean          pieceCanMove             = false;

        for (int[] piece : allPiecesThatCanMove) {
            if (Arrays.equals(piece, tilePieceToMoveIsOn)
                && this.allLegalMovesAfterControl.get(piece).size() != 0) {
                    legalMovesForPieceToMove = this.allLegalMovesAfterControl.get(piece);       
                    pieceCanMove = true;
            }
        }

        ArrayList<String> coordinateString = new ArrayList<String>();
        
        if (!pieceCanMove) {
            return coordinateString; //The controller ignores the user input if the arraylist is empty
        }

        for (int[] legalMove : legalMovesForPieceToMove) {
            coordinateString.add(legalMove[0] + "" + legalMove[1]); //This string representing the coordinates allows us to fetch imageviews by id
        }
        
        return new ArrayList<String>(coordinateString);
    }

    public String isMoveEnPassent(int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {

        Piece pieceToMove = boardTiles[chosenPieceRow][chosenPieceCol].getPiece();

        if (pieceToMove instanceof Pawn) {
            int moveNumber = checkLegalMoves.getMoveNumber();
            ((Pawn)pieceToMove).setMoveNumberEnPassant(moveNumber);
            if (pieceToMove.getHasMoved()) {
                ((Pawn)pieceToMove).setMovedTwoLastTurn(false);
            }
            else if (Math.abs(chosenPieceRow - moveToPieceRow) == 2) {
                ((Pawn)pieceToMove).setMovedTwoLastTurn(true);
            }

            if (!boardTiles[moveToPieceRow][moveToPieceCol].isOccupied() && Math.abs(chosenPieceCol-moveToPieceCol) == 1) {
                if (pieceToMove.getColor() == 'w') {
                    this.boardTiles[moveToPieceRow - 1][moveToPieceCol].removePiece();
                    return (moveToPieceRow - 1) + "" + moveToPieceCol;
                }
                else if (pieceToMove.getColor() == 'b') {
                    this.boardTiles[moveToPieceRow + 1][moveToPieceCol].removePiece();
                    return (moveToPieceRow + 1) + "" + moveToPieceCol;
                }
            }
        }
        return "";
    }

    public String isMoveCastling (int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {
        
        Piece pieceToMove = boardTiles[chosenPieceRow][chosenPieceCol].getPiece();

        if (pieceToMove instanceof King) {
            if (!pieceToMove.getHasMoved())  {
                if (pieceToMove.getColor() == 'w') {
                    if (moveToPieceCol == 6) {
                        Rook castlingRook = ((Rook)this.boardTiles[0][7].getPiece());
                        this.boardTiles[0][7].removePiece();
                        this.boardTiles[0][5].setPiece(castlingRook);
                        return "05";
                    }
                    else if (moveToPieceCol == 2) {
                        Rook castlingRook = ((Rook)this.boardTiles[0][0].getPiece());
                        this.boardTiles[0][0].removePiece();
                        this.boardTiles[0][3].setPiece(castlingRook);
                        return "03";
                    }
                }
                else if (pieceToMove.getColor() == 'b') {
                    if (moveToPieceCol == 6) {
                        Rook castlingRook = ((Rook)this.boardTiles[7][7].getPiece());
                        this.boardTiles[7][7].removePiece();
                        this.boardTiles[7][5].setPiece(castlingRook);
                        return "75";
                    }
                    else if (moveToPieceCol == 2) {
                        Rook castlingRook = ((Rook)this.boardTiles[7][0].getPiece());
                        this.boardTiles[7][0].removePiece();
                        this.boardTiles[7][3].setPiece(castlingRook);
                        return "73";
                    }
                }
            }
        }

        return "";
    }

    public String pawnPromotion() {
        Tile[] topRow = boardTiles[7];
        Tile[] bottomRow = boardTiles[0];

        for (Tile tile : topRow) {
            if (tile.isOccupied() && tile.getPiece() instanceof Pawn) {
                return tile.coordinatesToString();
            }
        }

        for (Tile tile : bottomRow) {
            if (tile.isOccupied() && tile.getPiece() instanceof Pawn) {
                return tile.coordinatesToString();
            }
        }

        return "";
    }
    
    //TODO: Error handling for parameters
    public void updateGameState(int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {
        
        Piece pieceToMove = boardTiles[chosenPieceRow][chosenPieceCol].getPiece();
        pieceToMove.setHasMoved(true);
       
        this.boardTiles[chosenPieceRow][chosenPieceCol].removePiece();
        this.boardTiles[moveToPieceRow][moveToPieceCol].setPiece(pieceToMove);
    }

    public int checkForGameOver() {

        checkLegalMoves.increaseMoveNumber();

        //allLegalMovesAfterControl is updated with the opposite player's moves here, as opposed to updating it 
        //during the next call of getLegalMoves. This allows us to use getGameStatus() to check for a mate or pat
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(this.boardDeepCopyUsingSerialization());
        
        this.printBoard();
        
        int gameStatus = checkLegalMoves.getGameStatus(); //gameStatus returns 1 if it is pat, 2 if it is check mate, and otherwise 0

        if (gameStatus == Consts.PAT) {
            return Consts.PAT;
        }
        else if (gameStatus == Consts.CHECKMATE) {
            return (checkLegalMoves.getMoveNumber() % 2 == 0) ? Consts.CHECKMATE_FOR_BLACK : Consts.CHECKMATE_FOR_WHITE;  //When movenumber is even, black has check mate, when it's odd, white has check mate. 
        }
        else {
            return Consts.GAME_NOT_OVER;
        }
    }

    //TODO: Error handling for parameters    
    public void changePieceOnTile(int row, int col, char pieceType, char color, int... pawnRookKingInfo) {

        String pieceName = color + " " + pieceType;
        Tile tile = boardTiles[row][col];

        switch (pieceType) {
            case 'B':
                tile.setPiece(new Bishop(pieceName, color));
                break;
            case 'K':
                tile.setPiece(new Knight(pieceName, color));
                break;
            case 'P':
                Pawn pawn = new Pawn(pieceName, color);
                boolean hasPawnMoved        = (pawnRookKingInfo[0] == 1) ? true : false;
                boolean movedTwoLastTurn    = (pawnRookKingInfo[1] == 1) ? true : false;
                int     enPassentMoveNumber = pawnRookKingInfo[2];

                pawn.setHasMoved(hasPawnMoved);
                pawn.setMovedTwoLastTurn(movedTwoLastTurn);
                pawn.setMoveNumberEnPassant(enPassentMoveNumber);

                tile.setPiece(new Pawn(pieceName, color));
                break;
            case 'R':
                Rook rook = new Rook(pieceName, color);
                boolean hasRookMoved = (pawnRookKingInfo[0] == 1) ? true : false; 
                rook.setHasMoved(hasRookMoved);
                
                tile.setPiece(rook);
                break;
            case 'Q':
                tile.setPiece(new Queen(pieceName, color));
                break;
            case 'X':
                King king = new King(pieceName, color);
                boolean hasKingMoved = (pawnRookKingInfo[0] == 1) ? true : false; 
                king.setHasMoved(hasKingMoved);
                
                tile.setPiece(king);
                break;
            default:
                //TODO: Maybe change to something else than printing 
                System.err.println("Invalid piece type");
                return;
        }
    } 

    public boolean allLegalPieces(int moveToPieceRow, int moveToPieceCol) {
        Set<int[]> allPiecesThatCanMove = allLegalMovesAfterControl.keySet();
        int[] moveToPiece = boardTiles[moveToPieceRow][moveToPieceCol].getCoordinates();

        for (int[] piece : allPiecesThatCanMove) {
            if (Arrays.equals(piece, moveToPiece)) {
                return true;
            }
        }
        
        return false; 
    }

    // wR+0-wK-wB-wQ-wX+0-wB-wK-wR+0-wP+0+0+0-wP+0+0+0-wP+0+0+0-wP+0+0+0-wP+0+0+0-wP+0+0+0-wP+0+0+0-wP+0+0+0-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-bP+0+0+0-bP+0+0+0-bP+0+0+0-bP+0+0+0-bP+0+0+0-bP+0+0+0-bP+0+0+0-bP+0+0+0-bR+0-bK-bB-bQ-bX+0-bB-bK-bR+0-0
    public void loadedGamePiecesPosition(String saveName) {
        SaveGames loadGame = new SaveGames();
        String saveGameString = loadGame.loadGame(saveName);

        String[] tileData = saveGameString.split("-");
        System.out.println(tileData.length);
        int turnNumber = Integer.parseInt(tileData[64]);
        checkLegalMoves.setMoveNumber(turnNumber);
        
        this.makeBoard(); //Create an empty board
        
        int tileDataIndex = 0;
        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {
                String[] pieceWithAttributes = tileData[tileDataIndex].split("=");

                switch (pieceWithAttributes.length) {
                case 1: //Other piece or empty
                    String pieceOrNothing = pieceWithAttributes[0];
                    if (!pieceOrNothing.equals("00")) {
                        char color     = pieceOrNothing.charAt(0);
                        char pieceType = pieceOrNothing.charAt(1);

                        changePieceOnTile(tile.getRow(), tile.getCol(), pieceType, color);
                    }
                    break;
                case 2: //Rook and King
                    String rookOrKing    = pieceWithAttributes[0];
                    char rookOrKingColor = rookOrKing.charAt(0);
                    char pieceType       = rookOrKing.charAt(1);
                    int hasMoved         = Integer.parseInt(pieceWithAttributes[1]);

                    changePieceOnTile(tile.getRow(), tile.getCol(), pieceType, rookOrKingColor, hasMoved);
                    break;
                case 4: //Pawn
                    String pawn             = pieceWithAttributes[0];
                    char pawnColor          = pawn.charAt(0);
                    char pawnType           = pawn.charAt(1);
                    int pawnHasMoved        = Integer.parseInt(pieceWithAttributes[1]);
                    int movedTwoLastTurn    = Integer.parseInt(pieceWithAttributes[2]);
                    int enPassentMoveNumber = Integer.parseInt(pieceWithAttributes[3]);

                    changePieceOnTile(tile.getRow(), tile.getCol(), pawnType, pawnColor, 
                                        pawnHasMoved, movedTwoLastTurn, enPassentMoveNumber);
                    break;
                default:
                    System.err.println("Invalid piece information.");
                }

                ++tileDataIndex;
            }
        }

        printBoard();
    }
    /*  SAVE INFO STRING STRUCTURE
                                 (w/b)Char       (1 or 0)         (1 or 0)               Positive int
        Pawn(P):            color and piece type=has moved+moved two spaces last turn=en passen move number-
        King or rook(X/R):  color and piece type=has moved-
        Other piece(Q/K/B): color and piece type-
        Empty tile:         00-
    */

    public void saveGame(String saveName) {
        SaveGames saveGameWriter = new SaveGames();
        String saveGameData = new String();

        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {
                if (tile.isOccupied()) {
                    
                    Piece piece = tile.getPiece();
                    
                    if (piece instanceof Pawn) {
                        Pawn pawn = (Pawn)piece;                

                        saveGameData += pawn.getSpriteId() + "=";
                        saveGameData += ((pawn.getHasMoved()) ? "1" : "0") + "=";
                        saveGameData += ((pawn.getMovedTwoLastTurn()) ? "1" : "0") + "=";
                        saveGameData += pawn.getMoveNumberEnPassant();
                    }
                    else if (piece instanceof Rook || piece instanceof King) {

                        saveGameData += piece.getSpriteId() + "=";
                        saveGameData += ((piece.getHasMoved()) ? "1" : "0");
                    }
                    else {
                        saveGameData += piece.getSpriteId();
                    }

                    saveGameData += "-";
                }
                else {
                    saveGameData += "00-";
                }
            }
        }

        saveGameData += checkLegalMoves.getMoveNumber();
        
        saveGameWriter.saveGame(saveName, saveGameData);

        System.out.println(saveGameData);
    }

    @Override
    public BoardTileIterator iterator() {
        return new BoardTileIterator(this);
    } 

    public static void main(String[] args) {
        
    }
}
