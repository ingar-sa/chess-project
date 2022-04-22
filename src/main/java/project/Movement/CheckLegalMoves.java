package project.Movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
    private MovementPatterns colorNotMoving;

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
    public HashMap<int[], ArrayList<int[]>> findAllMoves(MovementPatterns movementPattern, Tile[][] boardTiles) {
        
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
 
                int ourPieceRow = ourPiece[0];
                int ourPieceCol = ourPiece[1];

                int ourMoveToRow = ourMove[0];
                int ourMoveToCol = ourMove[1];
                
                Piece pieceToMove   = currentPositionTiles[ourPieceRow][ourPieceCol].getPiece();
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
                currentPositionTiles[ourPieceRow][ourPieceCol].removePiece();
                currentPositionTiles[ourMoveToRow][ourMoveToCol].setPiece(pieceToMove);
                
                //Updates the king position
                ourKingPos = (ourMovementPattern.getColor() == 'w') ? whiteKingPos : blackKingPos;
                
                HashMap<int[], ArrayList<int[]>> allOpponentMoves           = findAllMoves(colorNotMoving, currentPositionTiles);
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
                currentPositionTiles[ourPieceRow][ourPieceCol].setPiece(pieceToMoveBack);

                //Replaces the piece that was at the spot we moved to 
                currentPositionTiles[ourMoveToRow][ourMoveToCol].removePiece();
                currentPositionTiles[ourMoveToRow][ourMoveToCol].setPiece(pieceAtMoveTo);

                //Since setPiece also makes Occupied = True, this has to handeled
                if (currentPositionTiles[ourMoveToRow][ourMoveToCol].getPiece() == null) {
                    currentPositionTiles[ourMoveToRow][ourMoveToCol].setOccupied(false);
                }
                
                currentPositionTiles[ourPieceRow][ourPieceCol].setPiece(pieceToMoveBack);

                //Reset King field if king moved 
                if (whiteKingMoves) {
                    whiteKingPos = new int[]{ourPieceRow, ourPieceCol};
                }
                if (blackKingMoves) {
                    blackKingPos = new int[]{ourPieceRow, ourPieceCol};
                }
                
                deleteMoveAtIndex ++;
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

    public HashMap<int[], ArrayList<int[]>> checkforCheckMateAndPat(Tile[][] currentGamePositionTiles) {

        //TODO: se mer på dette, kongene kan ikke være inntill hverandre kanskje, sjekke at max en spiller er i sjakk, sjekke at pawns ikke står feil, hvis kongen ikke står på start feltet må den ha flyttet på seg, samme med bonde og tårn!?
        // validationOfKings(currentGamePositionTiles);

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

        int[] kingLocationplayerThatMoves;

        if (ourMovementPattern.getColor() == 'w') {
            kingLocationplayerThatMoves = whiteKing;
        }
        else {
            kingLocationplayerThatMoves = blackKing;
        }

        for (int[] pieceplayerThatMoves : legalMoves.keySet()) {
            if (!gameOver) {
                break;
            }

            if (legalMoves.get(pieceplayerThatMoves).size() != 0) {
                gameOver = false;
            }
        }

        if (gameOver) {

            HashMap<int[], ArrayList<int[]>> allPiecesAndMovesOppositeColor = findAllMoves(colorNotMoving, currentGamePositionTiles);
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
        if (moveNumber >= 0) 
            this.moveNumber = moveNumber;
        else 
            throw new IllegalArgumentException("Illegal move number!");
    }

    private void setPlayerThatMoves () {
        if (moveNumber % 2 == 0) {
            this.ourMovementPattern = whiteMovement;
            this.colorNotMoving = blackMovement;
        } 
        else {
            this.ourMovementPattern = blackMovement;
            this.colorNotMoving = whiteMovement;
        }
    }

    // private void setOriginalKingLocationForplayerThatMoves () {
    //     if (colorToMove.getColor() == 'w') {
    //         this.originalKingLocationplayerThatMoves = new int[]{0, 4};
    //     }
    //     else {
    //         this.originalKingLocationplayerThatMoves = new int[]{7, 4};
    //     }
    // }

    // private void setKingPositionForplayerThatMoves() {
    //     if (colorToMove.getColor() == 'w') {
    //         this.kingLocationplayerThatMoves = this.whiteKing;
    //     }
    //     else {
    //         this.kingLocationplayerThatMoves = this.blackKing;
    //     }
    // }

//Er vell også mulig å lage en form for Hashmap f.eks.

//Notes castling

//Possible to make one common if - with something like this, migth become a bit messy 

// int[] castlingKingEndPosition = (colorToMove.getColor() == 'w')    ? 
// (checkForSameCoordinates(pieceWillMoveTo, this.whiteKingMoveCastlingLeft))   ? this.whiteKingMoveCastlingLeft : this.whiteKingMoveCastlingRight 
// : (checkForSameCoordinates(pieceWillMoveTo, this.blackKingMoveCastlingLeft)) ? this.blackKingMoveCastlingLeft: this.blackKingMoveCastlingRight;

// int[] castlingKingSkippedPosition = (colorToMove.getColor() == 'w')   ? 
// (checkForSameCoordinates(pieceWillMoveTo, this.whiteCastlingSkippedTileLeft))   ? this.whiteCastlingSkippedTileLeft : this.whiteCastlingSkippedTileRight 
// : (checkForSameCoordinates(pieceWillMoveTo, this.blackCastlingSkippedTileLeft)) ? this.blackCastlingSkippedTileLeft : this.blackCastlingSkippedTileRight;

// if (checkForSameCoordinates(pieceWillMoveTo, castlingKingEndPosition)) {
//     if (checkForSameCoordinates(oneMoveForOppositePiece, this.originalKingLocation)) {
//         movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
//     }
//     else if (checkForSameCoordinates(oneMoveForOppositePiece, castlingKingSkippedPosition)) {
//         movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
//     }

// }


    public static void main(String[] args) {
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


