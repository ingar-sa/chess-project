package project;

import java.util.ArrayList;

import project.pieces.Bishop;
import project.pieces.King;
import project.pieces.Knight;
import project.pieces.Pawn;
import project.pieces.Queen;
import project.pieces.Rook;


public class PlayerMove {

    private Board board;
    private char color;

    PlayerMove (Board board, char color) {
        this.board = board;
        this.color = color;
    }

    public void moveHandler(Tile tile) {
        if (!(tile.isOccupied() && tile.getPiece().getColor() == this.color)) return;
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        Piece piece = tile.getPiece();
        
        if (piece instanceof Pawn) legalMoves = legalPawnMove(tile);
        System.out.println(legalMoves);
        // else if (piece instanceof Rook) legalRookMove(tile);
        // else if (piece instanceof Knight) legalKnightMove(tile);
        // else if (piece instanceof Bishop) legalBishopMove(tile);
        // else if (piece instanceof King) legalKingMove(tile);
        // else if (piece instanceof Queen) legalQueenMove(tile);
        
    }

    

    private Tile getTile(Tile tile) {
        if (tile.isOccupied() && tile.getColor() == this.color) {
            return tile;
        }
        
        return null;
    }

    private ArrayList<int[]> legalPawnMove(Tile tile) {     
        ArrayList<int[]> legalPawnMoves = new ArrayList<int[]>();
        
        Tile[][] boardTiles = board.getBoardTiles();
        // Pawn??
        Piece pawn = tile.getPiece(); 
        int row = tile.getRow();
        int col = tile.getCol();


        //ArrayList<int[]> legalCoordinates = new ArrayList<int[]>();
        
        int moveDirection = 1; 
        if (this.color == 'b') moveDirection = -1;
        
        

        Tile inFront  = boardTiles[row+(1*moveDirection)][col];
        Tile twoInFront  = boardTiles[row+(2*moveDirection)][col]; 

        //Need to handle edge cases for pawns on column 0 and 7
        Tile attackLeft = boardTiles[row+(1*moveDirection)][col-1];
        Tile attackRight = boardTiles[row+(1*moveDirection)][col+1];

        //Same as above
        Tile passantLeft = boardTiles[row][col-1];
        Tile passantRight = boardTiles[row][col+1];

        if (!inFront.isOccupied()) {
            legalPawnMoves.add(new int[]{inFront.getRow(), inFront.getCol()});
        }    
        
        if (!pawn.hasMoved()) {
            if (!twoInFront.isOccupied() && legalPawnMoves.size() != 0) {
                  legalPawnMoves.add(new int[]{twoInFront.getRow(), twoInFront.getCol()});
            }
        }
        
        if (attackLeft.isOccupied() && attackLeft.getPiece().getColor() != this.color) {
            legalPawnMoves.add(new int[]{attackLeft.getRow(), attackLeft.getCol()});
        }

        if (attackRight.isOccupied() && attackRight.getPiece().getColor() != this.color) {
            legalPawnMoves.add(new int[]{attackRight.getRow(), attackRight.getCol()});
        }

        // if (passantLeft.isOccupied() && passantLeft.getPiece().getColor() != this.color && passantLeft.getPiece().hasMovedTwo()){
        //     legalPawnMoves.add(new int[]{passantLeft.getRow(), passantLeft.getCol()});
        // }

        // if (passantRight.isOccupied() && passantLeft.getPiece().getColor() != this.color && passantRight.getPiece().hasMovedTwo()) {
        //     legalPawnMoves.add(new int[]{passantRight.getRow(), passantRight.getCol()});
        // } 

        return legalPawnMoves;
    }

    /*
    private ArrayList<int[]> legalRookMove(Tile tile) {
    }

    private ArrayList<int[]> legalKnightMove(Tile tile) {
    }

    private ArrayList<int[]> legalBishopMove(Tile tile) {
    }

    private ArrayList<int[]> legalKingMove(Tile tile) {
    }

    private ArrayList<int[]> legalQueenMove(Tile tile) {
    }
    asdfasdf
    

    Does this push to Erlend?
    */

    public static void main(String[] args) {
        Board testBoard = new Board();
        PlayerMove white = new PlayerMove(testBoard, 'w');
        PlayerMove black = new PlayerMove(testBoard, 'b');
        Tile[][] boardTiles = testBoard.getBoardTiles();
        Pawn testPawn = new Pawn("foo", 'w');
        Pawn testPawn2 = new Pawn("yes", 'w');
        Pawn testPawn3 = new Pawn("fri", 'w');
        Pawn testPawn4 = new Pawn("nei", 'w');
        boardTiles[5][1].setPiece(testPawn);
        //boardTiles[5][2].setPiece(testPawn2);
        boardTiles[5][3].setPiece(testPawn3);
        //boardTiles[4][2].setPiece(testPawn4);
        testBoard.printBoard();
        black.moveHandler(boardTiles[6][2]);
    }
}
