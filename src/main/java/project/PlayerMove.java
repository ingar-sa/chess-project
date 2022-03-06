package project;

import java.util.ArrayList;

import project.pieces.Bishop;
import project.pieces.King;
import project.pieces.Knight;
import project.pieces.Pawn;
import project.pieces.Queen;
import project.pieces.Rook;


public class PlayerMove {

    public void checkAvailableMoves(Tile tileFrom) {
        
    }
    
    public void playerMove(Tile tileFrom, Tile tileTo) {
        Piece piece = tileFrom.getPiece();
        tileTo.setPiece(piece);
        tileFrom.setPiece(null);

        switch (piece) {
            case piece.equals():
                
                break;
        
            default:
                break;
        }
    }

    public void legalPawnMove(Tile tileFrom, ArrayList<ArrayList<Tile>> chessBoard) {
        Piece piece = tileFrom.getPiece();
        int row = tileFrom.getRow();
        int col = tileFrom.getCol();
        int maxMoveDistance = 1;


        if (piece.getHasMoved()) {

        }
        if (!piece.getHasMoved()) {
            maxMoveDistance = 2;
            chessBoard.get(row + 1).get(col);
            chessBoard.get(row + 2).get(col);
        }
        
    }

    public static void main(String[] args) {
        Board testBoard = new Board();
        PlayerMove testMover = new PlayerMove();
        testBoard.printBoard();

        /*Tile tileFrom = testBoard.getBoardTiles().get(0).get(0);
        Tile tileTo = testBoard.getBoardTiles().get(2).get(4);
        testMover.playerMove(tileFrom, tileTo);
        testBoard.printBoard();
        */

        Piece piece;
        Pawn pawn = new Pawn("foo", 'w');

        piece = pawn;
        System.out.println(piece.getClass());

    }
}
