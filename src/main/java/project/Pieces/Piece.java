package project.Pieces;

import java.io.Serializable;

public class Piece implements Serializable {
    
    private String  name;
    private String  spriteId;
    private char    pieceType;
    private char    color;
    protected int   moveNumber;
    private boolean captured = false;
    private boolean hasMoved = false;

    protected Piece (String name,  char color, char spriteType) {
        this.name = name;
        this.color = color; //Add verification for correct type
        this.spriteId = color + "" + spriteType + ".png";
    }

    public String getSpriteId() {
        return this.spriteId;
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


