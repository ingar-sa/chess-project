package project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Movement.Game;

public class BoardTileIteratorTest {

    private BoardTileIterator tileIterator;
    
    @BeforeEach
    public void setup() {
        Game game = new Game();
        tileIterator = new BoardTileIterator(game);
    }

    @Test
    @DisplayName("Test .hasNext() stops at correct bounds.")
    public void testHasNext() {
        for (int i = 0; i < 70; ++i) {
            if (i < 64) {
                Assertions.assertEquals(true, tileIterator.hasNext());
                tileIterator.next();
            }

            if (i >= 64) {
                Assertions.assertEquals(false, tileIterator.hasNext());
            }
        }
    }

    @Test
    @DisplayName("Test .next() gives correct answer")
    public void testNext() {
        /* 
            How could we test this? If we want to assertEquals, then there is going to be a shitton of them.
            
            .next() returns a String[] in the format -> ["02", "wP"] if there is a piece and ["34", ""] if it is empty
            
            Since they are strings, it is possible to use .equals to check for equality, otherwise it would have been a complete
            pain in the ass to check.

            I guess you could create an array of String[] with a premade board inside, and then check if the iterator returns the
            same result.

            ### IS THIS TRUE?
            Since the iterator runs on a game object, it is impossible for there to be edge cases such as the board being too big,
            since the board is always created on construction of the object and there are no public methods to alter the board
            ###

        */
        
        for (int i = 0; i < 65; ++i) {
            try {
                tileIterator.next();
            } catch (NoSuchElementException e) {
                assertEquals(64, i);
            }
        }
    }

}
