package project.Movement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.scene.chart.PieChart;
import project.BoardTileIterator;
import project.Consts;
import project.Board.Chessboard;
import project.Board.Tile;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Pawn;
import project.Pieces.Piece;
import project.Pieces.Queen;
import project.Pieces.Rook;

public class Game implements Serializable, Iterable<String[]> {

    private final int[]                          columnLetters = {0, 1, 2, 3, 4, 5, 6, 7};
    private Tile[][]                             boardTiles = new Tile[8][8];
    private CheckLegalMoves                      checkLegalMoves;
    private HashMap<int[], ArrayList<int[]>>     allLegalMovesAfterControl; 
    private boolean                              gameIsOver = false;
    private boolean                              pieceReadyToMove = true;
    private boolean                              promotionPawn = false;

    //TODO: legge til at man ikke kan kalle på metodene hvis spillet er over.

    public Game() {
        makeBoard();
        placePieces();
        this.checkLegalMoves = new CheckLegalMoves(); 
        this.allLegalMovesAfterControl = checkLegalMoves.checkforCheckMateAndPat(this.getBoardTilesDeepCopy()); //allLegalMovesAfterControl is initialized with whites available moves
    }

    public int isGameOver() {
        return checkLegalMoves.getGameStatus();
    }

    public int getMoveNumber() {
        return checkLegalMoves.getMoveNumber();
    }
    
    //TODO: error handling for parameters
    public String[] getPieceInfoFromTile(int row, int col) {
        Tile tile = this.boardTiles[row][col];
        return (tile.isOccupied()) ? new String[] {tile.coordinatesToString(), tile.getPiece().getSpriteId()} 
                                   : new String[] {tile.coordinatesToString(), ""};
    }

