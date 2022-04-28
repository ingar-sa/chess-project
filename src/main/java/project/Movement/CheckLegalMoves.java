package project.Movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import project.Consts;
import project.Board.Tile;
import project.Pieces.King;
import project.Pieces.Piece;
import project.Pieces.Pawn;


public class CheckLegalMoves {

    private int gameStatus = Consts.GAME_NOT_OVER;

    private MovementPatterns whiteMovement;
    private MovementPatterns blackMovement;
    private MovementPatterns ourMovementPattern;
    private MovementPatterns opponentMovementPattern;

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

    //Turn number
    private int moveNumber = 0; 

    public CheckLegalMoves() {
        
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
          
    }

    public HashMap<int[], ArrayList<int[]>> checkforCheckmateAndPat(Tile[][] currentGamePositionTiles) {

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

        int[] playerThatMovesKingLocation = (ourMovementPattern.getColor() == 'w') ? whiteKing : blackKing;
        
        for (int[] pieceplayerThatMoves : legalMoves.keySet()) {
            if (!gameOver) {
                break;
            }

            if (legalMoves.get(pieceplayerThatMoves).size() != 0) {
                gameOver = false;
            }
        }

        if (gameOver) {

            HashMap<int[], ArrayList<int[]>> allPiecesAndMovesOppositeColor = findAllMoves(this.opponentMovementPattern, currentGamePositionTiles);
            Collection<ArrayList<int[]>> allPossibleMovesThatCouldTakeTheKing = allPiecesAndMovesOppositeColor.values();

            for (ArrayList<int[]> movesOppositePiece: allPossibleMovesThatCouldTakeTheKing) {
                
                if (kingCanBetaken) {
                    break;
                }

                for (int[] oneOppositeMove : movesOppositePiece) {
                    if (checkForSameCoordinates(oneOppositeMove, playerThatMovesKingLocation)) {

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

        setMovementPattern();

        //Us is the player who is moving this turn
        //Opponent is the other player
        int[] ourKingStartPos;
        ourKingStartPos = (this.ourMovementPattern.getColor() == 'w') ? whiteKingStartPos : blackKingStartPos;

        int[] ourKingPos;
        ourKingPos = (this.ourMovementPattern.getColor() == 'w') ? whiteKingPos : blackKingPos;


        HashMap<int[], ArrayList<int[]>> movesForAllOurPieces = findAllMoves(this.ourMovementPattern, currentPositionTiles);
        
        for (int[] ourPiece : movesForAllOurPieces.keySet()) {

            ArrayList<int[]> ourPieceMoves = movesForAllOurPieces.get(ourPiece);
            ArrayList<int[]> movesToRemove = new ArrayList<int[]>();

            //Count for what move that should be removed from piece moves, move that is illegal because of check
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
                ourKingPos = (this.ourMovementPattern.getColor() == 'w') ? whiteKingPos : blackKingPos;
                
                HashMap<int[], ArrayList<int[]>> allOpponentMoves           = findAllMoves(this.opponentMovementPattern, currentPositionTiles);
                Collection<ArrayList<int[]>>     allOpponentMoveCoordinates = allOpponentMoves.values();

                for (ArrayList<int[]> opponentPieceMoves: allOpponentMoveCoordinates) {
                    for (int[] opponentMove : opponentPieceMoves) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
                        
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

    private void setMovementPattern () {
        if (this.moveNumber % 2 == 0) {
            this.ourMovementPattern = whiteMovement;
            this.opponentMovementPattern = blackMovement;
        } 
        else {
            this.ourMovementPattern = blackMovement;
            this.opponentMovementPattern = whiteMovement;
        }
    }

    //Finds all the possible moves for black or white when check is not considered
    private HashMap<int[], ArrayList<int[]>> findAllMoves(MovementPatterns movementPattern, Tile[][] boardTiles) {

        //The key is the coordinates [row, col] of the tile a piece is standing on. 
        //The value is an ArrayList of the coordinates [row, col] of the tiles the piece can move to
        HashMap<int[], ArrayList<int[]>> legalMoves = new HashMap<int[], ArrayList<int[]>>();
        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {
                if (tile.isOccupied() 
                    && tile.getPiece().getColor() == movementPattern.getColor()) {
                    
                    int[] key = new int[] {tile.getRow(), tile.getCol()};
                    ArrayList<int[]> allMoves = movementPattern.moveHandler(tile, boardTiles, this.moveNumber);

                    legalMoves.put(key, allMoves);
                }
            }
        }
        return legalMoves;
    }

    public void increaseMoveNumber () {
        this.moveNumber++;
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

    public void validationOfGameState(Tile[][] currentGamePosition, int turnNumber) {
        
        /*
        EXPLANATION OF THE METHOD
        This method validates that the loaded game position is valid according to the logic used to play the game.
        Validating the string directly is much harder, therefore this method checks that game is in a legal state according to the game logic after loading a game.
        This method will allow illegal chess positions, if they dont break the logic used in the program, and from the loaded position it will follow normal chess rules.

        Exampels of positions that are allowed:
        - The king can start at a chosen position, but there needs to be 1 white king and 1 black king, and the player not Moving cant be in check. 
        - Pawns cant be placed at row 1 and 8.
        - And some other requirements that could/will break the game logic are checked.
        
        From this you can write your own chess positions as strings and make custom starts that follow normal chess rules from that point. 
        */


        int[] whiteKingLocation = new int[]{};
        int[] blackKingLocation = new int[]{};
        int whiteKingCount = 0;
        int blackKingCount = 0;
        int rowCount = -1;
        ArrayList<Integer> pawnMoveNumbers = new ArrayList<Integer>();

        if (currentGamePosition == null) {
           throw new IllegalArgumentException("Input cant be null"); 
        }

        if (!(currentGamePosition.length == 8 && currentGamePosition[0].length == 8 && turnNumber >= 0))
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

        if (turnNumber == 0) {
            if (highestPawnMoveNumber > 0) {
                throw new IllegalArgumentException("The pawn has a illegal move number, it is higher than the move number for the game!");
            }
        }
        else {
            if (highestPawnMoveNumber + 1 > turnNumber) {
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
        if (setPlayerToMove(turnNumber)) {
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

    private boolean checkForSameCoordinates(int[] coordinateOne, int[] coordinateTwo) {
        if (coordinateOne[0] == coordinateTwo[0] && coordinateOne[1] == coordinateTwo[1]) {
            return true;
        }
        return false;
    }

    //Returns true if white is moving and false if black is moving.
    private boolean setPlayerToMove(int moveNumber) {
        return (moveNumber % 2 == 0) ? true : false;
    }

}


