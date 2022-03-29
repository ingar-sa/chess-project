package project.Pieces;

import java.io.Serializable;

/*

*/

public class Pawn extends Piece {

    //overflødig? - movedtwo 
    //private boolean movedTwo = false;
    private boolean movedTwoLastTurn = false;
    private int moveNumberEnPassant;
    
    public boolean getMovedTwoLastTurn() {
        return movedTwoLastTurn;
    }

    public void setMovedTwoLastTurn(boolean movedTwoLastTurn) {
        this.movedTwoLastTurn = movedTwoLastTurn;
    }

    public Pawn (String name, char color) {
        super(name, color);
    }


    public void setMoveNumberEnPassant(int moveNumberEnPassant) {
         this.moveNumberEnPassant = moveNumberEnPassant;
    }

    public int getMoveNumberEnPassant() {
         return this.moveNumberEnPassant;
    }



    public static void main(String[] args) {
        Pawn testPawn = new Pawn("pawn", 'w');
        testPawn.setColor('c');
    }
}
