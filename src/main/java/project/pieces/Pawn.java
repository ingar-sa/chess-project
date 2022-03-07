package project.pieces;

import project.Piece;


/*

*/

public class Pawn extends Piece {

    private boolean movedTwo = false;
    //private boolean movedTwoLastTurn = false;
    //private int moveNumber;
    
    public Pawn (String name, char color) {
        super(name, color);
    }

    public boolean hasMovedTwo() {
        return this.movedTwo;
    }
    
    public void setMovedTwo() {
        this.movedTwo = true;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int getMoveNumber() {
        return this.moveNumber;
    }



    public static void main(String[] args) {
        Pawn testPawn = new Pawn("pawn", 'w');
        testPawn.setColor('c');
    }
}
