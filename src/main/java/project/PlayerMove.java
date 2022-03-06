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

    public void moveHandler() {
        //This method calls all the other methods needed to check
        //for valid moves
    }

    private int[] getTile() {
        return 
    }

    private void legalPawnMove(Tile tile) {
        
    }

    public static void main(String[] args) {
        Board testBoard = new Board();
        PlayerMove white = new PlayerMove(testBoard, 'w');
        PlayerMove black = new PlayerMove(testBoard, 'b');
        Pawn testPawn = new Pawn("foo", 'w');

        Tile[][] whiteTiles = white.board.getBoardTiles();
        whiteTiles[3][4].setPiece(testPawn);

        black.board.printBoard();
        
        

        //Game
        
        
    }
}
