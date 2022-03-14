package project.Pieces;

/*

*/

public class Pawn extends Piece {

    //overflødig? - movedtwo 
    //private boolean movedTwo = false;
    private boolean movedTwoLastTurn = false;
    //private int moveNumber;
    
    public boolean getMovedTwoLastTurn() {
        return movedTwoLastTurn;
    }

    public void setMovedTwoLastTurn(boolean movedTwoLastTurn) {
        this.movedTwoLastTurn = movedTwoLastTurn;
    }

    public Pawn (String name, char color) {
        super(name, color);
    }


    // public void setMoveNumber(int moveNumber) {
    //     this.moveNumber = moveNumber;
    // }

    // public int getMoveNumber() {
    //     return this.moveNumber;
    // }



    public static void main(String[] args) {
        Pawn testPawn = new Pawn("pawn", 'w');
        testPawn.setColor('c');
    }
}
