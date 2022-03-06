package project;

import project.pieces.Bishop;
import project.pieces.King;
import project.pieces.Knight;
import project.pieces.Pawn;
import project.pieces.Queen;
import project.pieces.Rook;

/* --INITIAL IDEA OF THE CLASS. SUBJECT TO CHANGE--
 * Class representing the chess board.
 * This is where game logic and state is handled,
 * as well as passing board state to file handling classes 
 */


public class Board {

    private Tile[][] boardTiles = new Tile[8][8];
    private final Character[] columnLetters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    Board () {
        makeBoard();
        placePieces();
    }


    private void makeBoard() {
        for (int row = 1; row < 9; row++) {
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
                boardTiles[row - 1][column - 1] = tile;
            }
        }
    }


    private void placePieces() {
        
        //White

        for (Tile tile : boardTiles[1]) {
            tile.setOccupied(true);

            String name = "wP" +tile.getCol();
            Pawn pawn = new Pawn(name, 'w');
            tile.setPiece(pawn);
        }
        
        for (Tile tile : boardTiles[0]) {
            tile.setOccupied(true);

            if (tile.getCol() == 1 || tile.getCol() == 8) {
                String name = "wR" + tile.getCol();
                Rook rook = new Rook(name, 'w');
                tile.setPiece(rook);
            }
            if (tile.getCol() == 2 || tile.getCol() == 7) {
                String name = "wK" + tile.getCol();
                Knight knight = new Knight(name, 'w');
                tile.setPiece(knight);
            }
            if (tile.getCol() == 3 || tile.getCol() == 6) {
                String name = "wB" + tile.getCol();
                Bishop bishop = new Bishop(name, 'w');
                tile.setPiece(bishop);
            }
            
            if (tile.getCol() == 4) {
                String name = "wQ" + tile.getCol();
                Queen queen = new Queen(name, 'w');
                tile.setPiece(queen);
            }
            
            if (tile.getCol() == 5) {
                String name = "wX" + tile.getCol();
                King king = new King(name, 'w');
                tile.setPiece(king);        
            }
        }

        //Black

        for (Tile tile : boardTiles[6]) {
            tile.setOccupied(true);

            String name = "bP" + tile.getCol();
            Pawn pawn = new Pawn(name, 'b');
            tile.setPiece(pawn);
        }

        for (Tile tile : boardTiles[7]) {
            tile.setOccupied(true); 

            if (tile.getCol() == 1 || tile.getCol() == 8) {
                String name = "bR" + tile.getCol();
                Rook rook = new Rook(name, 'b');
                tile.setPiece(rook);
            }
            if (tile.getCol() == 2 || tile.getCol() == 7) {
                String name = "bK" + tile.getCol();
                Knight knight = new Knight(name, 'b');
                tile.setPiece(knight);
            }
            if (tile.getCol() == 3 || tile.getCol() == 6) {
                String name = "bB" + tile.getCol();
                Bishop bishop = new Bishop(name, 'b');
                tile.setPiece(bishop);
            }
            
            if (tile.getCol() == 4) {
                String name = "bQ" + tile.getCol();
                Queen queen = new Queen(name, 'b');
                tile.setPiece(queen);
            }
            
            if (tile.getCol() == 5) {
                String name = "bX" + tile.getCol();
                King king = new King(name, 'b');
                tile.setPiece(king);        
            }
        }
    }

    
    public void printBoard() {
    
        //Collections.reverse(boardTiles);
        for (int row = 7; row >= 0; row--) {
            String ofRow = (row + 1) + " ";
            for (int col = 0; col < 8; col++) {
                
                Tile tile = boardTiles[row][col];
                try {
                    String piece = tile.getPiece().getName();
                    ofRow += "| " + piece + ' ';
                }
                catch(Exception e) {
                    ofRow += "| --- "; 
                }
            }

            System.out.println(ofRow);
            //System.out.println();
        }

        String colLetters = "  ";

        for (char columnLetter : columnLetters) {
            colLetters += "   " + columnLetter + "  ";
        }
        System.out.println(colLetters + "\n\n");

    }

    public Tile[][] getBoardTiles() {
        return boardTiles;
    }

    public static void main(String[] args) {
        Board gameBoard = new Board();
        gameBoard.makeBoard();
        gameBoard.placePieces();

        gameBoard.printBoard();
    }
}
