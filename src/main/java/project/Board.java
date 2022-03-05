package project;

import java.lang.reflect.Method;
import java.util.ArrayList;

/* --INITIAL IDEA OF THE CLASS. SUBJECT TO CHANGE--
 * Class representing the chess board.
 * This is where game logic and state is handled,
 * as well as passing board state to file handling classes 
 */


public class Board {
    
    // Use for release version
    //private Tile[][] boardTiles;

    private ArrayList<ArrayList<Tile>> boardTiles = new ArrayList<ArrayList<Tile>>();
    private final Character[] tileLetters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    public void makeBoard() {
        for (int row = 1; row < 9; row++) {
            ArrayList<Tile> rowTiles = new ArrayList<Tile>();   
            
            for (int column = 1; column < 9; column++) {
                
                char firstColor;
                char secondColor; 
                char tileColor;

                if (row % 2 == 1) {
                    firstColor = 'b';
                    secondColor = 'w';
                } 
                else {
                    firstColor = 'w';
                    secondColor = 'b';
                }

                tileColor = (column % 2 == 1) ? firstColor : secondColor;

                Tile tile = new Tile(row, column, tileColor);
                rowTiles.add(tile);       
            }

            boardTiles.add(rowTiles);
        }
    }
    

    public static void main(String[] args) {
        Board gameBoard = new Board();
        gameBoard.makeBoard();
        
    }
}
