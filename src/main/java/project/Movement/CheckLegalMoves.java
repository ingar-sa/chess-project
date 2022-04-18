package project.Movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import project.Consts;
import project.Board.Chessboard;
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
    private MovementPatterns colorToMove;
    private MovementPatterns colorNotMoving;

    private int[] whiteKing;
    private int[] blackKing;
    private int[] originalKingLocationPlayerToMove;
    private int[] kingLocationPlayerToMove;

    //Important tiles for when white is Castling  
    private final int[] whiteKingMoveCastlingRigth = new int[]{0, 6};
    private final int[] whiteKingMoveCastlingLeft = new int[]{0, 2};
    private final int[] whiteCastlingSkippedTileRigth = new int[]{0, 5};
    private final int[] whiteCastlingSkippedTileLeft = new int[]{0, 3};

    //Important tiles for when black is Castling  
    private final int[] blackKingMoveCastlingRigth = new int[]{7, 6};
    private final int[] blackKingMoveCastlingLeft = new int[]{7, 2};
    private final int[] blackCastlingSkippedTileRigth = new int[]{7, 5};
    private final int[] blackCastlingSkippedTileLeft = new int[]{7, 3};

    //TODO: legge til at man ikke kan kalle på metodene hvis spillet er over.

    //Turn number
    int moveNumber = 0; 

    public CheckLegalMoves() {
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
          
    }

    //Generates all the possible moves for black or white when check is not considered 
    public HashMap<int[], ArrayList<int[]>> populateAllMoves(MovementPatterns movementPattern, Tile[][] boardTiles) {

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

    private HashMap<int[], ArrayList<int[]>> eliminateChecks(Tile[][] currentGamePositionTiles) {

        setKingPositions(currentGamePositionTiles);
        setPlayerToMove();
        setOriginalKingLocationForPlayerToMove();
        setKingPositionForPlayerToMove();

        HashMap<int[], ArrayList<int[]>> legalMoves = populateAllMoves(colorToMove, currentGamePositionTiles);
        
        for (int[] chosenPiecePlayerToMove : legalMoves.keySet()) {

            ArrayList<int[]> allMovesForAPiece = legalMoves.get(chosenPiecePlayerToMove);
            ArrayList<int[]> movesToBeRemoved = new ArrayList<int[]>();

            //Count for what move that should be removed from piece moves, move that is illegal because of CHECK
            int indexForMoveToRemove = 0;

            for (int[] pieceWillMoveTo : allMovesForAPiece) {
 
                int piecePositionNowRow = chosenPiecePlayerToMove[0];
                int piecePositionNowCol = chosenPiecePlayerToMove[1];

                int movePieceToRow = pieceWillMoveTo[0];
                int movePieceToCol = pieceWillMoveTo[1];
                
                Piece pieceToMove = currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].getPiece();
                Piece pieceAtThespotWeAreMovingTo = currentGamePositionTiles[movePieceToRow][movePieceToCol].getPiece();

                //Checks if king moves, if yes, fields have to be updated
                boolean kingMovedWhite = false;
                boolean kingMovedBlacked = false;

                if (pieceToMove instanceof King && pieceToMove.getColor() == 'w') {
                    this.whiteKing = new int[]{movePieceToRow, movePieceToCol};
                    kingMovedWhite = true;
                }
                
                if (pieceToMove instanceof King && pieceToMove.getColor() == 'b') {
                    this.blackKing = new int[]{movePieceToRow, movePieceToCol};
                    kingMovedBlacked = true;
                }

                //Moves the piece 
                currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].removePiece();
                currentGamePositionTiles[movePieceToRow][movePieceToCol].setPiece(pieceToMove);
                
                //Updates the king position 
                setKingPositionForPlayerToMove();

                HashMap<int[], ArrayList<int[]>> legalMovesOppositeColor = populateAllMoves(colorNotMoving, currentGamePositionTiles);

                Collection<ArrayList<int[]>> allOppositeMoves = legalMovesOppositeColor.values();

                for (ArrayList<int[]> oppositeColorPieceMoves: allOppositeMoves) {
                    for (int[] oneMoveForOppositePiece : oppositeColorPieceMoves) {
                        
                        if (checkForSameCoordinates(kingLocationPlayerToMove, oneMoveForOppositePiece)) {

                            for (int[] oneOfAllMoves : allMovesForAPiece) {
                                if (checkForSameCoordinates(oneOfAllMoves, pieceWillMoveTo)) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                }

                            }
                
                        }
                        
                        //Handling of legal castling 
                        else if (pieceToMove instanceof King && !pieceToMove.getHasMoved()) {
                            if (colorToMove.getColor() == 'w') {

                                //Checks if rigth castling for white is legal 
                                if (checkForSameCoordinates(pieceWillMoveTo, this.whiteKingMoveCastlingRigth)) {
                                    if (checkForSameCoordinates(oneMoveForOppositePiece, this.originalKingLocationPlayerToMove)) { //examines if the king is already in check
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (checkForSameCoordinates(oneMoveForOppositePiece, this.whiteCastlingSkippedTileRigth)) { //examines if the tile that the king skips/moves over is check
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (currentGamePositionTiles[1][6].isOccupied()) { //examines if there is a pawn that attacks the skipped tile
                                        if (currentGamePositionTiles[1][6].getPiece().getColor() == 'b' && currentGamePositionTiles[1][6].getPiece() instanceof Pawn) {
                                            movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                        }
                                    }

                                }
                                //Checks if left castling for white is legal 
                                else if (checkForSameCoordinates(pieceWillMoveTo, this.whiteKingMoveCastlingLeft)) {         
                                    if (checkForSameCoordinates(oneMoveForOppositePiece, this.originalKingLocationPlayerToMove)) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (checkForSameCoordinates(oneMoveForOppositePiece, this.whiteCastlingSkippedTileLeft)) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (currentGamePositionTiles[1][2].isOccupied()) { 
                                        if (currentGamePositionTiles[1][2].getPiece().getColor() == 'b' && currentGamePositionTiles[1][2].getPiece() instanceof Pawn) {
                                            movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                        }
                                    }
                                }
                            }
                            else if (colorToMove.getColor() == 'b') {
                                
                                //Checks if rigth castling for black is legal 
                                if (checkForSameCoordinates(pieceWillMoveTo, this.blackKingMoveCastlingRigth)) {
                                    if (checkForSameCoordinates(oneMoveForOppositePiece, this.originalKingLocationPlayerToMove)) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (checkForSameCoordinates(oneMoveForOppositePiece, this.blackCastlingSkippedTileRigth)) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (currentGamePositionTiles[6][6].isOccupied()) { 
                                        if (currentGamePositionTiles[6][6].getPiece().getColor() == 'w' && currentGamePositionTiles[6][6].getPiece() instanceof Pawn) {
                                            movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                        }
                                    }

                                }
                                //Checks if left castling for black is legal 
                                else if (checkForSameCoordinates(pieceWillMoveTo, this.blackKingMoveCastlingLeft)) {
                                    if (checkForSameCoordinates(oneMoveForOppositePiece, this.originalKingLocationPlayerToMove)) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (checkForSameCoordinates(oneMoveForOppositePiece, this.blackCastlingSkippedTileLeft)) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (currentGamePositionTiles[6][2].isOccupied()) { 
                                        if (currentGamePositionTiles[6][2].getPiece().getColor() == 'w' && currentGamePositionTiles[6][2].getPiece() instanceof Pawn) {
                                            movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                        }
                                    }
                                }
                            }
                            
                        }
                                  
                    }
                    
                }
                

                //Moves the piece we moved back 
                Piece pieceToMoveBack = currentGamePositionTiles[movePieceToRow][movePieceToCol].getPiece();
                currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].setPiece(pieceToMoveBack);

                //Replaces the piece that was at the spot we moved to 
                currentGamePositionTiles[movePieceToRow][movePieceToCol].removePiece();
                currentGamePositionTiles[movePieceToRow][movePieceToCol].setPiece(pieceAtThespotWeAreMovingTo);

                //Since setPiece also makes Occupied = True, this has to handeled
                if (currentGamePositionTiles[movePieceToRow][movePieceToCol].getPiece() == null) {
                    currentGamePositionTiles[movePieceToRow][movePieceToCol].setOccupied(false);
                }
                
                currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].setPiece(pieceToMoveBack);

                //Reset King field if king moved 
                if (kingMovedWhite) {
                    this.whiteKing = new int[]{piecePositionNowRow, piecePositionNowCol};
                }
                if (kingMovedBlacked) {
                     this.blackKing = new int[]{piecePositionNowRow, piecePositionNowCol};
                }
                
                indexForMoveToRemove ++;
            }
            //Removes all the illegal moves for a piece that breaks check
            allMovesForAPiece.removeAll(movesToBeRemoved);
        }

        return legalMoves;
    }

    private boolean checkForSameCoordinates(int[] coordinateOne, int[] coordinateTwo) {
        if (coordinateOne[0] == coordinateTwo[0] && coordinateOne[1] == coordinateTwo[1]) {
            return true;
        }
        return false;
    }


    //TODO: Skal vel fjernes herfra  
    private void validationOfKings(Tile[][] currentGamePositionTiles) {

        int rowCount = 0;
        int totalTileCount = 0;
        int blackKingCount = 0;
        int whitKingCount = 0;

        for (Tile[] row : currentGamePositionTiles) {
            rowCount += 1;
            totalTileCount += row.length; 
            for (Tile tile : row) {
                if (tile.getPiece() instanceof King) {
                    if (tile.getPiece().getColor() == 'b') {
                        blackKingCount += 1;
                    }
                    else if (tile.getPiece().getColor() == 'w') {
                        whitKingCount += 1;
                    }
                }
                if (rowCount == 0 || rowCount == 7) {
                    if (tile.getPiece() instanceof Pawn) {
                        throw new IllegalArgumentException("There are pawns on row 1 or 8 this is not allowed!");
                    }
                }
            }
        }

        if (   rowCount       != 8
            || totalTileCount != 64 
            || blackKingCount != 1 
            || whitKingCount  != 1 ){
                throw new IllegalArgumentException("The board is not the correct size or there are too many kings!");
        }

        // for (int i = 0; i < 8; i++) {
        //     if (currentGamePositionTiles[0][i] instanceof Pawn &&) {
        //         i
        //     }
        // }
    }

    HashMap<int[], ArrayList<int[]>> CheckforCheckMateAndPat (Tile[][] currentGamePositionTiles) {

        //TODO: se mer på dette, kongene kan ikke være inntill hverandre kanskje, sjekke at max en spiller er i sjakk, sjekke at pawns ikke står feil, hvis kongen ikke står på start feltet må den ha flyttet på seg, samme med bonde og tårn!?
        // validationOfKings(currentGamePositionTiles);

        HashMap<int[], ArrayList<int[]>> legalMoves = eliminateChecks(currentGamePositionTiles);
        
        boolean kingCanBetaken = false;
        boolean notGameOver = false;

        setKingPositionForPlayerToMove();
    
        for (int[] piecePlayerToMove : legalMoves.keySet()) {
            if (notGameOver) {
                break;
            }

            if (legalMoves.get(piecePlayerToMove).size() != 0) {
                notGameOver = true;
            }
        }

        if (!notGameOver) {

            HashMap<int[], ArrayList<int[]>> allPiecesAndMovesOppositeColor = populateAllMoves(colorNotMoving, currentGamePositionTiles);
            Collection<ArrayList<int[]>> allPossibleMovesThatCouldTakeTheKing = allPiecesAndMovesOppositeColor.values();

            for (ArrayList<int[]> movesOppositePiece: allPossibleMovesThatCouldTakeTheKing) {
                
                if (kingCanBetaken) {
                    break;
                }

                for (int[] oneOppositeMove : movesOppositePiece) {
                    if (checkForSameCoordinates(oneOppositeMove, kingLocationPlayerToMove)) {

                        kingCanBetaken = true;
                        break;
                    }
                }
            } 
        }
        
        if (kingCanBetaken && !notGameOver) {
            this.gameStatus = Consts.CHECKMATE;
        } 
        else if (!kingCanBetaken && !notGameOver) {
            this.gameStatus = Consts.PAT;
        }

        return legalMoves;
    }

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
        if (moveNumber >= 0) {
            this.moveNumber = moveNumber;
        }
        else {
            throw new IllegalArgumentException("Illegal move number!");
        }
    }

    private void setKingPositions(Tile[][] currentGamePositionTiles) {

        for (Tile[] tileRow : currentGamePositionTiles) {
            for (Tile tile: tileRow) {
                if (tile.getPiece() instanceof King && ((King)tile.getPiece()).getColor() == 'w') {
                    this.whiteKing = new int[]{tile.getRow(), tile.getCol()};
                }
                else if (tile.getPiece() instanceof King && ((King)tile.getPiece()).getColor() == 'b') {
                    this.blackKing = new int[]{tile.getRow(), tile.getCol()};
                }
            }
        }
    }

    private void setPlayerToMove () {
        if (moveNumber % 2 == 0) {
            this.colorToMove = whiteMovement;
            this.colorNotMoving = blackMovement;
        } 
        else {
            this.colorToMove = blackMovement;
            this.colorNotMoving = whiteMovement;
        }
    }

    private void setOriginalKingLocationForPlayerToMove () {
        if (colorToMove.getColor() == 'w') {
            this.originalKingLocationPlayerToMove = new int[]{0, 4};
        }
        else {
            this.originalKingLocationPlayerToMove = new int[]{7, 4};
        }
    }

    private void setKingPositionForPlayerToMove() {
        if (colorToMove.getColor() == 'w') {
            this.kingLocationPlayerToMove = this.whiteKing;
        }
        else {
            this.kingLocationPlayerToMove = this.blackKing;
        }
    }

