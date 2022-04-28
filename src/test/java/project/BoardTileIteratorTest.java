package project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        for (int i = 0; i < 65; ++i) {
            try {
                tileIterator.next();
            } catch (NoSuchElementException e) {
                assertEquals(64, i);
            }
        }
    }
}
