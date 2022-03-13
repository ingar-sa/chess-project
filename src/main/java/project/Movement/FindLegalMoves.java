package project.Movement;

import java.util.ArrayList;
import java.util.HashMap;

import project.Board.Chessboard;
import project.Board.Tile;

public class CheckLegalMoves {
    
    private Tile[][] currentGamePositionTiles;

    private HashMap<int[], ArrayList<int[]>> legalMoves = new HashMap<int[], ArrayList<int[]>>();
    private MovementPatterns whiteMovement;
    private MovementPatterns blackMovement;    

    CheckLegalMoves(Tile[][] boardTiles) {
        this.currentGamePositionTiles = boardTiles;
        
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
    }

    private void populateAllMoves(MovementPatterns movementPattern) {
        movementPattern.setBoardTiles(currentGamePositionTiles);

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
    }

    private void eliminateChecks() {
        for (int[] key : legalMoves.keySet()) {
            for (int[] coordinates : legalMoves.get(key)) {
                Chessboard shadowChessboardObject = new Chessboard(currentGamePositionTiles);
                
                
            }
        }
    }
}
