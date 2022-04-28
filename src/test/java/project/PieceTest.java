package project;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Pieces.King;

//Piece is an abstract class, so tests must be performed on a subclass object.
//All of the subclasses have identical constructors, so which one we perform the test
//on is arbitrary
public class PieceTest {
    
    @Test
    @DisplayName("Test color validation in the constructor.")
    public void testColorValidation() {
        //Tests the entire char value range [0, 65535]
        assertThrows(IllegalArgumentException.class, () -> {
                for (char c = 0; c < 65536; ++c) {
                    if (c != 'w' || c != 'b')
                        new King("King", c);
                }
            });     
        
        assertDoesNotThrow(() -> {
            new King("King", 'w');
        });

        assertDoesNotThrow(() -> {
            new King("King", 'b');
        });
    }
}

