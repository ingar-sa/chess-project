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


public class FindLegalMoves {
    
    private Tile[][] currentGamePositionTiles;
    private Movement whiteMovement;
    private Movement blackMovement;
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
    FindLegalMoves(Tile[][] boardTiles, Chessboard board) {
        this.currentGamePositionTiles = boardTiles;

        for (Tile[] tileRow : boardTiles) {
            for (Tile tile: tileRow) {
                if (tile.getPiece() instanceof King 
                    && ((King)tile.getPiece()).getColor() == 'w') {
                    this.whiteKing = new int[]{tile.getRow(), tile.getCol()};
                }
                else if (tile.getPiece() instanceof King 
                         && ((King)tile.getPiece()).getColor() == 'b') {
                    this.blackKing = new int[]{tile.getRow(), tile.getCol()};
                } //Bytte rekkefølge raskere?
            }
        }
        //this.currentChessBoard = board;
        //currentChessBoard.printBoard();
        
        this.whiteMovement = new Movement('w');
        this.blackMovement = new Movement('b');
          
    }
    //endret så den returnerer 
    private HashMap<int[], ArrayList<int[]>> findAllMoves(Movement Movement, Tile[][] boardTiles) {
        Movement.setBoardTiles(boardTiles);
        
        // The hashmap's keys are the coordinates to a tile on the board (e.g. int[]{0, 5} = 1f), 
        // and the value for a key are the legal moves the piece *on* that tile can perform.
        // The legal moves are given as an ArrayList of the coordinates to the tiles it can move to.
        HashMap<int[], ArrayList<int[]>> legalMoves = new HashMap<int[], ArrayList<int[]>>();
        
        for (Tile[] row : boardTiles) {
            for (Tile tile : row) {
                if (tile.isOccupied() 
                    && tile.getPiece().getColor() == Movement.getColor()) {

                    int[] key = new int[] {tile.getRow(), tile.getCol()};
                    ArrayList<int[]> allMoves = Movement.moveHandler(tile);

                    legalMoves.put(key, allMoves);
                    
                }
            }
        }
        return legalMoves;
    }

