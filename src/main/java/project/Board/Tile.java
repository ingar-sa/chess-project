package project.Board;

import java.io.Serializable;

import project.Pieces.*;

public class Tile implements Serializable {

    private Piece piece;
    private int row;
    private int col;
    private boolean occupied;

    public Tile(int row, int col) {
        if (checkBounds(row))
            this.row = row;
        else
            throw new IllegalArgumentException("The row has to be between 0-7");

        if (checkBounds(col))
            this.col = col;
        else
            throw new IllegalArgumentException("The col has to be between 0-7");
    }

    private boolean checkBounds(int rowOrCol) {
        return (rowOrCol >= 0 && rowOrCol < 8);
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.occupied = (piece != null) ? true : false;
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
        this.occupied = false;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public int[] getCoordinates() {
        return new int[] { this.row, this.col };
    }

    public String coordinatesToString() {
        return (this.row + "" + this.col);
    }
}