    private void makeBoard() {
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {

                Tile tile = new Tile(row, col);
                this.boardTiles[row][col] = tile;
            }
        }
    }


    private void placePieces() {
        
        //White

        for (Tile tile : this.boardTiles[1]) {

            String name = "wP" + tile.getCol();
            Pawn pawn = new Pawn(name, 'w');
            tile.setPiece(pawn);
        }
        
        for (Tile tile : this.boardTiles[0]) {

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

        for (Tile tile : this.boardTiles[6]) {

            String name = "bP" + tile.getCol();
            Pawn pawn = new Pawn(name, 'b');
            tile.setPiece(pawn);
            }

        for (Tile tile : this.boardTiles[7]) { 

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
                
                Tile tile = this.boardTiles[row][col];
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

    //Fjerne try catch? - kommer til å krasje uansett, eller legge til sikkerhet der den brukes
    public Tile[][] getBoardTilesDeepCopy()
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
        catch (IOException e) {
            return null;
        } 
        catch (ClassNotFoundException e) {
            return null;
        }
	}

    public ArrayList<String> getLegalMoves(int row, int col) {

        if (!(validationOfCoordinates(row, col))) {
            throw new IllegalArgumentException("The given coordinates are Illegal! Valid values: 0-7");
        }

        int[]            tilePieceToMoveIsOn      = boardTiles[row][col].getCoordinates();            
        ArrayList<int[]> legalMovesForPieceToMove = new ArrayList<int[]>();
        Set<int[]>       allPiecesThatCanMove     = this.allLegalMovesAfterControl.keySet();
        boolean          pieceCanMove             = false;

        for (int[] piece : allPiecesThatCanMove) {
            if (piece[0] == tilePieceToMoveIsOn[0] && piece[1] == tilePieceToMoveIsOn[1]
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

    //Validates that the desired move is legal
    private boolean validationOfMove (int fromRow, int fromCol, int ToRow, int ToCol) {

        boolean legalMove = false;

        if (!(validationOfCoordinates(fromRow, fromCol))) {
            return legalMove;
        }

        if (!(validationOfCoordinates(ToRow, ToCol))) {
            return legalMove;
        }

        int[]            desiredPiece             = new int[]{fromRow, fromCol};
        int[]            desiredMove              = new int[]{ToRow, ToCol};          
        ArrayList<int[]> legalMovesForPieceToMove = new ArrayList<int[]>();
        Set<int[]>       allPiecesThatCanMove     = this.allLegalMovesAfterControl.keySet();
        boolean          legalPiece               = false;



        for (int[] Piece : allPiecesThatCanMove) {
            if (checkForSameCoordinates(desiredPiece , Piece)) {
                legalMovesForPieceToMove = this.allLegalMovesAfterControl.get(Piece);
                legalPiece = true;
            }
        }

        if (legalPiece) {
            for (int[] moves : legalMovesForPieceToMove) {
                if (checkForSameCoordinates(desiredMove, moves)) {
                    legalMove = true;
                }
            }
        }

        return legalMove;
    }

    public String pawnPromotionStringCoordinates() {

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
    
    //TODO: Error handling for parameters, burde kanskje bytte navn?
    public String moveChosenPiece(int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {

        if (this.gameIsOver) {
            throw new IllegalStateException("The game is over, cannt move pieces!");
        }
        
        if (!(validationOfMove(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol))) {
            throw new IllegalArgumentException("The move is illegal!");
        }

        String infoStringEnPassentCastling = "";

        if (this.pieceReadyToMove) {

            Piece pieceToMove = boardTiles[chosenPieceRow][chosenPieceCol].getPiece();

            //Checks if chosen move is a castling move
            if (pieceToMove instanceof King) {
                if (!pieceToMove.getHasMoved())  {
                    if (pieceToMove.getColor() == 'w') {
                        if (moveToPieceCol == 6) {
                            Rook castlingRook = ((Rook)this.boardTiles[0][7].getPiece());
                            this.boardTiles[0][7].removePiece();
                            this.boardTiles[0][5].setPiece(castlingRook);
                            infoStringEnPassentCastling = "05";
                        }
                        else if (moveToPieceCol == 2) {
                            Rook castlingRook = ((Rook)this.boardTiles[0][0].getPiece());
                            this.boardTiles[0][0].removePiece();
                            this.boardTiles[0][3].setPiece(castlingRook);
                            infoStringEnPassentCastling = "03";
                        }
                    }
                    else if (pieceToMove.getColor() == 'b') {
                        if (moveToPieceCol == 6) {
                            Rook castlingRook = ((Rook)this.boardTiles[7][7].getPiece());
                            this.boardTiles[7][7].removePiece();
                            this.boardTiles[7][5].setPiece(castlingRook);
                            infoStringEnPassentCastling = "75";
                        }
                        else if (moveToPieceCol == 2) {
                            Rook castlingRook = ((Rook)this.boardTiles[7][0].getPiece());
                            this.boardTiles[7][0].removePiece();
                            this.boardTiles[7][3].setPiece(castlingRook);
                            infoStringEnPassentCastling = "73";
                        }
                    }
                }
            }

            //Checks if chosen move is en passent
            else if (pieceToMove instanceof Pawn) {
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
                        infoStringEnPassentCastling = (moveToPieceRow - 1) + "" + moveToPieceCol;
                    }
                    else if (pieceToMove.getColor() == 'b') {
                        this.boardTiles[moveToPieceRow + 1][moveToPieceCol].removePiece();
                        infoStringEnPassentCastling = (moveToPieceRow + 1) + "" + moveToPieceCol;
                    }
                }
            }

            pieceToMove.setHasMoved(true);
            
            this.boardTiles[chosenPieceRow][chosenPieceCol].removePiece();
            this.boardTiles[moveToPieceRow][moveToPieceCol].setPiece(pieceToMove);
    
            this.pieceReadyToMove = false;
        }
        
        else {
            throw new IllegalStateException("A piece is already moved, update the game state (checkForGameOver)!");
        } 
        
        for (Tile tile : boardTiles[7]) {
            if (tile.getPiece() instanceof Pawn) {
                promotionPawn = true;
            } 
        }

        for (Tile tile: boardTiles[0]) {
            if (tile.getPiece() instanceof Pawn) {
                promotionPawn = true;
            }
        }

        return infoStringEnPassentCastling;

    }

    public int checkForGameOver() {

        if (gameIsOver) {
            throw new IllegalStateException("The game is over!");
        }

        if (promotionPawn) {
            throw new IllegalStateException("Promote pawn before updating the game!");
        }

        if (pieceReadyToMove) {
            throw new IllegalStateException("Move a piece before updating the game!");
        }

        checkLegalMoves.increaseMoveNumber();

        //allLegalMovesAfterControl is updated with the opposite player's moves here, as opposed to updating it 
        //during the next call of getLegalMoves. This allows us to use getGameStatus() to check for a mate or pat
        this.allLegalMovesAfterControl = checkLegalMoves.checkforCheckMateAndPat(this.getBoardTilesDeepCopy());  
        this.pieceReadyToMove = true;

        this.printBoard();
        
        int gameStatus = checkLegalMoves.getGameStatus(); //gameStatus returns 1 if it is pat, 2 if it is check mate, and otherwise 0

        if (gameStatus == Consts.PAT) {
            gameIsOver = true;
            return Consts.PAT;
        }
        else if (gameStatus == Consts.CHECKMATE) {
            gameIsOver = true;
            return (checkLegalMoves.getMoveNumber() % 2 == 0) ? Consts.CHECKMATE_FOR_BLACK : Consts.CHECKMATE_FOR_WHITE;  //When movenumber is even, black has check mate, when it's odd, white has check mate. 
        }
        else {
            return Consts.GAME_NOT_OVER;
        }
    }


    //TODO: This need to be checked for bugs 
    //TODO: Move out to help methods 
    public void promotePawn(int row, int col, char pieceType, char color) {

        if (!(validationOfCoordinates(row, col))) {
            throw new IllegalArgumentException("The given coordinates are Illegal! Valid values: 0-7");
        }

        if (gameIsOver) {
            throw new IllegalStateException("The game is over!");
        }

        if (!promotionPawn) {
            throw new IllegalStateException("No pawn to promote");
        }

        if ((row == 7 && color == 'b') || (row == 0 && color == 'w')) {
            throw new IllegalArgumentException("Wrong color for the piece to be promoted!");
        }

        int[] inputCoordinates = new int[]{row, col};
        int[] pawnPromotionCoordinates = new int[]{};

        for (Tile tile : boardTiles[7]) {
             if (tile.getPiece() instanceof Pawn) {
                pawnPromotionCoordinates = tile.getCoordinates();
            }
        }

        for (Tile tile : boardTiles[0]) {
            if (tile.getPiece() instanceof Pawn) {
                pawnPromotionCoordinates = tile.getCoordinates();
            }
        }
        
        if (!checkForSameCoordinates(pawnPromotionCoordinates, inputCoordinates)) {
            throw new IllegalArgumentException("There are no pawn to promote at these coordinates!");    
        }

        String pieceName = color + " " + pieceType;
        Tile tile = boardTiles[row][col];

        switch (pieceType) {
            case 'B':
                tile.setPiece(new Bishop(pieceName, color));
                promotionPawn = false;
                break;
            case 'K':
                tile.setPiece(new Knight(pieceName, color));
                promotionPawn = false;
                break;
            case 'Q':
                tile.setPiece(new Queen(pieceName, color));
                promotionPawn = false;
                break;
            case 'R':
                tile.setPiece(new Rook(pieceName, color));
                promotionPawn = false;
                break;
            default:
                throw new IllegalArgumentException("Illegal piece Type!");
        }
    } 

    private void changePieceOnTile(int row, int col, char pieceType, char color, int... pawnRookKingInfo) {

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
                Pawn    pawn                = new Pawn(pieceName, color);
                boolean hasPawnMoved        = (pawnRookKingInfo[0] == 1) ? true : false;
                boolean movedTwoLastTurn    = (pawnRookKingInfo[1] == 1) ? true : false;
                int     enPassentMoveNumber = pawnRookKingInfo[2];

                pawn.setHasMoved(hasPawnMoved);
                pawn.setMovedTwoLastTurn(movedTwoLastTurn);
                pawn.setMoveNumberEnPassant(enPassentMoveNumber);
                tile.setPiece(pawn);

                break;
            case 'R':
                Rook rook = new Rook(pieceName, color);                
                boolean hasRookMoved = false;
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
        }


    } 
         
    private boolean validationOfCoordinates(int row, int col) {

        if (row > 7 || row < 0) {
            return false;
        }
        if (col > 7 || col < 0) {
            return false;        
        }

        return true;
    }

    public boolean allLegalPieces(int moveToPieceRow, int moveToPieceCol) {
        
        Set<int[]> allPiecesThatCanMove = allLegalMovesAfterControl.keySet();
        int[] moveToPiece = boardTiles[moveToPieceRow][moveToPieceCol].getCoordinates();

        for (int[] piece : allPiecesThatCanMove) {
            if (piece[0] == moveToPiece[0] && piece[1] == moveToPiece[1]) {
                return true;
            }
        }
        
        return false; 
    }

    private boolean validationOfString (String saveGameString) {

        if (saveGameString == null) {
            return false;
        }

        String[] tileData = saveGameString.split("-");

        if (tileData.length != 65) {
            return false;
        }

        if (!(tileData[64].matches("^[0-9]+$"))) {
            return false;
        }

        List<String> listWithoutTurnNumber = new ArrayList<String>(Arrays.asList(tileData));
        listWithoutTurnNumber.remove(64);

        for (String string : listWithoutTurnNumber) {

            if (!(   string.matches("00") 
                  || string.matches("[wb][KQB]")
                  || string.matches("[wb][RX][=][01]")
                  || string.matches("[wb][P][=][01][=][01][=][0-9]+$")))
            {
                return false;
            }
        }

        return true;
    }

    public void loadedGamePiecesPosition(String saveGameString) {

        //Saves the orginal game state if something is wrong
        Tile[][] currentGamePosition = this.getBoardTilesDeepCopy();
        int currentMoveNumber = checkLegalMoves.getMoveNumber();

        if (!validationOfString(saveGameString)) {
            throw new IllegalArgumentException("The string format is wrong!");
        }

        String[] tileData = saveGameString.split("-");
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

                }
                ++tileDataIndex;
            }
        }

        try {
            checkLegalMoves.validationOfGameState(this.getBoardTilesDeepCopy(), turnNumber);
        }
        catch (IllegalArgumentException e) {

            //resets the game if something about the game state was wrong, e.g, pawns in row 1 and 8. 
            this.boardTiles = currentGamePosition;
            checkLegalMoves.setMoveNumber(currentMoveNumber);
            throw new IllegalArgumentException("This is not a legal chess position, no change was made");
        }
    
        //Reset attributes after the game is loaded
        this.gameIsOver = false;
        this.pieceReadyToMove = true;
        this.promotionPawn = false;
        this.allLegalMovesAfterControl = checkLegalMoves.checkforCheckMateAndPat(this.getBoardTilesDeepCopy());

        printBoard();
    }

    private boolean checkForSameCoordinates(int[] coordinateOne, int[] coordinateTwo) {
        // if (coordinateOne.length == 0 || coordinateTwo.length == 0)
        //     throw new IllegalArgumentException("Coordinates must have values. Coordinate one: " + coordinateOne.length + ", coordinate two: " + coordinateTwo.length);

        if (coordinateOne[0] == coordinateTwo[0] && coordinateOne[1] == coordinateTwo[1]) {
            return true;
        }
        return false;
    }

    @Override
    public BoardTileIterator iterator() {

        return new BoardTileIterator(this);
    } 

    public static void main(String[] args) {
        Game game = new Game();
    }
}
