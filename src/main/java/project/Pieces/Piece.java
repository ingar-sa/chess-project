package project.Pieces;

import java.io.Serializable;

public abstract class Piece implements Serializable {
    
    private String  name;
    private String  spriteId;
    private char    color;
    private boolean hasMoved = false;

    protected Piece (String name,  char color, char spriteType) {
        this.name = name;
        if (color == 'w' || color == 'b') 
            this.color = color;
        else
            throw new IllegalArgumentException("Color must be w or b");
        
        //spriteType is not a parameter in the subclasses, and is passed anonymously by their
        //constructors, ensuring that it is always correct
        this.spriteId = color + "" + spriteType; 
    }

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


