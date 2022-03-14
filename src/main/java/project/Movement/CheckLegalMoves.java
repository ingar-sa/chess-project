package project.Movement;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import project.Game;
import project.Board.Chessboard;
import project.Board.Tile;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Piece;
import project.Pieces.Rook;
import project.Pieces.Pawn;

public class CheckLegalMoves {
    
    private Tile[][] currentGamePositionTiles;
    private MovementPatterns whiteMovement;
    private MovementPatterns blackMovement;
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

                shadowBoardTiles[xKey][yKey].removePiece();
                shadowBoardTiles[xKey][yKey].setOccupied(false);

                shadowBoardTiles[xValue][yValue].setPiece(pieceToMove);
                shadowBoardTiles[xValue][yValue].isOccupied();


                //skal dette brukes til noe?
                MovementPatterns color;

                if (moveNumber % 2 == 0) {
                    color = whiteMovement;
                } 
                else {
                    color = blackMovement;
                }

                int[] kingLocation;
                int[] originalKingLocation;
                

                if (colorToMove.getColor() == 'w') {
                    kingLocation = this.whiteKing;
                }
                else {
                    kingLocation = this.blackKing;
                }

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
                                
                                //Tror ikke dette skal brukes til noe man kan ses på
                                // if (oppositeCorr[0] == originalKingLocation[0] && oppositeCorr[1] == originalKingLocation[1]) {
                                //     if (chosenCorr[0] == coordinates[0] && chosenCorr[1] == coordinates[1]) {
                                //         movesToBeRemoved.add(allMovesForAPiece.get(count));
                                //     }
                                // }

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

                        else if (pieceToMove instanceof King && !pieceToMove.hasMoved() && colorToMove.getColor() == 'w' && yValue == 6) {
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
                

                //flytt brikken tilbake, den som ble flyttet på 
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
    public static void main(String[] args) {
        Chessboard chessboard = new Chessboard();
        
        Tile[][] tiles = chessboard.getBoardTiles();
        Rook rook = new Rook("bR1",'b');
        Rook rook2 = new Rook("bR2",'b');
        Bishop bishop = new Bishop("bBi", 'b');
        Bishop bishop2 = new Bishop("bBi", 'b');
        Pawn enpawn = new Pawn("bP1", 'b');
        Pawn enpawn2 = new Pawn("wP2", 'w');
        King testKing = new King("wK2", 'b');
        tiles[0][2].setPiece(rook);
        //tiles[2][6].setPiece(bishop);
        //tiles[1][4].setPiece(bishop);
        tiles[0][5].removePiece();
        tiles[1][4].removePiece();
        tiles[1][6].removePiece();
        //tiles[5][4].setPiece(rook);
        //tiles[0][7].setPiece(rook2);
        //tiles[4][4].setOccupied(true);
        tiles[7][1].removePiece();
        tiles[7][2].removePiece();
        tiles[7][3].removePiece();
        tiles[7][5].removePiece();
        tiles[7][6].removePiece();
        tiles[7][4].removePiece();
        //tiles[4][4].setPiece(rook2);
        tiles[4][5].setPiece(enpawn);
        tiles[2][2].setPiece(bishop);
        tiles[2][5].setPiece(testKing);
        tiles[2][5].getPiece().setHasMoved();
        ((Pawn)tiles[4][5].getPiece()).setMovedTwoLastTurn(true);
        // ((Pawn)tiles[4][5].getPiece()).setMovedTwo();

        tiles[4][4].setPiece(enpawn2);

        
        


                
        tiles[0][6].removePiece();
        chessboard.printBoard();

        CheckLegalMoves checklegalmoves = new CheckLegalMoves(tiles, chessboard);

        checklegalmoves.eliminateChecks();
    }
}
