package project.Movement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private MovementPatterns                     whiteMovement;
    private MovementPatterns                     blackMovement;
    
    private final int[] whiteRookRightStartTile  = new int[]{0, 7};
    private final int[] whiteRookLeftStartTile   = new int[]{0, 0}; 
    private final int[] blackRookRightStartTile  = new int[]{7, 7};
    private final int[] blackRookLeftStartTile   = new int[]{7, 0};

    private final int[] orginalWhiteKingLocation = new int[]{0, 4};
    private final int[] orginalBlackKingLocation = new int[]{7, 4};

    public Game() {
        makeBoard();
        placePieces();
        this.checkLegalMoves = new CheckLegalMoves(); 
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(this.getBoardDeepCopyUsingSerialization()); //allLegalMovesAfterControl is initialized with whites available moves
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
    }

    public int isGameOver() {
        return checkLegalMoves.getGameStatus();
    }

    //TODO: Rememerb to remove these! make private!
    private Tile[][] getBoardTiles() {
        return boardTiles;
    }

    public Tile getTile(int row, int col) {
        return boardTiles[row][col]; 
    }

    public int getMoveNUmber() {
        return checkLegalMoves.getMoveNumber();
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

    public Tile[][] getBoardDeepCopyUsingSerialization()
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

        validationOfCoordinates(row, col);

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

    public String isMoveEnPassent(int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {

        validationOfCoordinates(chosenPieceRow, chosenPieceCol);
        validationOfCoordinates(moveToPieceRow, moveToPieceCol);

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

        validationOfCoordinates(chosenPieceRow, chosenPieceCol);
        validationOfCoordinates(moveToPieceRow, moveToPieceCol);
        
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

        validationOfCoordinates(chosenPieceRow, chosenPieceCol);
        validationOfCoordinates(moveToPieceRow, moveToPieceCol);

        //TODO: Make method to validate that the preformed move is legal

        
        Piece pieceToMove = boardTiles[chosenPieceRow][chosenPieceCol].getPiece();
        pieceToMove.setHasMoved(true);
       
        this.boardTiles[chosenPieceRow][chosenPieceCol].removePiece();
        this.boardTiles[moveToPieceRow][moveToPieceCol].setPiece(pieceToMove);
    }

    public int checkForGameOver() {

        checkLegalMoves.increaseMoveNumber();

        //allLegalMovesAfterControl is updated with the opposite player's moves here, as opposed to updating it 
        //during the next call of getLegalMoves. This allows us to use getGameStatus() to check for a mate or pat
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(this.getBoardDeepCopyUsingSerialization());
        
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
    public void changePieceOnTile(int row, int col, char pieceType, char color, boolean pawnPromotion, int... pawnRookKingInfo) {

        if (!(color == 'b' || color == 'w')) {
            throw new IllegalArgumentException("Only black (b) and white (w) color is allowed!");
        }

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

                if (!(enPassentMoveNumber >= 0)) {
                    throw new IllegalArgumentException("The Pawn cant have a negative move number!");
                }

                pawn.setHasMoved(hasPawnMoved);
                pawn.setMovedTwoLastTurn(movedTwoLastTurn);
                pawn.setMoveNumberEnPassant(enPassentMoveNumber);

                tile.setPiece(pawn);
                break;
            case 'R':
                Rook rook = new Rook(pieceName, color);                
                boolean hasRookMoved = false;

                if (pawnPromotion) {
                    hasRookMoved = true;  
                }
                else {
                hasRookMoved = (pawnRookKingInfo[0] == 1) ? true : false;
                } 
                
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
                throw new IllegalArgumentException("Illegal piece Type!");
        }
    } 

    private void validationOfCoordinates(int row, int col) {

        if (row > 7 || row < 0) {
            throw new IllegalArgumentException("The given row is outside the board!");
        }
        if (col > 7 || col < 0) {
            throw new IllegalArgumentException("The given col is outside the board!");
        }
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

    //wR=0-wK-wB-wQ-wX=0-wB-wK-wR=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-00-00-wP=1=1=0-bP=1=0=3-00-00-00-00-wP=1=0=4-bP=1=1=5-00-00-00-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6
    public void loadedGamePiecesPosition(String saveGameString) {

        Tile[][] currentGamePosition = this.getBoardDeepCopyUsingSerialization();
        int currentMoveNumber = checkLegalMoves.getMoveNumber();

        try { 

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

                            changePieceOnTile(tile.getRow(), tile.getCol(), pieceType, color, false);
                        }
                        break;
                    case 2: //Rook and King
                        String rookOrKing    = pieceWithAttributes[0];
                        char rookOrKingColor = rookOrKing.charAt(0);
                        char pieceType       = rookOrKing.charAt(1);
                        int hasMoved         = Integer.parseInt(pieceWithAttributes[1]);

                        changePieceOnTile(tile.getRow(), tile.getCol(), pieceType, rookOrKingColor, false, hasMoved);
                        break;
                    case 4: //Pawn
                        String pawn             = pieceWithAttributes[0];
                        char pawnColor          = pawn.charAt(0);
                        char pawnType           = pawn.charAt(1);
                        int pawnHasMoved        = Integer.parseInt(pieceWithAttributes[1]);
                        int movedTwoLastTurn    = Integer.parseInt(pieceWithAttributes[2]);
                        int enPassentMoveNumber = Integer.parseInt(pieceWithAttributes[3]);

                        changePieceOnTile(tile.getRow(), tile.getCol(), pawnType, pawnColor, false,
                                            pawnHasMoved, movedTwoLastTurn, enPassentMoveNumber);
                        break;
                    default:
                        throw new IllegalArgumentException("The String has wrong formatting or wrong information!");
                    }

                    ++tileDataIndex;
                }
            }
        }

        catch (Exception e) {
            this.boardTiles = currentGamePosition;
            checkLegalMoves.setMoveNumber(currentMoveNumber);
            throw new IllegalArgumentException("The String has wrong formatting, no change is made!");
        }

        try {
            validationOfGameState();
        }
        catch(IllegalArgumentException e) {
            this.boardTiles = currentGamePosition;
            checkLegalMoves.setMoveNumber(currentMoveNumber);
            throw new IllegalArgumentException("This is not a legal chess position, no change was made!");
        }

        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(this.getBoardDeepCopyUsingSerialization());

        printBoard();
    }

    //This method validates that the loaded game position is valid according to chess rules, e.g, 
    //there are no pawns on row 1 and 8, there are no pawns, rooks or kings that is not placed on their orginal position and that has moved etc.
    //From this you can write your own chess positions as strings
    private void validationOfGameState() {

        int[] whiteKingLocation = new int[]{};
        int[] blackKingLocation = new int[]{};
        int whiteKingCount = 0;
        int blackKingCount = 0;
        int rowCount = -1;
        ArrayList<Integer> pawnMoveNumbers = new ArrayList<Integer>();
        

        for (Tile[] row : boardTiles) {
            rowCount++;
            for (Tile tile : row) {

                Piece chosenPiece = tile.getPiece();
                int[] chosenPieceCoordinates = tile.getCoordinates();
                
                if (tile.getPiece() instanceof King) { //Checks that there is only one black king and that if it is not placed on the start tile it has moved
                    if (chosenPiece.getColor() == 'b') {
                        blackKingCount++;
                        blackKingLocation = chosenPieceCoordinates;
                        if ((!(checkForSameCoordinates(this.orginalBlackKingLocation, chosenPieceCoordinates))) && chosenPiece.getHasMoved() == false) {
                            System.out.println("BK");
                            throw new IllegalArgumentException("The Black king has moved, but the input says it has not!");
                        }

                    }
                    else if (chosenPiece.getColor() == 'w') {
                        whiteKingCount++;
                        whiteKingLocation = chosenPieceCoordinates;
                        if ((!(checkForSameCoordinates(this.orginalWhiteKingLocation, chosenPieceCoordinates))) && chosenPiece.getHasMoved() == false) {
                            System.out.println("WK");
                            throw new IllegalArgumentException("The White king has moved, but the input says it has not!");
                        }
                    }
                }

                else if (chosenPiece instanceof Rook) {// Checks if Rook has moved 
                    if (chosenPiece.getColor() == 'w') {
                        if (!(checkForSameCoordinates(chosenPieceCoordinates, this.whiteRookLeftStartTile) || checkForSameCoordinates(chosenPieceCoordinates, this.whiteRookRightStartTile))) {
                            if (chosenPiece.getHasMoved() == false) {
                                System.out.println("WR");
                                throw new IllegalArgumentException("The white rook has moved, but input says it has not!");
                            }
                        }
                    }
                    else if (chosenPiece.getColor() == 'b') {
                        if (!(checkForSameCoordinates(chosenPieceCoordinates, this.blackRookLeftStartTile) || checkForSameCoordinates(chosenPieceCoordinates, this.blackRookRightStartTile))) {
                            if (chosenPiece.getHasMoved() == false) {
                                System.out.println("BR");
                                throw new IllegalArgumentException("The black rook has moved, but input says it has not!");
                            }
                        }
                    }

                }

                else if (chosenPiece instanceof Pawn) { //checks if Pawn has moved, moved two last turn and that they are not placed on illegal rows

                    if (rowCount == 0 || rowCount == 7) {
                        throw new IllegalArgumentException("There are pawns on row 1 or 8 this is not allowed!");
                    }

                    if (chosenPiece.getHasMoved() == true || ((Pawn)chosenPiece).getMovedTwoLastTurn() == true) {
                        if (rowCount == 1 && chosenPiece.getColor() == 'w') {
                            throw new IllegalArgumentException("White pawn has not moved, but input says it has!");
                        }
                        else if (rowCount == 6 && chosenPiece.getColor() == 'b') {
                            throw new IllegalArgumentException("Black pawn has not moved, but input says it has!");
                        }
                    }

                    if (((Pawn)chosenPiece).getMovedTwoLastTurn() == true) {
                        if (rowCount != 3 && chosenPiece.getColor() == 'w') {
                            throw new IllegalArgumentException("White pawn cant have moved two last turn, but input says it has!"); 
                        }
                        else if (rowCount != 4 && chosenPiece.getColor() == 'b') {
                            throw new IllegalArgumentException("Black pawn cant have moved two last turn, but input says it has!"); 
                        }
                    }

                    if (chosenPiece.getHasMoved() == false) {
                        if (chosenPiece.getColor() == 'w' && rowCount != 1) {
                            throw new IllegalArgumentException("A white pawn has moves, but input says it has!");
                        }
                        else if (chosenPiece.getColor() == 'b' && rowCount != 6) {
                            throw new IllegalArgumentException("A white pawn has moves, but input says it has!");
                        }
                    }

                    pawnMoveNumbers.add(((Pawn)chosenPiece).getMoveNumberEnPassant());

                }
            }
        }

        int highestPawnMoveNumber = Collections.max(pawnMoveNumbers);

        if (checkLegalMoves.getMoveNumber() == 0) {
            if (highestPawnMoveNumber > 0) {
                throw new IllegalArgumentException("The pawn has a illegal move number, it is higher than the move number for the game!");
            }
        }
        else {
            if (highestPawnMoveNumber + 1 > checkLegalMoves.getMoveNumber()) {
                throw new IllegalArgumentException("The pawn has a illegal move number, it is higher than the move number for the game!");
            }
        }

        List<Integer> pawnMoveNumbersLargerThanZero = pawnMoveNumbers.stream()
                                                                     .filter(i -> i > 0)
                                                                     .toList();

        Set<Integer> uniquePawnMoveNumber = new HashSet<Integer>(pawnMoveNumbersLargerThanZero);

        if (pawnMoveNumbersLargerThanZero.size() != uniquePawnMoveNumber.size()) {
            throw new IllegalArgumentException("Two pawns cant have the same move number!");
        }
    
        if (whiteKingCount != 1 || blackKingCount != 1) {
            throw new IllegalArgumentException("Too many or too few kings, only two are allowed!");
        }

        HashMap<int[], ArrayList<int[]>> allMovesWhite = checkLegalMoves.populateAllMoves(whiteMovement, this.getBoardDeepCopyUsingSerialization());
        HashMap<int[], ArrayList<int[]>> allMovesBlack = checkLegalMoves.populateAllMoves(blackMovement, this.getBoardDeepCopyUsingSerialization());

        
        Collection<ArrayList<int[]>> onlyValuesAllMovesWhite = allMovesWhite.values();
        Collection<ArrayList<int[]>> onlyValuesAllMovesBlack = allMovesBlack.values();


        boolean whiteKingInCheck = false;
        boolean blackKingInCheck = false;
            
        for (ArrayList<int[]> allMovesForAPiece: onlyValuesAllMovesWhite) {
            for (int[] oneMove : allMovesForAPiece) {
                if (checkForSameCoordinates(oneMove, blackKingLocation)) {
                    blackKingInCheck = true;
                }
            }
        }

        for (ArrayList<int[]> allMovesForAPiece: onlyValuesAllMovesBlack) {
            for (int[] oneMove : allMovesForAPiece) {
                if (checkForSameCoordinates(oneMove, whiteKingLocation)) {
                    whiteKingInCheck = true;
                }
            }
        }
        
        if (whiteKingInCheck && blackKingInCheck) {
            throw new IllegalArgumentException("Both kings cant be in check!");
        }
    }


    private boolean checkForSameCoordinates(int[] coordinateOne, int[] coordinateTwo) {
        if (coordinateOne[0] == coordinateTwo[0] && coordinateOne[1] == coordinateTwo[1]) {
            return true;
        }
        return false;
    }



    /*  SAVE INFO STRING STRUCTURE
                                 (w/b)Char       (1 or 0)         (1 or 0)               Positive int
        Pawn(P):            color and piece type=has moved+moved two spaces last turn=en passen move number-
        King or rook(X/R):  color and piece type=has moved-
        Other piece(Q/K/B): color and piece type-
        Empty tile:         00-
    */

    // public void saveGame(String saveName) {
    //     SaveGames saveGameWriter = new SaveGames();
    //     String saveGameData = new String();

    //     for (Tile[] row : boardTiles) {
    //         for (Tile tile : row) {
    //             if (tile.isOccupied()) {
                    
    //                 Piece piece = tile.getPiece();
                    
    //                 if (piece instanceof Pawn) {
    //                     Pawn pawn = (Pawn)piece;                

    //                     saveGameData += pawn.getSpriteId() + "=";
    //                     saveGameData += ((pawn.getHasMoved()) ? "1" : "0") + "=";
    //                     saveGameData += ((pawn.getMovedTwoLastTurn()) ? "1" : "0") + "=";
    //                     saveGameData += pawn.getMoveNumberEnPassant();
    //                 }
    //                 else if (piece instanceof Rook || piece instanceof King) {

    //                     saveGameData += piece.getSpriteId() + "=";
    //                     saveGameData += ((piece.getHasMoved()) ? "1" : "0");
    //                 }
    //                 else {
    //                     saveGameData += piece.getSpriteId();
    //                 }

    //                 saveGameData += "-";
    //             }
    //             else {
    //                 saveGameData += "00-";
    //             }
    //         }
    //     }

    //     saveGameData += checkLegalMoves.getMoveNumber();
        
    //     saveGameWriter.saveGame(saveName, saveGameData);

    //     System.out.println(saveGameData);
    // }

    @Override
    public BoardTileIterator iterator() {
        return new BoardTileIterator(this);
    } 

    public static void main(String[] args) {
        Game game = new Game();
        game.isMoveCastling(8, -1, 0, 2);
    }
}
