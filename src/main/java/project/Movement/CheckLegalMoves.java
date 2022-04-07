package project.Movement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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

//Trenger vel ikke å implementere Serializable her?
public class CheckLegalMoves implements Serializable {
    
    //Mulig å bare ha 2?
    private MovementPatterns whiteMovement;
    private MovementPatterns blackMovement;
    private MovementPatterns colorToMove;
    private MovementPatterns colorNotMoving;
    

    private int gameStatus = Consts.GAME_NOT_OVER;

    //mulig å droppe whiteking og blackking?
    private int[] whiteKing;
    private int[] blackKing;
    private int[] originalKingLocation;
    private int[] kingLocationPlayerToMove;
    
    //se hvordan dette skal gjøres
    //Tur
    int moveNumber = 0; 

    public CheckLegalMoves() {
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
          
    }
    //endret så den returnerer 
    //Generates all the possible moves for black or white when check is not considered 
    //Legge inn movenumber her?
    private HashMap<int[], ArrayList<int[]>> populateAllMoves(MovementPatterns movementPattern, Tile[][] boardTiles) {

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

    private HashMap<int[], ArrayList<int[]>> eliminateChecks(Tile[][] currentGamePositionTiles) {
        //MovementPatterns colorToMove;
        //MovementPatterns colorNotMoving;

        setKingPositions(currentGamePositionTiles);
        setPlayerToMove();
        setOriginalKingLocationForPlayerToMove();
        setKingPositionForPlayerToMove();

        // //lagre som felt isteden? - og bruke metode?
        // if (moveNumber % 2 == 0) {
        //     colorToMove = whiteMovement;
        //     colorNotMoving = blackMovement;
        // } 
        // else {
        //     colorToMove = blackMovement;
        //     colorNotMoving = whiteMovement;
        // }

        //samme her?
        // int[] originalKingLocation;

        // if (colorToMove.getColor() == 'w') {
        //     originalKingLocation = new int[]{0, 4};
        // }
        // else {
        //     originalKingLocation = new int[]{7, 4};
        // }

        HashMap<int[], ArrayList<int[]>> legalMoves = populateAllMoves(colorToMove, currentGamePositionTiles);
        
        for (int[] chosenPiecePlayerToMove : legalMoves.keySet()) {

            ArrayList<int[]> allMovesForAPiece = legalMoves.get(chosenPiecePlayerToMove);
            //Moves that break check that should be removed 
            //Endre til arraylist?
            List<int[]> movesToBeRemoved = new ArrayList<int[]>();

            //Count for what element that should be removed
            int indexForMoveToRemove = 0;

            for (int[] oneSpecificMove : allMovesForAPiece) {
 
                // Name e.g. piecePositonNow
                int piecePositionNowRow = chosenPiecePlayerToMove[0];
                int piecePositionNowCol = chosenPiecePlayerToMove[1];

                // Name e.g. pieceWillMoveTo
                int movePieceToRow = oneSpecificMove[0];
                int movePieceToCol = oneSpecificMove[1];
                
                Piece pieceToMove = currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].getPiece();

                //sjekk for kongen, skal oppdatere område hvis han flytter 

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

                // pieceAtThespotWeAreMovingTo
                Piece placeBackPiece = currentGamePositionTiles[movePieceToRow][movePieceToCol].getPiece();

                //Se på dette iforhold til okupering osv - i piece klassn
                currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].removePiece();
                //tester om dette funker 
                //currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].setOccupied(false);

                currentGamePositionTiles[movePieceToRow][movePieceToCol].setPiece(pieceToMove);
                //Tester om dette funker fint 
                //currentGamePositionTiles[movePieceToRow][movePieceToCol].isOccupied();

                //Må vell gjøres her hvis kongen blir flyttet 
                setKingPositionForPlayerToMove();
                // int[] kingLocation;

                //dette trengs ikke gjøres hver gang - flytt 
            
                // if (colorToMove.getColor() == 'w') {
                //     kingLocation = this.whiteKing;
                // }
                // else {
                //     kingLocation = this.blackKing;
                // }

                HashMap<int[], ArrayList<int[]>> legalMovesOppositeColor = populateAllMoves(colorNotMoving, currentGamePositionTiles);

                Collection<ArrayList<int[]>> allOppositeMoves = legalMovesOppositeColor.values();

                for (ArrayList<int[]> oppositeColorPieceMoves: allOppositeMoves) {
                    for (int[] oneMoveForOppositePiece : oppositeColorPieceMoves) {
                        if (oneMoveForOppositePiece[0] == kingLocationPlayerToMove[0] && oneMoveForOppositePiece[1] == kingLocationPlayerToMove[1]) {

                            //tror ikke denne er nødvendig 
                            //ArrayList<int[]> pieceMoves = legalMoves.get(specificPiece);
                            
                            for (int[] oneOfAllMoves : allMovesForAPiece) {
                                if (oneOfAllMoves[0] == oneSpecificMove[0] && oneOfAllMoves[1] == oneSpecificMove[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                }

                            }
                
                        }

                        //håndtering av lovlig rokade

                        else if (pieceToMove instanceof King && !pieceToMove.getHasMoved()) {
                            if (colorToMove.getColor() == 'w') {
                                if (movePieceToRow == 0 && movePieceToCol == 6) {
                                    if (oneMoveForOppositePiece[0] == originalKingLocation[0] && oneMoveForOppositePiece[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (oneMoveForOppositePiece[0] == 0 && oneMoveForOppositePiece[1] == 5) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (currentGamePositionTiles[1][6].isOccupied()) { //håndtering hvis det er bonde som truer et felt 
                                        if (currentGamePositionTiles[1][6].getPiece().getColor() == 'b' && currentGamePositionTiles[1][6].getPiece() instanceof Pawn) {
                                            movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                        }
                                    }

                                }
                                else if (movePieceToRow == 0 && movePieceToCol == 2) {         
                                    if (oneMoveForOppositePiece[0] == originalKingLocation[0] && oneMoveForOppositePiece[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (oneMoveForOppositePiece[0] == 0 && oneMoveForOppositePiece[1] == 3) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (currentGamePositionTiles[1][2].isOccupied()) { //håndtering hvis det er bonde som truer et felt 
                                        if (currentGamePositionTiles[1][2].getPiece().getColor() == 'b' && currentGamePositionTiles[1][2].getPiece() instanceof Pawn) {
                                            movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                        }
                                    }
                                }
                            }
                            else if (colorToMove.getColor() == 'b') {
                                if (movePieceToRow == 7 && movePieceToCol == 6) {
                                    if (oneMoveForOppositePiece[0] == originalKingLocation[0] && oneMoveForOppositePiece[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (oneMoveForOppositePiece[0] == 7 && oneMoveForOppositePiece[1] == 5) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (currentGamePositionTiles[6][6].isOccupied()) { //håndtering hvis det er bonde som truer et felt 
                                        if (currentGamePositionTiles[6][6].getPiece().getColor() == 'w' && currentGamePositionTiles[6][6].getPiece() instanceof Pawn) {
                                            movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                        }
                                    }

                                }
                                else if (movePieceToRow == 7 && movePieceToCol == 2) {
                                    if (oneMoveForOppositePiece[0] == originalKingLocation[0] && oneMoveForOppositePiece[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (oneMoveForOppositePiece[0] == 7 && oneMoveForOppositePiece[1] == 3) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                    }
                                    else if (currentGamePositionTiles[6][2].isOccupied()) { //håndtering hvis det er bonde som truer et felt 
                                        if (currentGamePositionTiles[6][2].getPiece().getColor() == 'w' && currentGamePositionTiles[6][2].getPiece() instanceof Pawn) {
                                            movesToBeRemoved.add(allMovesForAPiece.get(indexForMoveToRemove));
                                        }
                                    }
                                }
                            }
                            
                        }
                                  
                    }
                    
                }
                

                //flytt brikken tilbake, den som ble flyttet på - går vell å lage metode
                Piece pieceToMoveBack = currentGamePositionTiles[movePieceToRow][movePieceToCol].getPiece();

                currentGamePositionTiles[movePieceToRow][movePieceToCol].removePiece();
                currentGamePositionTiles[movePieceToRow][movePieceToCol].setPiece(placeBackPiece);
                if (currentGamePositionTiles[movePieceToRow][movePieceToCol].getPiece() == null) {
                    currentGamePositionTiles[movePieceToRow][movePieceToCol].setOccupied(false);
                    //overkill etter fix? - se på - setOccupied
                }
                
                
                currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].setPiece(pieceToMoveBack);
                //overkill? - se på piece 
                //currentGamePositionTiles[piecePositionNowRow][piecePositionNowCol].isOccupied();

                //setter kongen tilbake
                if (kingMovedWhite) {
                    this.whiteKing = new int[]{piecePositionNowRow, piecePositionNowCol};
                }
                if (kingMovedBlacked) {
                     this.blackKing = new int[]{piecePositionNowRow, piecePositionNowCol};
                }
                
                indexForMoveToRemove ++;
            }

            allMovesForAPiece.removeAll(movesToBeRemoved);
        }

        return legalMoves;
    }

    public HashMap<int[], ArrayList<int[]>> CheckforCheckMateAndPat (Tile[][] currentGamePositionTiles) {

        // MovementPatterns colorToMove;
        // MovementPatterns colorNotMoving;

        HashMap<int[], ArrayList<int[]>> legalMoves = eliminateChecks(currentGamePositionTiles);
        
        boolean kingCanBetaken = false;
        boolean notGameOver = false;

        setKingPositionForPlayerToMove();

        //metode for dette?
        // if (moveNumber % 2 == 0) {
        //     colorToMove = whiteMovement;
        //     colorNotMoving = blackMovement;
        // } 
        // else {
        //     colorToMove = blackMovement;
        //     colorNotMoving = whiteMovement;
        // }

        // //kan lage metode på dette
        // int[] kingLocation;
                
        // if (colorToMove.getColor() == 'w') {
        //     kingLocation = this.whiteKing;
        // }
        // else {
        //     kingLocation = this.blackKing;
        // }
    
        for (int[] colorToMovePiece : legalMoves.keySet()) {
            if (notGameOver) {
                break;
            }

            if (legalMoves.get(colorToMovePiece).size() != 0) {
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
                    if (oneOppositeMove[0] == kingLocationPlayerToMove[0] && oneOppositeMove[1] == kingLocationPlayerToMove[1]) {

                        kingCanBetaken = true;
                        break;
                    }
                }
            } 
        }
        //byttet om her 
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
            this.originalKingLocation = new int[]{0, 4};
        }
        else {
            this.originalKingLocation = new int[]{7, 4};
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


