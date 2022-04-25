package project;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Movement.CheckLegalMoves;
import project.Movement.Game;
import project.Files.SaveBoardState;

public class SaveBoardStateTest {
    
	private Game game = new Game();
	private SaveBoardState saveBoard = new SaveBoardState(true);
	private CheckLegalMoves checkLegalMoves = new CheckLegalMoves();

	@Test
	@DisplayName("Test that a file in the correct format loads")
	public void testCorrectFile() {
		String correctFormatString = loadFile("correct-formatE");
		assertDoesNotThrow(() -> game.loadedGamePiecesPosition(correctFormatString));
	}

	@Test
	@DisplayName("Tests that an incorrectly formatted string throws exception")
	public void testIncorrectFile() {
		String incorrectFormatString = loadFile("incorrect-format");
		assertThrows(IllegalArgumentException.class, () -> game.loadedGamePiecesPosition(incorrectFormatString));

	}

	

	private String loadFile(String filename) {
		String loadedString = "";
		try {
			loadedString = saveBoard.loadGame(filename);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("No such file exists");
		}
		catch (IOException e) { 
			e.printStackTrace();
			fail("An IOException occurred");
		}

		return loadedString;
	}

	
/*
	@AfterAll
	static void teardown() {
		File newTestSaveFile = new File(SaveHandler.getFilePath("test-save-new"));
		newTestSaveFile.delete();
	}
*/
	

}
