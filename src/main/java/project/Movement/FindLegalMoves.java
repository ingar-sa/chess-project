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
    //private Chessboard currentChessBoard;

    //må vell egentlig hentes fra hovedbrettet
    private int[] whiteKing;
    private int[] blackKing;
    // private int[] whiteKing = new int[]{0, 4};
    // private int[] blackKing = new int[]{7, 4};
    
    //se hvordan dette skal gjøres
    //Tur
    int moveNumber = 1; 


    //Sjekk om vi trenger chessboard
    CheckLegalMoves(Tile[][] boardTiles, Chessboard board) {
        this.currentGamePositionTiles = boardTiles;

        for (Tile[] tileRow : boardTiles) {
            for (Tile tile: tileRow) {
                if (tile.getPiece() instanceof King && ((King)tile.getPiece()).getColor() == 'w') {
                    this.whiteKing = new int[]{tile.getRow(), tile.getCol()};
                }
                else if (tile.getPiece() instanceof King && ((King)tile.getPiece()).getColor() == 'b') {
                    this.blackKing = new int[]{tile.getRow(), tile.getCol()};
                } //Bytte rekkefølge raskere?
            }
        }
        //this.currentChessBoard = board;
        //currentChessBoard.printBoard();
        
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
          
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

        if (moveNumber % 2 == 0) {
            colorToMove = whiteMovement;
            colorNotMoving = blackMovement;
        } 
        else {
            colorToMove = blackMovement;
            colorNotMoving = whiteMovement;
        }

        HashMap<int[], ArrayList<int[]>> legalMoves = populateAllMoves(colorToMove, currentGamePositionTiles);
        

        for (int[] key : legalMoves.keySet()) {

           ArrayList<int[]> allMovesForAPiece = legalMoves.get(key);
           List<int[]> movesToBeRemoved = new ArrayList<int[]>();

           //Count for what element that should be removed
           int count = 0;

            for (int[] coordinates : allMovesForAPiece) {
                //Chessboard shadowChessboardObject = new Chessboard();

                Tile[][] shadowBoardTiles = this.currentGamePositionTiles;
 
                int xKey = key[0];
                int yKey = key[1];

                int xValue = coordinates[0];
                int yValue = coordinates[1];
                
                Piece pieceToMove = shadowBoardTiles[xKey][yKey].getPiece();

                // Tror ikke dette er nødvendig  
                // if (pieceToMove instanceof Pawn && Math.abs(yValue - yKey) == 2) {
                //     ((Pawn)pieceToMove).setMovedTwoLastTurn(true);
                // }


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

                Piece placeBackPiece = shadowBoardTiles[xValue][yValue].getPiece();

                //Se på dette iforhold til okupering osv. 
                shadowBoardTiles[xKey][yKey].removePiece();
                shadowBoardTiles[xKey][yKey].setOccupied(false);

                shadowBoardTiles[xValue][yValue].setPiece(pieceToMove);
                shadowBoardTiles[xValue][yValue].isOccupied();

                int[] kingLocation;

                //dette trengs ikke gjøres hver gang - flytt 
                int[] originalKingLocation;
                
                if (colorToMove.getColor() == 'w') {
                    kingLocation = this.whiteKing;
                }
                else {
                    kingLocation = this.blackKing;
                }

                //Dette burde vell flyttes 
                if (colorToMove.getColor() == 'w') {
                    originalKingLocation = new int[]{0, 4};
                }
                else {
                    originalKingLocation = new int[]{7, 4};
                }


                HashMap<int[], ArrayList<int[]>> legalMovesOpposite = populateAllMoves(colorNotMoving, shadowBoardTiles);

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

                        else if (pieceToMove instanceof King && !pieceToMove.hasMoved() && colorToMove.getColor() == 'w' && yValue == 6) {
                            //fjerner rokade trekk
                            if (xValue == 0) {
                                if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                                else if (oppositeCorr[0] == 0 && oppositeCorr[1] == 5) { //Hvis feltet som paseres er sjakk 
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                            
                            }
                        }

                        else if (pieceToMove instanceof King && !pieceToMove.hasMoved() && colorToMove.getColor() == 'w' && yValue == 2) { //sjakk på 3
                            //fjerner rokade trekk
                            if (xValue == 0) {
                                if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                                else if (oppositeCorr[0] == 0 && oppositeCorr[1] == 3) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                            }
                            
                        }

                        else if (pieceToMove instanceof King && !pieceToMove.hasMoved() && colorToMove.getColor() == 'b' && yValue == 6) {
                            //fjerner rokade trekk
                            if (xValue == 7) {
                                if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                                else if (oppositeCorr[0] == 7 && oppositeCorr[1] == 5) { //Hvis feltet som paseres er sjakk 
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                            
                            }
                        }

                        else if (pieceToMove instanceof King && !pieceToMove.hasMoved() && colorToMove.getColor() == 'b' && yValue == 2) { //sjakk på 3
                            //fjerner rokade trekk
                            if (xValue == 7) {
                                if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                                else if (oppositeCorr[0] == 7 && oppositeCorr[1] == 3) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                            }
                            
                        }
                                  
                    }
                    
                }
                

                //flytt brikken tilbake, den som ble flyttet på - går vell å lage metode
                Piece pieceToMoveBack = shadowBoardTiles[xValue][yValue].getPiece();


                //Tror ikke dette trengs til noe egentlig
                // if (pieceToMoveBack instanceof Pawn && Math.abs(yValue - yKey) == 2) {
                //     ((Pawn)pieceToMoveBack).setMovedTwoLastTurn(false);
                // }

                shadowBoardTiles[xValue][yValue].removePiece();
                shadowBoardTiles[xValue][yValue].setPiece(placeBackPiece);
                if (shadowBoardTiles[xValue][yValue].getPiece() == null) {
                    shadowBoardTiles[xValue][yValue].setOccupied(false);
                    //overkill etter fix? - se på - setOccupied
                }
                
                
                shadowBoardTiles[xKey][yKey].setPiece(pieceToMoveBack);
                shadowBoardTiles[xKey][yKey].isOccupied();

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

        //Unødvendig er allerde 0 
        if (notGameOver) {
            this.gameStatus = 0;
        }

        else if (kingCanBetaken && !notGameOver) {
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

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Chessboard chessboard = new Chessboard();
        
        Tile[][] tiles = chessboard.getBoardTiles();
        Rook rook = new Rook("bR1",'b');
        Rook rook2 = new Rook("bR2",'b');
        Rook rook3 = new Rook("bR3", 'b');
        Rook rook4 = new Rook("wR4", 'w');
        Rook rook5 = new Rook("bR5", 'b');
        Bishop bishop = new Bishop("bB1", 'b');
        Bishop bishop2 = new Bishop("wB1", 'w');

        Pawn enpawn = new Pawn("bP1", 'b');
        Pawn enpawn2 = new Pawn("wP2", 'w');
        Pawn pawn3 = new Pawn("bP3", 'b');
        Pawn wpawn1 = new Pawn("wP2", 'w');
        Pawn wpawn3 = new Pawn("wP3", 'w');

        King testKing = new King("wX2", 'w');

        Queen queen = new Queen("wQ1", 'w');

        Knight kn1 = new Knight("bK1", 'b');
        Knight kn2 = new Knight("wK2", 'w');
        Knight kn3 = new Knight("bK1", 'b');

        // tiles[0][2].setPiece(rook);
        //tiles[2][6].setPiece(bishop);
        //tiles[1][4].setPiece(bishop);
        tiles[1][0].removePiece();
        tiles[1][1].removePiece();
        tiles[1][2].removePiece();
        tiles[1][3].removePiece();
        tiles[1][4].removePiece();
        tiles[1][5].removePiece();
        tiles[1][6].removePiece();
        tiles[1][7].removePiece();
        //tiles[6][8].removePiece();

        tiles[0][1].removePiece();
        tiles[0][2].removePiece();
        tiles[0][3].removePiece();
        tiles[0][0].removePiece();
        tiles[0][5].removePiece();
        tiles[0][6].removePiece();
        tiles[0][7].removePiece();
        tiles[0][4].removePiece();
        // tiles[0][3].removePiece();
        // tiles[1][1].removePiece();
        // tiles[1][2].removePiece();
        // tiles[1][3].removePiece();
        // tiles[1][4].removePiece();
        // tiles[1][7].removePiece();
        // tiles[0][7].removePiece();
        // tiles[0][7].setPiece(rook);
        // tiles[7][5].setPiece(rook2);
        // tiles[1][7].setPiece(rook);
        // tiles[7][3].setPiece(rook3);

        // //tiles[4][4].setOccupied(true);
        // tiles[7][1].removePiece();
        // tiles[7][2].removePiece();
        // //tiles[7][3].removePiece();
        // //tiles[7][5].removePiece();
        // tiles[7][6].removePiece();
        // tiles[7][4].removePiece();
    // tiles[1][0].removePiece();
        // tiles[7][0].removePiece();

        //tiles[4][4].setPiece(rook2);
        // tiles[0][5].setPiece(enpawn);
        //tiles[2][2].setPiece(bishop);
        //tiles[2][5].setPiece(testKing);
        //tiles[1][7].setPiece(rook);
        //tiles[1][0].setPiece(enpawn);
        // tiles[1][5].removePiece();
        // tiles[2][5].removePiece();
        // tiles[7][2].setPiece(testKing);
        //tiles[3][5].setPiece(rook2);
        //tiles[3][7].setPiece(bishop);
        //tiles[2][5].getPiece().setHasMoved();
        //((Pawn)tiles[4][5].getPiece()).setMovedTwoLastTurn(true);
        // ((Pawn)tiles[4][5].getPiece()).setMovedTwo();

        //tiles[4][4].setPiece(enpawn2);

        
        


                
        // tiles[0][6].removePiece();
        tiles[0][5].removePiece();
        tiles[0][6].removePiece();
        tiles[0][1].removePiece();
        tiles[1][3].removePiece();

        tiles[7][0].removePiece();
        tiles[7][5].removePiece();
        tiles[7][6].removePiece();
        tiles[6][0].removePiece();
        tiles[6][2].removePiece();
        tiles[6][5].removePiece();
        
        
        // tiles[1][4].setPiece(kn1);
        // tiles[3][0].setPiece(kn2);
        // tiles[5][5].setPiece(kn3);

        // tiles[0][5].setPiece(rook);

    
        // tiles[3][1].setPiece(bishop);
        // tiles[3][2].setPiece(bishop2);
        
        // tiles[2][3].setPiece(wpawn1);
        // tiles[4][4].setPiece(wpawn3);

        tiles[0][4].setPiece(testKing);
        //tiles[0][6].setPiece(rook2);
        tiles[2][4].setPiece(kn1);
        //tiles[3][2].setPiece(bishop);
        tiles[4][3].setPiece(rook);
        tiles[3][6].setPiece(pawn3);
        //tiles[0][3].setPiece(queen);
        tiles[3][4].setPiece(rook3);
        //tiles[0][4].setPiece(kn2);
        //tiles[2][5].setPiece(rook4);
        //tiles[1][0].setPiece(rook5);
        
        //tiles[2][2].setPiece(enpawn2);
        //tiles[5][0].setPiece(wpawn1);
        tiles[5][6].setPiece(bishop2);
        //tiles[1][0].setPiece(rook4);
        enpawn2.setHasMoved();
        
        

       
      


        


       
        chessboard.printBoard();

        CheckLegalMoves checklegalmoves = new CheckLegalMoves(tiles, chessboard);
        //checklegalmoves.eliminateChecks();

        checklegalmoves.CheckforCheckMateAndPat();
        
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
    }
}
