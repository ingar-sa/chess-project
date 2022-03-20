package project.Movement;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import project.Game;
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
    
    private Tile[][] currentGamePositionTiles;
    private MovementPatterns whiteMovement;
    private MovementPatterns blackMovement;

    private int gameStatus = 0;

    //må vell egentlig hentes fra hovedbrettet
    private int[] whiteKing;
    private int[] blackKing;
    
    //se hvordan dette skal gjøres
    //Tur
    int moveNumber = 0; 


    //Sjekk om vi trenger chessboard
    public CheckLegalMoves(Tile[][] boardTiles) {
        this.currentGamePositionTiles = boardTiles;
        
        this.whiteMovement = new MovementPatterns('w', this);
        this.blackMovement = new MovementPatterns('b', this);
          
    }
    //endret så den returnerer 
    private HashMap<int[], ArrayList<int[]>> populateAllMoves(MovementPatterns movementPattern, Tile[][] boardTiles) {
        movementPattern.setBoardTiles(boardTiles);

        HashMap<int[], ArrayList<int[]>> legalMoves = new HashMap<int[], ArrayList<int[]>>();
        
        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {
                if (tile.isOccupied() 
                    && tile.getPiece().getColor() == movementPattern.getColor()) {

                    int[] key = new int[] {tile.getRow(), tile.getCol()};
                    ArrayList<int[]> allMoves = movementPattern.moveHandler(tile);

                    legalMoves.put(key, allMoves);
                    
                }
            }
        }
        return legalMoves;
    }

    private HashMap<int[], ArrayList<int[]>> eliminateChecks() {
        MovementPatterns colorToMove;
        MovementPatterns colorNotMoving;

        setKingPosition();

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

        HashMap<int[], ArrayList<int[]>> legalMoves = populateAllMoves(colorToMove, currentGamePositionTiles);
        

        for (int[] key : legalMoves.keySet()) {

           ArrayList<int[]> allMovesForAPiece = legalMoves.get(key);
           List<int[]> movesToBeRemoved = new ArrayList<int[]>();

           //Count for what element that should be removed
           int count = 0;

            for (int[] coordinates : allMovesForAPiece) {
                //Chessboard shadowChessboardObject = new Chessboard();

                //Tile[][] shadowBoardTiles = this.currentGamePositionTiles;
 
                // Name e.g. piecePositonNow
                int xKey = key[0];
                int yKey = key[1];

                // Name e.g. pieceWillMoveTo
                int xValue = coordinates[0];
                int yValue = coordinates[1];
                
                Piece pieceToMove = this.currentGamePositionTiles[xKey][yKey].getPiece();

                //sjekk for kongen, skal oppdatere område hvis han flytter 

                boolean kingMovedWhite = false;
                boolean kingMovedBlacked = false;

                if (pieceToMove instanceof King && pieceToMove.getColor() == 'w') {
                    this.whiteKing = new int[]{xValue, yValue};
                    kingMovedWhite = true;
                }
                
                 if (pieceToMove instanceof King && pieceToMove.getColor() == 'b') {
                    this.blackKing = new int[]{xValue, yValue};
                    kingMovedBlacked = true;
                }

                // pieceAtThespotWeAreMovingTo
                Piece placeBackPiece = this.currentGamePositionTiles[xValue][yValue].getPiece();

                //Se på dette iforhold til okupering osv - i piece klassn
                this.currentGamePositionTiles[xKey][yKey].removePiece();
                this.currentGamePositionTiles[xKey][yKey].setOccupied(false);

                this.currentGamePositionTiles[xValue][yValue].setPiece(pieceToMove);
                this.currentGamePositionTiles[xValue][yValue].isOccupied();

                int[] kingLocation;

                //dette trengs ikke gjøres hver gang - flytt 
                
                if (colorToMove.getColor() == 'w') {
                    kingLocation = this.whiteKing;
                }
                else {
                    kingLocation = this.blackKing;
                }

                HashMap<int[], ArrayList<int[]>> legalMovesOpposite = populateAllMoves(colorNotMoving, this.currentGamePositionTiles);

                Collection<ArrayList<int[]>> allOppositeMoves = legalMovesOpposite.values();

                for (ArrayList<int[]> oppositeCorrArray: allOppositeMoves) {
                    for (int[] oppositeCorr : oppositeCorrArray) {
                        if (oppositeCorr[0] == kingLocation[0] && oppositeCorr[1] == kingLocation[1]) {

                            ArrayList<int[]> pieceMoves = legalMoves.get(key);
                            
                            for (int[] chosenCorr : pieceMoves) {
                                if (chosenCorr[0] == coordinates[0] && chosenCorr[1] == coordinates[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }

                            }
                
                        }

                        //håndtering av lovlig rokade

                        else if (pieceToMove instanceof King && !pieceToMove.getHasMoved()) {
                            if (colorToMove.getColor() == 'w') {
                                if (xValue == 0 && yValue == 6) {
                                    if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                    else if (oppositeCorr[0] == 0 && oppositeCorr[1] == 5) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                }
                                else if (xValue == 0 && yValue == 2) {
                                    if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                    else if (oppositeCorr[0] == 0 && oppositeCorr[1] == 3) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                }
                            }
                            else if (colorToMove.getColor() == 'b') {
                                if (xValue == 7 && yValue == 6) {
                                    if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                    else if (oppositeCorr[0] == 7 && oppositeCorr[1] == 5) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                }
                                else if (xValue == 7 && yValue == 2) {
                                    if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                    else if (oppositeCorr[0] == 7 && oppositeCorr[1] == 3) { //Hvis feltet som paseres er sjakk 
                                        movesToBeRemoved.add(allMovesForAPiece.get(count));
                                    }
                                }
                            }
                            
                        }



                        // else if (pieceToMove instanceof King && !pieceToMove.getHasMoved() && colorToMove.getColor() == 'w' && yValue == 6) {
                        //     //fjerner rokade trekk
                        //     if (xValue == 0) {
                        //         if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                        //             movesToBeRemoved.add(allMovesForAPiece.get(count));
                        //         }
                        //         else if (oppositeCorr[0] == 0 && oppositeCorr[1] == 5) { //Hvis feltet som paseres er sjakk 
                        //             movesToBeRemoved.add(allMovesForAPiece.get(count));
                        //         }
                            
                        //     }
                        // }

                        // else if (pieceToMove instanceof King && !pieceToMove.getHasMoved() && colorToMove.getColor() == 'w' && yValue == 2) { //sjakk på 3
                        //     //fjerner rokade trekk
                        //     if (xValue == 0) {
                        //         if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                        //             movesToBeRemoved.add(allMovesForAPiece.get(count));
                        //         }
                        //         else if (oppositeCorr[0] == 0 && oppositeCorr[1] == 3) {
                        //             movesToBeRemoved.add(allMovesForAPiece.get(count));
                        //         }
                        //     }
                            
                        // }

                        // else if (pieceToMove instanceof King && !pieceToMove.getHasMoved() && colorToMove.getColor() == 'b' && yValue == 6) {
                        //     //fjerner rokade trekk
                        //     if (xValue == 7) {
                        //         if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                        //             movesToBeRemoved.add(allMovesForAPiece.get(count));
                        //         }
                        //         else if (oppositeCorr[0] == 7 && oppositeCorr[1] == 5) { //Hvis feltet som paseres er sjakk 
                        //             movesToBeRemoved.add(allMovesForAPiece.get(count));
                        //         }
                            
                        //     }
                        // }

                        // else if (pieceToMove instanceof King && !pieceToMove.getHasMoved() && colorToMove.getColor() == 'b' && yValue == 2) { //sjakk på 3
                        //     //fjerner rokade trekk
                        //     if (xValue == 7) {
                        //         if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                        //             movesToBeRemoved.add(allMovesForAPiece.get(count));
                        //         }
                        //         else if (oppositeCorr[0] == 7 && oppositeCorr[1] == 3) {
                        //             movesToBeRemoved.add(allMovesForAPiece.get(count));
                        //         }
                        //     }
                            
                        // }
                                  
                    }
                    
                }
                

                //flytt brikken tilbake, den som ble flyttet på - går vell å lage metode
                Piece pieceToMoveBack = this.currentGamePositionTiles[xValue][yValue].getPiece();


                //Tror ikke dette trengs til noe egentlig
                // if (pieceToMoveBack instanceof Pawn && Math.abs(yValue - yKey) == 2) {
                //     ((Pawn)pieceToMoveBack).setMovedTwoLastTurn(false);
                // }

                this.currentGamePositionTiles[xValue][yValue].removePiece();
                this.currentGamePositionTiles[xValue][yValue].setPiece(placeBackPiece);
                if (this.currentGamePositionTiles[xValue][yValue].getPiece() == null) {
                    this.currentGamePositionTiles[xValue][yValue].setOccupied(false);
                    //overkill etter fix? - se på - setOccupied
                }
                
                
                this.currentGamePositionTiles[xKey][yKey].setPiece(pieceToMoveBack);
                //overkill? - se på piece 
                this.currentGamePositionTiles[xKey][yKey].isOccupied();

                //setter kongen tilbake
                if (kingMovedWhite) {
                    this.whiteKing = new int[]{xKey, yKey};
                }
                if (kingMovedBlacked) {
                     this.blackKing = new int[]{xKey, yKey};
                }
                
                count ++;
            }

            allMovesForAPiece.removeAll(movesToBeRemoved);
        }

        return legalMoves;
    }

    public HashMap<int[], ArrayList<int[]>> CheckforCheckMateAndPat () {

        MovementPatterns colorToMove;
        MovementPatterns colorNotMoving;

        HashMap<int[], ArrayList<int[]>> legalMoves = eliminateChecks();
        
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
    
        for (int[] key : legalMoves.keySet()) {
            if (notGameOver) {
                break;
            }

            if (legalMoves.get(key).size() != 0) {
                notGameOver = true;
            }
        }

        if (!notGameOver) {

            HashMap<int[], ArrayList<int[]>> canTheKingBeTakenNoMoves = populateAllMoves(colorNotMoving, currentGamePositionTiles);
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
            this.gameStatus = 2;
        } 
        else if (!kingCanBetaken && !notGameOver) {
            this.gameStatus = 1;
        }

        return legalMoves;
    }

    public void increaseMoveNumber () {
        this.moveNumber ++;
    }

    public int getMoveNUmber() {
        return this.moveNumber;
    }

    public int getGameStatus() {
        return this.gameStatus;
    }

    private void setKingPosition() {

        for (Tile[] tileRow : this.currentGamePositionTiles) {
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

        CheckLegalMoves checklegalmoves = new CheckLegalMoves(tiles);


        checklegalmoves.CheckforCheckMateAndPat();
        
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }
}


