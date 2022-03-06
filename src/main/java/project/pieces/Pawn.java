package project.pieces;

import project.Piece;


/*

*/

public class Pawn extends Piece {

    private boolean movedTwo = false;
    
    public Pawn (String name, char color) {
        super(name, color);
    }

    
    public boolean isMovedTwo() {
        return this.movedTwo;
    }

    public void setMovedTwo(boolean movedTwo) {
        this.movedTwo = movedTwo;
    }



    public static void main(String[] args) {
        Pawn testPawn = new Pawn("pawn", 'w');
        testPawn.setColor('c');
    }
}
