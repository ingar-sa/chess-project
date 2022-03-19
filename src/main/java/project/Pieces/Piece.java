package project.Pieces;

public class Piece {
    
    private String name;
    private char pieceType;
    private char color;
    private boolean captured = false;
    private boolean hasMoved = false;
    protected int moveNumber;


    protected Piece (String name,  char color) {
        this.name = name;
        //this.pieceType = pieceType;
        this.color = color;
    }

    public void capture() {
        this.captured = true;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    
    public boolean getHasMoved() {
        return this.hasMoved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public char getPieceType() {
        return pieceType;
    }

    public void setPieceType(char pieceType) {
        this.pieceType = pieceType;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }    
}


