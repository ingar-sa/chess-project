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

public class TileCheckLegalMoves implements Serializable {
    
    //private Tile[][]             currentGamePositionTiles;
    private TileMovementPatterns whiteMovement;
    private TileMovementPatterns blackMovement;

    private int gameStatus = Consts.GAME_NOT_OVER;

    //må vell egentlig hentes fra hovedbrettet
    private int[] whiteKing;
    private int[] blackKing;
    
    //se hvordan dette skal gjøres
    //Tur
    int moveNumber = 0; 


    //Sjekk om vi trenger chessboard
    public TileCheckLegalMoves() {
        //this.currentGamePositionTiles = boardTiles;
        
        this.whiteMovement = new TileMovementPatterns('w', this);
        this.blackMovement = new TileMovementPatterns('b', this);
          
    }
    //endret så den returnerer 
    private HashMap<Tile, ArrayList<int[]>> populateAllMoves(TileMovementPatterns movementPattern, Tile[][] currentGamePositionTiles) {
        //movementPattern.setBoardTiles(currentGamePositionTiles);

        HashMap<Tile, ArrayList<int[]>> legalMoves = new HashMap<Tile, ArrayList<int[]>>();
        
        for (Tile[] row : currentGamePositionTiles) {
            for (Tile tile : row) {
                if (tile.isOccupied() 
                    && tile.getPiece().getColor() == movementPattern.getColor()) {

                    Tile keyPieceConnectedToValueMoves = currentGamePositionTiles[tile.getRow()][tile.getCol()];
                    ArrayList<int[]> allMoves = movementPattern.moveHandler(tile, currentGamePositionTiles);

                    legalMoves.put(keyPieceConnectedToValueMoves, allMoves);
                    
                }
            }
        }
        return legalMoves;
    }

    private HashMap<Tile, ArrayList<int[]>> eliminateChecks(Tile[][] currentGamePositionTiles) {
        TileMovementPatterns colorToMove;
        TileMovementPatterns colorNotMoving;

        setKingPosition(currentGamePositionTiles);

        if (moveNumber % 2 == 0) {
            colorToMove = whiteMovement;
            colorNotMoving = blackMovement;
        } 
        else {
            colorToMove = blackMovement;
            colorNotMoving = whiteMovement;
        }

        int[] originalKingLocation;

        if (colorToMove.getColor() == 'w') {
            originalKingLocation = new int[]{0, 4};
        }
        else {
            originalKingLocation = new int[]{7, 4};
        }

        HashMap<Tile, ArrayList<int[]>> legalMoves = populateAllMoves(colorToMove, currentGamePositionTiles);
        

        for (Tile keyPiece : legalMoves.keySet()) {

           ArrayList<int[]> allMovesForAPiece = legalMoves.get(keyPiece);
           List<int[]> movesToBeRemoved = new ArrayList<int[]>();

           //Count for what element that should be removed
           int count = 0;

            for (int[] coordinatesForAMove : allMovesForAPiece) {
                //Chessboard shadowChessboardObject = new Chessboard();

                //Tile[][] shadowBoardTiles = this.currentGamePositionTiles;
 
                // Name e.g. piecePositonNow
                int rowPieceIsOn = keyPiece.getRow();
                int colPieceIsOn = keyPiece.getCol();

                // Name e.g. pieceWillMoveTo
                int rowMoveCoordinate = coordinatesForAMove[0];
                int colMoveCoordinate = coordinatesForAMove[1];
                
                Piece pieceToMove = currentGamePositionTiles[rowPieceIsOn][colPieceIsOn].getPiece();

                //sjekk for kongen, skal oppdatere område hvis han flytter 

                boolean kingMovedWhite = false;
                boolean kingMovedBlacked = false;

                if (pieceToMove instanceof King && pieceToMove.getColor() == 'w') {
                    this.whiteKing = new int[]{rowMoveCoordinate, colMoveCoordinate};
                    kingMovedWhite = true;
                }
                
                 if (pieceToMove instanceof King && pieceToMove.getColor() == 'b') {
                    this.blackKing = new int[]{rowMoveCoordinate, colMoveCoordinate};
                    kingMovedBlacked = true;
                }

                // pieceAtThespotWeAreMovingTo
                Piece placeBackPiece = currentGamePositionTiles[rowMoveCoordinate][colMoveCoordinate].getPiece();

                //Se på dette iforhold til okupering osv - i piece klassen
                //Fjerner brikken
                currentGamePositionTiles[rowPieceIsOn][colPieceIsOn].removePiece();
                currentGamePositionTiles[rowPieceIsOn][colPieceIsOn].setOccupied(false);
                //Setter brikken på nytt felt
                currentGamePositionTiles[rowMoveCoordinate][colMoveCoordinate].setPiece(pieceToMove);
                currentGamePositionTiles[rowMoveCoordinate][colMoveCoordinate].isOccupied();

                int[] kingLocation;

                //dette trengs ikke gjøres hver gang - flytt 
                
                if (colorToMove.getColor() == 'w') {
                    kingLocation = this.whiteKing;
                }
                else {
                    kingLocation = this.blackKing;
                }

                HashMap<Tile, ArrayList<int[]>> legalMovesOpposite = populateAllMoves(colorNotMoving, currentGamePositionTiles);

                Collection<ArrayList<int[]>> allOppositeMoves = legalMovesOpposite.values();

                for (ArrayList<int[]> oppositeCorrArray: allOppositeMoves) {
                    for (int[] oppositeCorr : oppositeCorrArray) {
                        if (oppositeCorr[0] == kingLocation[0] && oppositeCorr[1] == kingLocation[1]) {

                            ArrayList<int[]> pieceMoves = legalMoves.get(keyPiece);
                            
                            for (int[] chosenCorr : pieceMoves) {
                                if (chosenCorr[0] == rowMoveCoordinate && chosenCorr[1] == colMoveCoordinate) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }

                            }
                
                        }

                        //håndtering av lovlig rokade

                        else if (pieceToMove instanceof King && !pieceToMove.getHasMoved()) {
                            if (colorToMove.getColor() == 'w') {
                                if (rowMoveCoordinate == 0 && colMoveCoordinate == 6) {
                                    if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                    else if (oppositeCorr[0] == 0 && oppositeCorr[1] == 5) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                }
                                else if (rowMoveCoordinate == 0 && colMoveCoordinate == 2) {
                                    if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                    else if (oppositeCorr[0] == 0 && oppositeCorr[1] == 3) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                }
                            }
                            else if (colorToMove.getColor() == 'b') {
                                if (rowMoveCoordinate == 7 && colMoveCoordinate == 6) {
                                    if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                    else if (oppositeCorr[0] == 7 && oppositeCorr[1] == 5) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                }
                                else if (rowMoveCoordinate == 7 && colMoveCoordinate== 2) {
                                    if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                    else if (oppositeCorr[0] == 7 && oppositeCorr[1] == 3) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                }
                            }
                            
                        }
                                  
                    }
                    
                }
                

                //flytt brikken tilbake, den som ble flyttet på - går vell å lage metode
                Piece pieceToMoveBack = currentGamePositionTiles[rowMoveCoordinate][colMoveCoordinate].getPiece();


                //Tror ikke dette trengs til noe egentlig
                // if (pieceToMoveBack instanceof Pawn && Math.abs(yValue - yKey) == 2) {
                //     ((Pawn)pieceToMoveBack).setMovedTwoLastTurn(false);
                // }

                currentGamePositionTiles[rowMoveCoordinate][colMoveCoordinate].removePiece();
                currentGamePositionTiles[rowMoveCoordinate][colMoveCoordinate].setPiece(placeBackPiece);

                //Viktig! fordi setPiece setter true, kanskje endre?
                if (currentGamePositionTiles[rowMoveCoordinate][colMoveCoordinate].getPiece() == null) {
                    currentGamePositionTiles[rowMoveCoordinate][colMoveCoordinate].setOccupied(false);
                    //overkill etter fix? - se på - setOccupied
                }
                
                
                currentGamePositionTiles[rowPieceIsOn][colPieceIsOn].setPiece(pieceToMoveBack);
                //overkill? - se på piece 
                //currentGamePositionTiles[rowPieceIsOn][colPieceIsOn].isOccupied();

                //setter kongen tilbake
                if (kingMovedWhite) {
                    this.whiteKing = new int[]{rowPieceIsOn, colPieceIsOn};
                }
                if (kingMovedBlacked) {
                     this.blackKing = new int[]{rowPieceIsOn, colPieceIsOn};
                }
                
                count ++;
            }

            allMovesForAPiece.removeAll(movesToBeRemoved);
        }

