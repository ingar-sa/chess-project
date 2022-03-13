package project.Board;

import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Pawn;
import project.Pieces.Queen;
import project.Pieces.Rook;


public class Chessboard {

    // Make hashmap
    private Tile[][] boardTiles = new Tile[8][8];
    private final int[] columnLetters = {0, 1, 2, 3, 4, 5, 6, 7};

    public Chessboard () {
        makeBoard();
        placePieces();
    }

    public Chessboard(Tile[][] boardTiles) {
        this.boardTiles = boardTiles;
    }


    private void makeBoard() {
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                
                char firstColor;
                char secondColor; 
                char tileColor;

                if (row % 2 == 1) {
                    firstColor = 'w';
                    secondColor = 'b';
                } 
                else {
                    firstColor = 'b';
                    secondColor = 'w';
                }

                tileColor = (col % 2 == 1) ? firstColor : secondColor;

                Tile tile = new Tile(row, col, tileColor);
                boardTiles[row][col] = tile;
            }
        }
    }


    private void placePieces() {
        
        //White

        for (Tile tile : boardTiles[1]) {
            tile.setOccupied(true);

            String name = "wP" + tile.getCol();
            Pawn pawn = new Pawn(name, 'w');
            tile.setPiece(pawn);
        }
        
        for (Tile tile : boardTiles[0]) {
            tile.setOccupied(true);

            if (tile.getCol() == 0 || tile.getCol() == 7) {
                String name = "wR" + tile.getCol();
                Rook rook = new Rook(name, 'w');
                tile.setPiece(rook);
            }
            if (tile.getCol() == 1 || tile.getCol() == 6) {
                String name = "wK" + tile.getCol();
                Knight knight = new Knight(name, 'w');
                tile.setPiece(knight);
            }
            if (tile.getCol() == 2 || tile.getCol() == 5) {
                String name = "wB" + tile.getCol();
                Bishop bishop = new Bishop(name, 'w');
                tile.setPiece(bishop);
            }
            
            if (tile.getCol() == 3) {
                String name = "wQ" + tile.getCol();
                Queen queen = new Queen(name, 'w');
                tile.setPiece(queen);
            }
            
            if (tile.getCol() == 4) {
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

            if (tile.getCol() == 0 || tile.getCol() == 7) {
                String name = "bR" + tile.getCol();
                Rook rook = new Rook(name, 'b');
                tile.setPiece(rook);
            }
            if (tile.getCol() == 1 || tile.getCol() == 6) {
                String name = "bK" + tile.getCol();
                Knight knight = new Knight(name, 'b');
                tile.setPiece(knight);
            }
            if (tile.getCol() == 2 || tile.getCol() == 5) {
                String name = "bB" + tile.getCol();
                Bishop bishop = new Bishop(name, 'b');
                tile.setPiece(bishop);
            }
            
            if (tile.getCol() == 3) {
                String name = "bQ" + tile.getCol();
                Queen queen = new Queen(name, 'b');
                tile.setPiece(queen);
            }
            
            if (tile.getCol() == 4) {
                String name = "bX" + tile.getCol();
                King king = new King(name, 'b');
                tile.setPiece(king);        
            }
        }
    }

    
    public void printBoard() {
    
        //Collections.reverse(boardTiles);
        for (int row = 7; row >= 0; --row) {
            String ofRow = (row) + " ";
            for (int col = 0; col < 8; ++col) {
                
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

        for (int columnLetter : columnLetters) {
            colLetters += "   " + columnLetter + "  ";
        }
        System.out.println(colLetters + "\n\n");

    }

    public Tile[][] getBoardTiles() {
        return this.boardTiles;
    }

    public static void main(String[] args) {
        Chessboard gameBoard = new Chessboard();
        gameBoard.makeBoard();
        gameBoard.placePieces();
        gameBoard.printBoard();
    }
}
