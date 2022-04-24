package project.Movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import project.Consts;
//import project.Board.Chessboard; TODO: Delete, right?
import project.Board.Tile;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Piece;
import project.Pieces.Queen;
import project.Pieces.Rook;
import project.Pieces.Pawn;


public class CheckLegalMoves {

    private int gameStatus = Consts.GAME_NOT_OVER;

    private MovementPatterns whiteMovement;
    private MovementPatterns blackMovement;
    private MovementPatterns ourMovementPattern;
    private MovementPatterns opponentMovementPattern;

    //private int[] kingLocationplayerThatMoves;

    //Important tiles for when white is Castling
    private final int[] whiteKingStartPos             = new int[]{0, 4};
    private final int[] whiteKingMoveCastlingRight    = new int[]{0, 6};
    private final int[] whiteKingMoveCastlingLeft     = new int[]{0, 2};
    private final int[] whiteCastlingSkippedTileRight = new int[]{0, 5};
    private final int[] whiteCastlingSkippedTileLeft  = new int[]{0, 3};

    //Important tiles for when black is Castling  
    private final int[] blackKingStartPos             = new int[]{7, 4};
    private final int[] blackKingMoveCastlingRight    = new int[]{7, 6};
    private final int[] blackKingMoveCastlingLeft     = new int[]{7, 2};
    private final int[] blackCastlingSkippedTileRight = new int[]{7, 5};
    private final int[] blackCastlingSkippedTileLeft  = new int[]{7, 3};

    //TODO: legge til at man ikke kan kalle på metodene hvis spillet er over.

    //Turn number
    int moveNumber = 0; 

    public CheckLegalMoves() {
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
          
    }