        return legalMoves;
    }

    public HashMap<Tile, ArrayList<int[]>> CheckforCheckMateAndPat (Tile[][] currentGamePositionTiles) {

        TileMovementPatterns colorToMove;
        TileMovementPatterns colorNotMoving;

        HashMap <Tile, ArrayList<int[]>> legalMoves = eliminateChecks(currentGamePositionTiles);
        
        boolean kingCanBetaken = false;
        boolean notGameOver = false;

        //metode for dette?
        if (moveNumber % 2 == 0) {
            colorToMove = whiteMovement;
            colorNotMoving = blackMovement;
        } 
        else {
            colorToMove = blackMovement;
            colorNotMoving = whiteMovement;
        }

        //kan lage metode på dette
        int[] kingLocation;
                
        if (colorToMove.getColor() == 'w') {
            kingLocation = this.whiteKing;
        }
        else {
            kingLocation = this.blackKing;
        }
    
        for (Tile keyPiece : legalMoves.keySet()) {
            if (notGameOver) {
                break;
            }

            if (legalMoves.get(keyPiece).size() != 0) {
                notGameOver = true;
            }
        }

        if (!notGameOver) {

            HashMap<Tile, ArrayList<int[]>> canTheKingBeTakenNoMoves = populateAllMoves(colorNotMoving, currentGamePositionTiles);
            Collection<ArrayList<int[]>> allPossibleMovesThatCouldTakeTheKing = canTheKingBeTakenNoMoves.values();

            for (ArrayList<int[]> oppositeCorrArray: allPossibleMovesThatCouldTakeTheKing) {
                
                if (kingCanBetaken) {
                    break;
                }

                for (int[] oppositeCorr : oppositeCorrArray) {
                    if (oppositeCorr[0] == kingLocation[0] && oppositeCorr[1] == kingLocation[1]) {

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

    private void setKingPosition(Tile[][] currentGamePositionTiles) {

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

        TileCheckLegalMoves checklegalmoves = new TileCheckLegalMoves(tiles);


        //checklegalmoves.CheckforCheckMateAndPat();
        
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }
}


