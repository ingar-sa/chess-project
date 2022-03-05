package project;
import project.pieces.*;


public class Tile {

    private Piece piece;
    private int row;
    private int col;
    private char color; 
    private boolean occupied;
    
    
    public Tile(int row, int col, char color) {
        this.row = row;
        this.col = col;
        this.color = color;
    }


    public Piece getPiece() {
        return this.piece;
    }


    public void setPiece(Piece piece) {
        this.occupied = true;
        this.piece = piece;
    }


    public int getRow() {
        return this.row;
    }


    public void setRow(int row) {
        this.row = row;
    }


    public int getCol() {
        return this.col;
    }


    public void setCol(int col) {
        this.col = col;
    }


    public char getColor() {
        return this.color;
    }


    public void setColor(char color) {
        this.color = color;
    }


    public boolean isOccupied() {
        return this.occupied;
    }


    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    
    @Override
    public String toString() {
        return(this.row + ", " + this.col);
    }
}
    
    
