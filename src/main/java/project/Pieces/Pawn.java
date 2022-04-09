package project.Pieces;

public class Pawn extends Piece {

    //overflødig? - movedtwo 
    //private boolean movedTwo = false;
    //Endre navnet til kun has movedTwo
    private boolean movedTwoLastTurn = false;
    private int moveNumberEnPassant;
    
    public Pawn (String name, char color) {
        super(name, color, 'P');
    }

    public boolean getMovedTwoLastTurn() {
        return movedTwoLastTurn;
    }

    public void setMovedTwoLastTurn(boolean movedTwoLastTurn) {
        this.movedTwoLastTurn = movedTwoLastTurn;
    }

    public void setMoveNumberEnPassant(int moveNumberEnPassant) {
        if (moveNumberEnPassant >= 0) {
            this.moveNumberEnPassant = moveNumberEnPassant;
        } 
        else {
            System.err.println("Illegal value for moveNumberEnPassant!");
        }
    }

    public int getMoveNumberEnPassant() {
         return this.moveNumberEnPassant;
    }

    public static void main(String[] args) {
        Pawn testPawn = new Pawn("pawn", 'w');
        testPawn.setColor('c');
    }
}
