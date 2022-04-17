package project.Pieces;

import java.io.Serializable;

public abstract class Piece implements Serializable {
    
    private String  name;
    private String  spriteId;
    private char    color;
    private boolean hasMoved = false;

    protected Piece (String name,  char color, char spriteType) {
        this.name = name;
        this.color = color; //Add verification for correct type
        this.spriteId = color + "" + spriteType;
    }

    //TODO: skriv valideringmetode av konstruktør og evetuelle settere!

    public String getSpriteId() {
        return this.spriteId;
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

    public char getColor() {
        return color;
    }

}