    //Finds all the possible moves for black or white when check is not considered
    private HashMap<int[], ArrayList<int[]>> findAllMoves(MovementPatterns movementPattern, Tile[][] boardTiles) {

        //validationOfGameState throws illegalArgumentException
        //this.validationOfGameState(boardTiles, this.moveNumber);

        //The key is the coordinates [row, col] of the tile a piece is standing on. 
        //The value is an ArrayList of the coordinates [row, col] of the tiles the piece can move to
        HashMap<int[], ArrayList<int[]>> legalMoves = new HashMap<int[], ArrayList<int[]>>();
        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {
                if (tile.isOccupied() 
                    && tile.getPiece().getColor() == movementPattern.getColor()) {
                    
                    //TODO: kanskje bytte ut navn her 
                    int[] key = new int[] {tile.getRow(), tile.getCol()};
                    ArrayList<int[]> allMoves = movementPattern.moveHandler(tile, boardTiles, this.moveNumber);

                    legalMoves.put(key, allMoves);
                }
            }
        }
        return legalMoves;
    }

    private HashMap<int[], ArrayList<int[]>> eliminateChecks(Tile[][] currentPositionTiles) {

        /*  EXPLANATION OF THE ALGORITHM
            The position of a piece is represented by an int array of the coordinates of the tile 
            the piece is standing on, in the form [row, col]. Likewise, a move is represented by
            an int array of the coordinates of the tile the piece can move to, in the same form.

            The algorithm first finds all of the possible moves for the current player's pieces. 
            This method then looks for moves that are illegal due to putting the current player's 
            king in check.
            
            It does this by iterating through all of the current player's moves, and for each move, 
            it updates currentGamePositionTiles to the new board position. For every new position,
            it iterates through all of the opposite player's moves for that position. 
            
            If any of the opponent's moves can put the current player's king in check, the current 
            player's move that was performed is flagged as illegal, and will be removed from the 
            current player's possible moves
            
            When all of the opponent's moves have been checked on the given position,
            currentGamePositionTiles is reset to the starting position for this turn, and the
            process is repeated for the next of the current player's move.
        */

        int[] whiteKingPos = new int[]{};
        int[] blackKingPos = new int[]{};

        //Finds the position of both kings
        for (Tile[] row : currentPositionTiles) {
            for (Tile tile: row) {
                if (tile.getPiece() instanceof King && ((King)tile.getPiece()).getColor() == 'w') {
                    whiteKingPos = new int[]{tile.getRow(), tile.getCol()};
                }
                else if (tile.getPiece() instanceof King && ((King)tile.getPiece()).getColor() == 'b') {
                    blackKingPos = new int[]{tile.getRow(), tile.getCol()};
                }
            }
        }

        setPlayerThatMoves();

        //Us is the player who is moving this turn
        //Opponent is the other player
        int[] ourKingStartPos;
        ourKingStartPos = (ourMovementPattern.getColor() == 'w') ? whiteKingStartPos : blackKingStartPos;

        int[] ourKingPos;
        ourKingPos = (ourMovementPattern.getColor() == 'w') ? whiteKingPos : blackKingPos;


        HashMap<int[], ArrayList<int[]>> movesForAllOurPieces = findAllMoves(ourMovementPattern, currentPositionTiles);
        
        for (int[] ourPiece : movesForAllOurPieces.keySet()) {

            ArrayList<int[]> ourPieceMoves = movesForAllOurPieces.get(ourPiece);
            ArrayList<int[]> movesToRemove = new ArrayList<int[]>();

            //Count for what move that should be removed from piece moves, move that is illegal because of CHECK
            int deleteMoveAtIndex = 0;

            for (int[] ourMove : ourPieceMoves) {
 
                int ourMoveFromRow = ourPiece[0];
                int ourMoveFromCol = ourPiece[1];

                int ourMoveToRow = ourMove[0];
                int ourMoveToCol = ourMove[1];
                
                Piece pieceToMove   = currentPositionTiles[ourMoveFromRow][ourMoveFromCol].getPiece();
                Piece pieceAtMoveTo = currentPositionTiles[ourMoveToRow][ourMoveToCol].getPiece();

                //Checks if king moves, if yes, fields have to be updated
                boolean whiteKingMoves = false;
                boolean blackKingMoves = false;

                if (pieceToMove instanceof King && pieceToMove.getColor() == 'w') {
                    whiteKingPos   = new int[]{ourMoveToRow, ourMoveToCol};
                    whiteKingMoves = true;
                }
                
                if (pieceToMove instanceof King && pieceToMove.getColor() == 'b') {
                    blackKingPos   = new int[]{ourMoveToRow, ourMoveToCol};
                    blackKingMoves = true;
                }

                //Moves the piece 
                currentPositionTiles[ourMoveFromRow][ourMoveFromCol].removePiece();
                currentPositionTiles[ourMoveToRow][ourMoveToCol].setPiece(pieceToMove);
                
                //Updates the king position
                ourKingPos = (ourMovementPattern.getColor() == 'w') ? whiteKingPos : blackKingPos;
                
                HashMap<int[], ArrayList<int[]>> allOpponentMoves           = findAllMoves(opponentMovementPattern, currentPositionTiles);
                Collection<ArrayList<int[]>>     allOpponentMoveCoordinates = allOpponentMoves.values();

                for (ArrayList<int[]> opponentPieceMoves: allOpponentMoveCoordinates) {
                    for (int[] opponentMove : opponentPieceMoves) {
                        
                        //bjn54                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
                        if (checkForSameCoordinates(ourKingPos, opponentMove)) {
                            for (int[] oneOfAllMoves : ourPieceMoves) {
                                if (checkForSameCoordinates(oneOfAllMoves, ourMove)) {
                                    movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                }
                            }
                        }
                        
                        //Handling of legal castling 
                        else if (pieceToMove instanceof King && !pieceToMove.getHasMoved()) {
                            if (ourMovementPattern.getColor() == 'w') {

                                //Checks if Right castling for white is legal 
                                if (checkForSameCoordinates(ourMove, this.whiteKingMoveCastlingRight)) {
                                    if (checkForSameCoordinates(opponentMove, ourKingStartPos)) { //examines if the king is already in check
                                        movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                    }
                                    else if (checkForSameCoordinates(opponentMove, this.whiteCastlingSkippedTileRight)) { //examines if the tile that the king skips/moves over is check
                                        movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                    }
                                    else if (currentPositionTiles[1][6].isOccupied()) { //examines if there is a pawn that attacks the skipped tile
                                        if (currentPositionTiles[1][6].getPiece().getColor() == 'b' && currentPositionTiles[1][6].getPiece() instanceof Pawn) {
                                            movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                        }
                                    }

                                }
                                //Checks if left castling for white is legal 
                                else if (checkForSameCoordinates(ourMove, this.whiteKingMoveCastlingLeft)) {         
                                    if (checkForSameCoordinates(opponentMove, ourKingStartPos)) {
                                        movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                    }
                                    else if (checkForSameCoordinates(opponentMove, this.whiteCastlingSkippedTileLeft)) {
                                        movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                    }
                                    else if (currentPositionTiles[1][2].isOccupied()) { 
                                        if (currentPositionTiles[1][2].getPiece().getColor() == 'b' && currentPositionTiles[1][2].getPiece() instanceof Pawn) {
                                            movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                        }
                                    }
                                }
                            }
                            else if (ourMovementPattern.getColor() == 'b') {
                                
                                //Checks if Right castling for black is legal 
                                if (checkForSameCoordinates(ourMove, this.blackKingMoveCastlingRight)) {
                                    if (checkForSameCoordinates(opponentMove, ourKingStartPos)) {
                                        movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                    }
                                    else if (checkForSameCoordinates(opponentMove, this.blackCastlingSkippedTileRight)) {
                                        movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                    }
                                    else if (currentPositionTiles[6][6].isOccupied()) { 
                                        if (currentPositionTiles[6][6].getPiece().getColor() == 'w' && currentPositionTiles[6][6].getPiece() instanceof Pawn) {
                                            movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                        }
                                    }

                                }
                                //Checks if left castling for black is legal 
                                else if (checkForSameCoordinates(ourMove, this.blackKingMoveCastlingLeft)) {
                                    if (checkForSameCoordinates(opponentMove, ourKingStartPos)) {
                                        movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                    }
                                    else if (checkForSameCoordinates(opponentMove, this.blackCastlingSkippedTileLeft)) {
                                        movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                    }
                                    else if (currentPositionTiles[6][2].isOccupied()) { 
                                        if (currentPositionTiles[6][2].getPiece().getColor() == 'w' && currentPositionTiles[6][2].getPiece() instanceof Pawn) {
                                            movesToRemove.add(ourPieceMoves.get(deleteMoveAtIndex));
                                        }
                                    }
                                }
                            } 
                        }     
                    }
                }
                

                //Moves the piece we moved back 
                Piece pieceToMoveBack = currentPositionTiles[ourMoveToRow][ourMoveToCol].getPiece();
                currentPositionTiles[ourMoveFromRow][ourMoveFromCol].setPiece(pieceToMoveBack);

                //Replaces the piece that was at the spot we moved to 
                currentPositionTiles[ourMoveToRow][ourMoveToCol].removePiece();
                currentPositionTiles[ourMoveToRow][ourMoveToCol].setPiece(pieceAtMoveTo);

                //TODO: CHANGED: setPiece now checks if the argument piece is null, and sets occupied appropriately
                //Since setPiece also makes Occupied = True, this has to handeled
                // if (currentPositionTiles[ourMoveToRow][ourMoveToCol].getPiece() == null) {
                //     currentPositionTiles[ourMoveToRow][ourMoveToCol].setOccupied(false);
                // }
                
                //REDUNDANT, this was performed above
                //currentPositionTiles[ourMoveFromRow][ourMoveFromCol].setPiece(pieceToMoveBack);

                //Reset King field if king moved 
                if (whiteKingMoves) {
                    whiteKingPos = new int[]{ourMoveFromRow, ourMoveFromCol};
                }
                if (blackKingMoves) {
                    blackKingPos = new int[]{ourMoveFromRow, ourMoveFromCol};
                }
                
                ++deleteMoveAtIndex;
            }
            //Removes all the illegal moves for a piece that breaks check
            ourPieceMoves.removeAll(movesToRemove);
        }

        return movesForAllOurPieces;
    }

    private boolean checkForSameCoordinates(int[] coordinateOne, int[] coordinateTwo) {
        if (coordinateOne[0] == coordinateTwo[0] && coordinateOne[1] == coordinateTwo[1]) {
            return true;
        }
        return false;
    }
    
    //DOKUMENTASJON
    //TODO: Hva skjer hvis man sender inn et brett der hvit har vunnet men det er hvit sin tur
    public HashMap<int[], ArrayList<int[]>> checkforCheckMateAndPat(Tile[][] currentGamePositionTiles) {

        this.validationOfGameState(currentGamePositionTiles, this.moveNumber);

        HashMap<int[], ArrayList<int[]>> legalMoves = eliminateChecks(currentGamePositionTiles);
        
        boolean kingCanBetaken = false;
        boolean gameOver = true;

        int[] whiteKing = new int[]{};
        int[] blackKing = new int[]{};

        for (Tile[] tileRow : currentGamePositionTiles) {
            for (Tile tile: tileRow) {
                if (tile.getPiece() instanceof King && ((King)tile.getPiece()).getColor() == 'w') {
                    whiteKing = new int[]{tile.getRow(), tile.getCol()};
                }
                else if (tile.getPiece() instanceof King && ((King)tile.getPiece()).getColor() == 'b') {
                    blackKing = new int[]{tile.getRow(), tile.getCol()};
                }
            }
        }

        int[] kingLocationplayerThatMoves = (ourMovementPattern.getColor() == 'w') ? whiteKing : blackKing;
        
        for (int[] pieceplayerThatMoves : legalMoves.keySet()) {
            if (!gameOver) {
                break;
            }

            if (legalMoves.get(pieceplayerThatMoves).size() != 0) {
                gameOver = false;
            }
        }

        if (gameOver) {

            HashMap<int[], ArrayList<int[]>> allPiecesAndMovesOppositeColor = findAllMoves(opponentMovementPattern, currentGamePositionTiles);
            Collection<ArrayList<int[]>> allPossibleMovesThatCouldTakeTheKing = allPiecesAndMovesOppositeColor.values();

            for (ArrayList<int[]> movesOppositePiece: allPossibleMovesThatCouldTakeTheKing) {
                
                if (kingCanBetaken) {
                    break;
                }

                for (int[] oneOppositeMove : movesOppositePiece) {
                    if (checkForSameCoordinates(oneOppositeMove, kingLocationplayerThatMoves)) {

                        kingCanBetaken = true;
                        break;
                    }
                }
            } 
        }
        
        if (kingCanBetaken && gameOver) {
            this.gameStatus = Consts.CHECKMATE;
        } 
        else if (!kingCanBetaken && gameOver) {
            this.gameStatus = Consts.PAT;
        }

        return legalMoves;
    }

    //TODO: Må vi validere noe tilstand her?
    public void increaseMoveNumber () {
        this.moveNumber ++;
    }

    public int getMoveNumber() {
        return this.moveNumber;
    }

    public int getGameStatus() {
        return this.gameStatus;
    }

    public void setMoveNumber(int moveNumber) {
        if (this.moveNumber < 0)
            throw new IllegalArgumentException("Movenumber must be greater than or equal to 0");
            
        this.moveNumber = moveNumber;
    }

    private void setPlayerThatMoves () {
        if (this.moveNumber % 2 == 0) {
            this.ourMovementPattern = whiteMovement;
            this.opponentMovementPattern = blackMovement;
        } 
        else {
            this.ourMovementPattern = blackMovement;
            this.opponentMovementPattern = whiteMovement;
        }
    }

    // This method validates that the loaded game position is valid according to the logic used to play the game.
    // Validating the string directly is much harder, therefore this method checks that game is in a legal state according to the game logic after loading a game.
    // This method will allow illegal chess positions, if they dont break the logic used in the program, and from the loaded position it will follow normal chess rules.
    // Exampels of positions that are allowed:
    // The king can start at a chosen position, but there needs to be 1 white king and 1 black king, and the player not Moving cant be in check. 
    // Pawns cant be placed at row 1 and 8.
    // And some other requirements that could/will break the game logic are checked.
    // From this you can write your own chess positions as strings and make custom starts that follow normal chess rules from that point. 

    //TODO: Change movenumber?
    public void validationOfGameState(Tile[][] currentGamePosition, int moveNumber) {

        int[] whiteKingLocation = new int[]{};
        int[] blackKingLocation = new int[]{};
        int whiteKingCount = 0;
        int blackKingCount = 0;
        int rowCount = -1;
        ArrayList<Integer> pawnMoveNumbers = new ArrayList<Integer>();


        if (!(currentGamePosition.length == 8 && currentGamePosition[0].length == 8 && moveNumber >= 0))
            throw new IllegalArgumentException("Board size must be 8x8 and moveNumber must be greater than or equal to 0");

        for (Tile[] row : currentGamePosition) {
            rowCount++;
            for (Tile tile : row) {

                Piece chosenPiece = tile.getPiece();
                int[] chosenPieceCoordinates = tile.getCoordinates();
                
                if (tile.getPiece() instanceof King) { //Checks that there is only one black king and one white king
                    if (chosenPiece.getColor() == 'b') {
                        blackKingCount++;
                        blackKingLocation = chosenPieceCoordinates;
                        if ((!(checkForSameCoordinates(this.blackKingStartPos, chosenPieceCoordinates))) && chosenPiece.getHasMoved() == false) {
                            throw new IllegalArgumentException("The Black king has moved, but the input says it has not!");
                        }
                        
                    }
                    else if (chosenPiece.getColor() == 'w') {
                        whiteKingCount++;
                        whiteKingLocation = chosenPieceCoordinates;
                         if ((!(checkForSameCoordinates(this.whiteKingStartPos, chosenPieceCoordinates))) && chosenPiece.getHasMoved() == false) {
                            throw new IllegalArgumentException("The white king has moved, but the input says it has not!");
                        }
                    }
                }

                else if (chosenPiece instanceof Pawn) { //Checks if Pawns are placed on illegal rows

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
                            throw new IllegalArgumentException("A white pawn has moved, but input says it has not!");
                        }
                        else if (chosenPiece.getColor() == 'b' && rowCount != 6) {
                            throw new IllegalArgumentException("A white pawn has moved, but input says it has not!");
                        }
                    }

                    pawnMoveNumbers.add(((Pawn)chosenPiece).getMoveNumberEnPassant());
                }
            }
        }
    
        if (whiteKingCount != 1 || blackKingCount != 1) {
            throw new IllegalArgumentException("Too many or too few kings, only two are allowed!");
        }

        int highestPawnMoveNumber = 0;

        if (pawnMoveNumbers.size() != 0)
            highestPawnMoveNumber = Collections.max(pawnMoveNumbers);

        if (moveNumber == 0) {
            if (highestPawnMoveNumber > 0) {
                throw new IllegalArgumentException("The pawn has a illegal move number, it is higher than the move number for the game!");
            }
        }
        else {
            if (highestPawnMoveNumber + 1 > moveNumber) {
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

        boolean playerNotToMoveInCheck = false;

        //If setPlayerMove returns true - white is moving
        if (setPlayerToMove(moveNumber)) {
            HashMap<int[], ArrayList<int[]>> allMovesWhite = this.findAllMoves(this.whiteMovement, currentGamePosition);
            Collection<ArrayList<int[]>> onlyValuesAllMovesWhite = allMovesWhite.values();
            
            for (ArrayList<int[]> allMovesForAPiece: onlyValuesAllMovesWhite) {
                for (int[] oneMove : allMovesForAPiece) {
                    if (checkForSameCoordinates(oneMove, blackKingLocation)) {
                        playerNotToMoveInCheck = true;
                    }
                }
            }

        }
        else {
            HashMap<int[], ArrayList<int[]>> allMovesBlack = this.findAllMoves(this.blackMovement, currentGamePosition);
            Collection<ArrayList<int[]>> onlyValuesAllMovesBlack = allMovesBlack.values();

            for (ArrayList<int[]> allMovesForAPiece: onlyValuesAllMovesBlack) {
                for (int[] oneMove : allMovesForAPiece) {
                    if (checkForSameCoordinates(oneMove, whiteKingLocation)) {
                        playerNotToMoveInCheck = true;
                    }
                }
            }
        }
        
        if (playerNotToMoveInCheck) {
            throw new IllegalArgumentException("The player not moving is in check, this is not allowed!");
        }
    }

     //Returns true if white is moving and false if black is moving.
    private boolean setPlayerToMove(int moveNumber) {

        return (moveNumber % 2 == 0) ? true : false;
    }


    public void keyWriter(Tile[][] boardTiles) {
        
        HashMap<int[], ArrayList<int[]>> movesForAllOurPieces = eliminateChecks(boardTiles);
        Set<int[]> keys = movesForAllOurPieces.keySet(); 

        String keyString = new String();
        
        for (int[] key : keys) {
            keyString += "new " + "int[]" + "{" + key[0] + ", " + key[1] + "}" + ", ";
        }

        System.out.println(keyString);
    }


    public static void main(String[] args) {

        Game game = new Game();
        MovementPatterns movementPattern = new MovementPatterns('w');
        CheckLegalMoves checklegalmoves = new CheckLegalMoves();

        game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6");
        Tile[][] board = game.getBoardTilesDeepCopy();
        checklegalmoves.keyWriter(board);



        /*
        long startTime = System.nanoTime();
        Chessboard chessboard = new Chessboard();
        Tile[][] tiles = chessboard.getBoardTiles();

        Rook bR1 = new Rook("bR1", 'b');
        Rook bR2 = new Rook("bR2", 'b');
        Rook bR3 = new Rook("bR3", 'b');
        Rook bR4 = new Rook("bR4", 'b');
        Rook bR5 = new Rook("bR5", 'b');

        Rook wR1 = new Rook("wR1", 'w');
        Rook wR2 = new Rook("wR2", 'w');
        Rook wR3 = new Rook("wR3", 'w');
        Rook wR4 = new Rook("wR4", 'w');
        Rook wR5 = new Rook("wR5", 'w');

        Bishop bB1 = new Bishop("bB1", 'b');
        Bishop bB2 = new Bishop("bB2", 'b');
        Bishop bB3 = new Bishop("bB3", 'b');
        Bishop bB4 = new Bishop("bB4", 'b');

        Bishop wB1 = new Bishop("wB1", 'w');
        Bishop wB2 = new Bishop("wB2", 'w');
        Bishop wB3 = new Bishop("wB3", 'w');
        Bishop wB4 = new Bishop("wB4", 'w');

        Pawn bP1 = new Pawn("bP1", 'b');
        Pawn bP2 = new Pawn("bP2", 'b');
        Pawn bP3 = new Pawn("bP3", 'b');
        Pawn bP4 = new Pawn("bP4", 'b');
        Pawn bP5 = new Pawn("bP5", 'b');

        Pawn wP1 = new Pawn("wP1", 'w');
        Pawn wP2 = new Pawn("wP2", 'w');
        Pawn wP3 = new Pawn("wP3", 'w');
        Pawn wP4 = new Pawn("wP4", 'w');
        Pawn wP5 = new Pawn("wP5", 'w');

        King wX1 = new King("wX2", 'w');

        King bX1 = new King("bX2", 'b');

        Queen bQ1 = new Queen("bQ1", 'b');

        Queen wQ1 = new Queen("wQ1", 'w');

        Knight bK1 = new Knight("bK1", 'b');
        Knight bK2 = new Knight("bK2", 'b');
        Knight bK3 = new Knight("bK3", 'b');

        Knight wK1 = new Knight("wK1", 'w');
        Knight wK2 = new Knight("wK2", 'w');
        Knight wK3 = new Knight("wK3", 'w');


        // remove pawns for white 
        tiles[1][0].removePiece();
        tiles[1][1].removePiece();
        tiles[1][2].removePiece();
        tiles[1][3].removePiece();
        tiles[1][4].removePiece();
        tiles[1][5].removePiece();
        tiles[1][6].removePiece();
        tiles[1][7].removePiece();
        
        // remove backrank white 
        tiles[0][1].removePiece();
        tiles[0][2].removePiece();
        tiles[0][3].removePiece();
        tiles[0][0].removePiece();
        tiles[0][5].removePiece();
        tiles[0][6].removePiece();
        tiles[0][7].removePiece();
        tiles[0][4].removePiece();

        // remove pawns for black
        tiles[6][0].removePiece();
        tiles[6][1].removePiece();
        tiles[6][2].removePiece();
        tiles[6][3].removePiece();
        tiles[6][4].removePiece();
        tiles[6][5].removePiece();
        tiles[6][6].removePiece();
        tiles[6][7].removePiece();
        
        // remove backrank black 
        tiles[7][1].removePiece();
        tiles[7][2].removePiece();
        tiles[7][3].removePiece();
        tiles[7][0].removePiece();
        tiles[7][5].removePiece();
        tiles[7][6].removePiece();
        tiles[7][7].removePiece();
        tiles[7][4].removePiece();

        tiles[0][4].setPiece(wX1);
        tiles[7][4].setPiece(bX1);

        //tiles[0][0].setPiece(wR1);
        //tiles[0][7].setPiece(wR2);

        tiles[7][0].setPiece(bR1);
        tiles[7][7].setPiece(bR2);

        //tiles[1][4].setPiece(wK1);

        tiles[5][3].setPiece(wQ1);
        //tiles[5][6].setPiece(bP1);
        //bP1.setMovedTwoLastTurn(true);
        


       
        chessboard.printBoard();

        //CheckLegalMoves checklegalmoves = new CheckLegalMoves(tiles);


        //checklegalmoves.CheckforCheckMateAndPat();
        
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
        */
    }
}


