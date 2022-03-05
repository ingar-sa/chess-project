package project.pieces;

import project.Piece;

public class Pawn extends Piece {
    
    public Pawn (String name, char color) {
        super(name, color);
    }

    public static void main(String[] args) {
        Pawn testPawn = new Pawn("pawn", 'w');
        testPawn.setColor('c');
    }
}
