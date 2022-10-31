package project.Pieces;

public class Pawn extends Piece {

    private boolean movedTwoLastTurn = false;
    private int moveNumberEnPassant;
    
    public Pawn (String name, char color) {
        super(name, color, 'P');
    }

    public boolean getMovedTwoLastTurn() {
        return this.movedTwoLastTurn;
    }

    public void setMovedTwoLastTurn(boolean movedTwoLastTurn) {
        this.movedTwoLastTurn = movedTwoLastTurn;
    }

    public void setMoveNumberEnPassant(int moveNumberEnPassant) {
        if (moveNumberEnPassant >= 0) 
            this.moveNumberEnPassant = moveNumberEnPassant;
        else 
            throw new IllegalArgumentException("The value must be zero or positive, but was " + moveNumberEnPassant);        
    }

    public int getMoveNumberEnPassant() {
         return this.moveNumberEnPassant;
    }
}