    private HashMap<int[], ArrayList<int[]>> eliminateChecks() {
        Movement colorMoving;
        Movement colorNotMoving;

        if (moveNumber % 2 == 0) {
            colorMoving = whiteMovement;
            colorNotMoving = blackMovement;
        } 
        else {
            colorMoving = blackMovement;
            colorNotMoving = whiteMovement;
        }

        HashMap<int[], ArrayList<int[]>> legalMoves = findAllMoves(colorMoving, currentGamePositionTiles);
        

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

                boolean whiteKingMoved = false;
                boolean blackKingMoved = false;

                if (pieceToMove instanceof King && pieceToMove.getColor() == 'w') {
                    this.whiteKing = new int[]{xValue, yValue};
                     whiteKingMoved = true;
                }
                
                 if (pieceToMove instanceof King && pieceToMove.getColor() == 'b') {
                    this.blackKing = new int[]{xValue, yValue};
                     blackKingMoved = true;
                }

                Piece placeBackPiece = shadowBoardTiles[xValue][yValue].getPiece();

                shadowBoardTiles[xKey][yKey].removePiece();
                shadowBoardTiles[xKey][yKey].setOccupied(false);

                shadowBoardTiles[xValue][yValue].setPiece(pieceToMove);
                shadowBoardTiles[xValue][yValue].isOccupied();


                //skal dette brukes til noe?
                Movement color;

                if (moveNumber % 2 == 0) color = whiteMovement;
                else                     color = blackMovement;
                
                int[] kingLocation;
                int[] kingStartLocation;
                

                if (colorMoving.getColor() == 'w') {
                    kingLocation = this.whiteKing;
                }
                else {
                    kingLocation = this.blackKing;
                }

                if (colorMoving.getColor() == 'w') {
                    kingStartLocation = new int[]{0, 4}; 
                }
                else {
                    kingStartLocation = new int[]{7, 4}; 
                }


                HashMap<int[], ArrayList<int[]>> opponentLegalMoves = findAllMoves(colorNotMoving, shadowBoardTiles);

                Collection<ArrayList<int[]>> allOpponentMoves = opponentLegalMoves.values();

                for (ArrayList<int[]> opponentCoordinateArray: allOpponentMoves) {
                    for (int[] opponentCoordinate : opponentCoordinateArray) {
                        if (opponentCoordinate[0] == kingLocation[0] && opponentCoordinate[1] == kingLocation[1]) {

                            ArrayList<int[]> pieceMoves = legalMoves.get(key);
                            
                            for (int[] chosenCoordinate : pieceMoves) {
                                if (chosenCoordinate[0] == coordinates[0] && chosenCoordinate[1] == coordinates[1]) {
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

                        else if (pieceToMove instanceof King 
                                && !pieceToMove.hasMoved() 
                                && colorMoving.getColor() == 'w' 
                                && yValue == 6) {

                            //fjerner rokade trekk
                            if (xValue == 0) {
                                if (opponentCoordinate[0] == kingStartLocation[0] && opponentCoordinate[1] == kingStartLocation[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                                else if (opponentCoordinate[0] == 0 && opponentCoordinate[1] == 5) { //Hvis feltet som paseres er sjakk 
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                            
                            }
                        }

                        else if (pieceToMove instanceof King && !pieceToMove.hasMoved() && colorMoving.getColor() == 'w' && yValue == 2) { //sjakk på 3
                            //fjerner rokade trekk
                            if (xValue == 0) {
                                if (opponentCoordinate[0] == kingStartLocation[0] && opponentCoordinate[1] == kingStartLocation[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                                else if (opponentCoordinate[0] == 0 && opponentCoordinate[1] == 3) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                            }
                            
                        }

                        else if (pieceToMove instanceof King && !pieceToMove.hasMoved() && colorMoving.getColor() == 'w' && yValue == 6) {
                            //fjerner rokade trekk
                            if (xValue == 7) {
                                if (opponentCoordinate[0] == kingStartLocation[0] && opponentCoordinate[1] == kingStartLocation[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                                else if (opponentCoordinate[0] == 7 && opponentCoordinate[1] == 5) { //Hvis feltet som paseres er sjakk 
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                            
                            }
                        }

                        else if (pieceToMove instanceof King && !pieceToMove.hasMoved() && colorMoving.getColor() == 'b' && yValue == 2) { //sjakk på 3
                            //fjerner rokade trekk
                            if (xValue == 7) {
                                if (opponentCoordinate[0] == kingStartLocation[0] && opponentCoordinate[1] == kingStartLocation[1]) {
                                    movesToBeRemoved.add(allMovesForAPiece.get(count));
                                }
                                else if (opponentCoordinate[0] == 7 && opponentCoordinate[1] == 3) {
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

                //xValue, yValue are incredibly vague and need to be renamed
                shadowBoardTiles[xValue][yValue].removePiece();
                shadowBoardTiles[xValue][yValue].setPiece(placeBackPiece);
                if (shadowBoardTiles[xValue][yValue].getPiece() == null) {
                    shadowBoardTiles[xValue][yValue].setOccupied(false);
                    //overkill etter fix? - se på - setOccupied
                }
                
                //Same as above.
                shadowBoardTiles[xKey][yKey].setPiece(pieceToMoveBack);
                shadowBoardTiles[xKey][yKey].isOccupied();

                //setter kongen tilbake
                if (whiteKingMoved) {
                    this.whiteKing = new int[]{xKey, yKey};
                }
                if (blackKingMoved) {
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

        FindLegalMoves checklegalmoves = new FindLegalMoves(tiles, chessboard);

        checklegalmoves.eliminateChecks();
    }
}
