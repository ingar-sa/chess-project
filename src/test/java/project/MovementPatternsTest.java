package project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Board.Tile;
import project.Movement.Game;
import project.Movement.MovementPatterns;

public class MovementPatternsTest {
    
    private Game game;
    private Tile[][] boardTiles;


    @BeforeEach
    public void setup() {
        game = new Game();
        boardTiles = game.getBoardDeepCopyUsingSerialization();
    }


    @Test
    @DisplayName("Pawn test")
    public void pawnTest() {
        
    }

    @Test
    @DisplayName("King test")
    public void kingTest() {
        
    }

    @Test
    @DisplayName("Knight test")
    public void knightTest() {
        
    }

    @Test
    @DisplayName("Bishop test")
    public void bishopTest() {
        
    }

    @Test
    @DisplayName("Rook test")
    public void rookTest() {
        
    }

    @Test
    @DisplayName("Queen test")
    public void queenTest() {
        
    }

    

}
