package project.Board;
import java.io.Serializable;

import project.Pieces.*;


public class Tile implements Serializable {

    private Piece   piece;
    private int     row;
    private int     col;
    private char    color;
    private boolean occupied;
    
    
    public Tile(int row, int col, char color) {
        this.row   = row;
        this.col   = col;
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


    public char getTileColor() {
        return this.color;
    }


    public void setTileColor(char color) {
        this.color = color;
    }


    public boolean isOccupied() {
        return this.occupied;
    }


    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    
    public void removePiece() {
        this.piece = null;
        this.occupied = false;
    }
    
    @Override
    public String toString() {
        return(this.row + ", " + this.col);
    }
}
    
    