//Er vell også mulig å lage en form for Hashmap f.eks.

//Notes castling

//Possible to make one common if - with something like this, migth become a bit messy 

// int[] castlingKingEndPosition = (colorToMove.getColor() == 'w')    ? 
// (checkForSameCoordinates(pieceWillMoveTo, this.whiteKingMoveCastlingLeft))   ? this.whiteKingMoveCastlingLeft : this.whiteKingMoveCastlingRigth 
// : (checkForSameCoordinates(pieceWillMoveTo, this.blackKingMoveCastlingLeft)) ? this.blackKingMoveCastlingLeft: this.blackKingMoveCastlingRigth;

// int[] castlingKingSkippedPosition = (colorToMove.getColor() == 'w')   ? 
// (checkForSameCoordinates(pieceWillMoveTo, this.whiteCastlingSkippedTileLeft))   ? this.whiteCastlingSkippedTileLeft : this.whiteCastlingSkippedTileRigth 
// : (checkForSameCoordinates(pieceWillMoveTo, this.blackCastlingSkippedTileLeft)) ? this.blackCastlingSkippedTileLeft : this.blackCastlingSkippedTileRigth;

// if (checkForSameCoordinates(pieceWillMoveTo, castlingKingEndPosition)) {
//     if (checkForSameCoordinates(oneMoveForOppositePiece, this.originalKingLocation)) {
//         movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
//     }
//     else if (checkForSameCoordinates(oneMoveForOppositePiece, castlingKingSkippedPosition)) {
//         movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
//     }

// }


    public static void main(String[] args) {
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
    }
}


