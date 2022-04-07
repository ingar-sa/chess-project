package project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

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

public class Game implements Serializable {

    //Chessboard                            chessboard;
    //private Tile[][]                        boardTiles;
    private final int[]                     columnLetters = {0, 1, 2, 3, 4, 5, 6, 7};
    private Tile[][]                        boardTiles = new Tile[8][8];
    private CheckLegalMoves                 checkLegalMoves;
    private HashMap<int[], ArrayList<int[]>> allLegalMovesAfterControl;

    public Game() {
        //this.chessboard                = new Chessboard();        //Only necessary for printing board 
        //this.boardTiles  = chessboard.getBoardTiles();
        makeBoard();
        placePieces();
        this.checkLegalMoves = new CheckLegalMoves(); 
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(this.deepCopyUsingSerialization()); //allLegalMovesAfterControl is initialized with whites available moves
    }

    public int isGameOver() {
        return checkLegalMoves.getGameStatus();
    }

    public Tile[][] getBoardTiles() {
        return boardTiles;
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

                tileColor = (col % 2 == 1) ? firstColor : secondColor;

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

    public Tile[][] deepCopyUsingSerialization()
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

    public String isMoveCasteling (int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {
        
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

    public int updateGameState(int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {
        
        Piece pieceToMove = boardTiles[chosenPieceRow][chosenPieceCol].getPiece();

        pieceToMove.setHasMoved(true);
       
        this.boardTiles[chosenPieceRow][chosenPieceCol].removePiece();
        this.boardTiles[moveToPieceRow][moveToPieceCol].setPiece(pieceToMove);
        
        checkLegalMoves.increaseMoveNumber();

        //allLegalMovesAfterControl is updated with the opposite player's moves here, as opposed to updating it 
        //during the next call of getLegalMoves. This allows us to use getGameStatus() to check for a mate or pat
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(this.deepCopyUsingSerialization()); 
        
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


    public HashMap<String, String> loadedGamePiecesPosition() {

        HashMap<String, String> piecePositions = new HashMap<>();

        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {

                String positionId = tile.getRow() + "" + tile.getCol();

                if (tile.isOccupied())
                    piecePositions.put(positionId, tile.getPiece().getSpriteId());
                else
                    piecePositions.put(positionId, null);
            }
        }

        return piecePositions;
    }

    public void saveGame() {
        SaveGames saveGame = new SaveGames();
        String saveGameData = new String();

        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {
                if (tile.isOccupied()) {
                    saveGameData += tile.getPiece().getSpriteId();
                    saveGameData += "-";
                }
                else{
                    saveGameData += "0-";
                }
            }
        }
        
        System.out.println(saveGameData);
    }
    

    public static void main(String[] args) {
        Game game = new Game();
        Tile[][] copyBoard = game.deepCopyUsingSerialization();
        Tile[][] realBoard = game.getBoardTiles();
        
    }

}
