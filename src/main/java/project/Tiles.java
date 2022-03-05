package project;
import project.pieces.*;

/**
 * Spot
 */
public class Tile {

    private Piece piece;
    private int x;
    private int y;
    private char color; 
    private boolean occupied;
    
    
    public Tile(int x, int y, char color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }


    

    /* 
    * GETTERS AND SETTERS
    */    
    
    public Piece getPiece() {
        return piece;    
    }


    public void setPiece(Piece piece) {
        this.piece = piece;
    }


    public int getX() {
        return x;
    }


    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }


    public void setY(int y) {
        this.y = y;
    }


    public boolean isOccupied() {
        return occupied;
    }


    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

}
    
    
