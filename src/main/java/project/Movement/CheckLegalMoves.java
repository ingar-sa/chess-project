package project.Movement;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import project.Game;
import project.Board.Chessboard;
import project.Board.Tile;
import project.Pieces.King;
import project.Pieces.Piece;

public class CheckLegalMoves {
    
    private Tile[][] currentGamePositionTiles;
    private MovementPatterns whiteMovement;
    private MovementPatterns blackMovement;

    //må vell egentlig hentes fra hovedbrettet 
    private int[] whiteKing= new int[]{0, 4};
    private int[] blackKing= new int[]{7, 4};
    
    int moveNumber = 0; 

    CheckLegalMoves(Tile[][] boardTiles) {
        this.currentGamePositionTiles = boardTiles;
        
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
          
    }
    //endret så den returnerer 
    private HashMap<int[], ArrayList<int[]>> populateAllMoves(MovementPatterns movementPattern) {
        movementPattern.setBoardTiles(currentGamePositionTiles);

        HashMap<int[], ArrayList<int[]>> legalMoves = new HashMap<int[], ArrayList<int[]>>();
        
        for (Tile[] row : currentGamePositionTiles) {
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

        HashMap<int[], ArrayList<int[]>> legalMoves = populateAllMoves(colorToMove);


        for (int[] key : legalMoves.keySet()) {
            for (int[] coordinates : legalMoves.get(key)) {
                Chessboard shadowChessboardObject = new Chessboard();

                Tile[][] shadowBoardTiles = shadowChessboardObject.getBoardTiles();

                int xKey = key[0];
                int yKey = key[1];

                int xValue = coordinates[0];
                int yValue = coordinates[1];
                
                Piece pieceToMove = shadowBoardTiles[xKey][yKey].getPiece();
                shadowBoardTiles[xKey][yKey].removePiece();
                shadowBoardTiles[xKey][yKey].setOccupied(false);

                shadowBoardTiles[xValue][yValue].setPiece(pieceToMove);
                shadowBoardTiles[xValue][yValue].isOccupied();

                MovementPatterns color;

                if (moveNumber % 2 == 0) {
                    color = whiteMovement;
                } 
                else {
                    color = blackMovement;
                }

                int[] kingLocation;

                if (colorToMove.getColor() == 'w') {
                    kingLocation = this.whiteKing;
                }
                else {
                    kingLocation = this.blackKing;
                }

                HashMap<int[], ArrayList<int[]>> legalMovesOpposite = populateAllMoves(colorNotMoving);

                Collection<ArrayList<int[]>> allOppositeMoves = legalMovesOpposite.values();

                for (ArrayList<int[]> oppositeCorrArray: allOppositeMoves) {
                    for (int[] oppositeCorr : oppositeCorrArray) {
                        if (oppositeCorr[0] == kingLocation[0] && oppositeCorr[1] == kingLocation[1]) {

                            ArrayList<int[]> pieceMoves = legalMoves.get(key);
                            
                            for (int[] chosenCorr : pieceMoves) {
                                if (chosenCorr[0] == coordinates[0] && chosenCorr[1] == coordinates[1]) {
                                    pieceMoves.remove(0);
                                }

                            }
                    }
                    
                }
                    
                }
                



                
                
            }
        }

        return legalMoves;
    }
    public static void main(String[] args) {
        Chessboard chessboard = new Chessboard();
        
        Tile[][] tiles = chessboard.getBoardTiles();
        CheckLegalMoves checklegalmoves =  new CheckLegalMoves(tiles);
        checklegalmoves.eliminateChecks();
    }
}
