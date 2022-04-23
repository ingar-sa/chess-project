package project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Board.Tile;
import project.Movement.Game;
import project.Movement.MovementPatterns;
import project.Pieces.Rook;

public class GameTest {
    
    private Game game;
    private MovementPatterns whiteMovement = new MovementPatterns('w');

    @BeforeEach
    public void setup() {
        //Makes a empty board 
        this.game = new Game(false);
    }
    
    //TODO; can add test for all black pices as well
    @Test
    @DisplayName("getPieceInfoFromTileTest - Tests that the returned piece info from a tile is correct")
    public void getPieceInfoFromTileTest() {
        
        //Checks that the empty board returns expected String[]
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                
                assertTrue(compareStringArrays(new String[]{row + "" + col, ""}, game.getPieceInfoFromTile(row, col)));
    
            }
            
        }
        
        this.game = new Game();
    
        String[] expectedWhitePawnString = new String[]{"10", "wP"};
        String[] expectedwhiteRookString = new String[]{"00", "wR"};
        String[] expectedwhiteKnightString = new String[]{"01", "wK"};
        String[] expectedwhiteBishopString = new String[]{"02", "wB"};
        String[] expectecWhiteQueenString = new String[]{"03", "wQ"};
        String[] expectedwhiteKingString = new String[]{"04", "wX"}; 

        String[] expectedBlackPawnString = new String[]{"67", "bP"};

        //Checks that placed pieces return expected information
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == 1 && col == 0) {
                    assertTrue(compareStringArrays(expectedWhitePawnString, game.getPieceInfoFromTile(row, col)));
                }
                else if (row == 0 && col == 0) {
                    assertTrue(compareStringArrays(expectedwhiteRookString, game.getPieceInfoFromTile(row, col)));
                }
                else if (row == 0 && col == 1) {
                    assertTrue(compareStringArrays(expectedwhiteKnightString, game.getPieceInfoFromTile(row, col)));
                }
                else if (row == 0 && col == 2) {
                    assertTrue(compareStringArrays(expectedwhiteBishopString, game.getPieceInfoFromTile(row, col)));
                }
                else if (row == 0 && col == 3) {
                    assertTrue(compareStringArrays(expectecWhiteQueenString, game.getPieceInfoFromTile(row, col)));
                }
                else if (row == 0 && col == 4) {
                    assertTrue(compareStringArrays(expectedwhiteKingString, game.getPieceInfoFromTile(row, col)));
                }
                else if (row == 6 && col == 7) {
                    assertTrue(compareStringArrays(expectedBlackPawnString, game.getPieceInfoFromTile(row, col)));
                }
            }        
        }
    }

    @Test
    @DisplayName("getLegalMovesTest")
    public void getLegalMovesTest() {
        
        //Tests that coordinates outside the board are illegal - this tests the private method validationOfCoordinates
        for (int row = - 50; row < 50; row++) {
            for (int col = - 50; col < 50; col++) {
                int rowNow = row;
                int colNow = col;

                if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                    continue;
                }
                else {
                    assertThrows(IllegalArgumentException.class,
                    () -> game.getLegalMoves(rowNow, colNow));
                }
            }
        }

    
        this.game = new Game();

        
        //Checks that the tiles without a piece returns a empty ArrayList
        for (int row = 2; row < 7; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(true, game.getLegalMoves(row, col).isEmpty());
            }
        } 

        //Tests that a white horse returns expected string from start position
        ArrayList<String> expectedArrayListStringLeftwK = new ArrayList<>(Arrays.asList("20", "22"));

        assertEquals(expectedArrayListStringLeftwK, game.getLegalMoves(0, 1));
        
        //Loads inn specific game position
        game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6");

        //Tests that the white king returns the expected string from a generated position
        ArrayList<String> expectedArrayListStringwX = new ArrayList<>(Arrays.asList("14", "03", "05"));

        assertEquals(expectedArrayListStringwX, game.getLegalMoves(0, 4));

    }

    @Test
    @DisplayName("isMoveEnPassentTest")
    public void isMoveEnPassentTest() {
        
        //Start position
        this.game = new Game();

        //Loads position where en passent can be preformed for white
        this.game.loadedGamePiecesPosition("wR=0-wK-wB-wQ-wX=0-wB-wK-wR=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-wP=1=0=2-bP=1=1=3-00-00-00-00-00-bP=1=1=1-00-00-00-00-00-00-00-00-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-4");

        String ExpectedStringEnPassent1 = "41"; 
        assertEquals(ExpectedStringEnPassent1, this.game.isMoveEnPassent(4, 0, 5, 1));

        


    }

    private boolean compareStringArrays(String[] expected, String[] actual) {
        
        if (expected[0].equals(actual[0]) && expected[1].equals(actual[1])) {
            return true;
        }
        return false;
    }



}

