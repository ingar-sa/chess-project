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
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Queen;
import project.Pieces.Rook;
import project.Board.Tile;
import project.Files.SaveBoardState;

public class SaveBoardStateTest {
    
	private Game game = new Game();
	private SaveBoardState saveBoard = new SaveBoardState(true);
	private CheckLegalMoves checkLegalMoves = new CheckLegalMoves();

	@BeforeEach
	private void resetBoard() {
		game = new Game();
	}

	@Test
	@DisplayName("Test that the method can save an illegal position")
	public void saveIllegalPosition() {
		Tile[][] boardTiles = game.getBoardTilesDeepCopy();
		boardTiles[4][4].setPiece(new King("wX1", 'w'));

		assertDoesNotThrow(() ->  saveBoard.saveGame("illegal-position", boardTiles, 0));
	}

	@Test
	@DisplayName("Tests that an illegal position does not load")
	public void testDoesNotLoadIllegalPosition() {
		String incorrectFormatString = loadFile("illegal-position");
		assertThrows(IllegalArgumentException.class, () -> game.loadedGamePiecesPosition(incorrectFormatString));
	}


	@Test
	@DisplayName("Test that a file in the correct format loads")
	public void testCorrectFormat() {
		String correctFormatString = loadFile("correct-format");
		assertDoesNotThrow(() -> game.loadedGamePiecesPosition(correctFormatString));
	}

	@Test
	@DisplayName("Tests that an incorrectly formatted string throws exception")
	public void testIncorrectFormat() {
		String incorrectFormatString = loadFile("incorrect-format");
		assertThrows(IllegalArgumentException.class, () -> game.loadedGamePiecesPosition(incorrectFormatString));
	}

	@Test
	@DisplayName("Test that the save string is generated properly")
	public void testSave() {
		String expectedString = "00-00-00-00-00-00-00-00-00-00-00-00-wX=1-00-00-00-00-00-00-00-00-00-00-wK-00-bK-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-wQ-00-00-00-00-00-00-00-00-00-00-00-00-00-00-bX=0-00-00-bR=0-10037";
		Tile[][] boardTiles = game.getBoardTilesDeepCopy();
		
		for (Tile[] row : boardTiles) {
			for (Tile tile : row) {
				tile.setPiece(null);
			}
		}

		boardTiles[3][1].setPiece(new Knight("bK", 'b'));
		boardTiles[7][4].setPiece(new King("bX", 'b'));
		boardTiles[7][7].setPiece(new Rook("bR", 'b'));

		King whiteKing = new King("wX", 'w');
		whiteKing.setHasMoved(true);
		boardTiles[1][4].setPiece(whiteKing);
		boardTiles[2][7].setPiece(new Knight("wK", 'w'));
		boardTiles[5][5].setPiece(new Queen("wQ", 'w'));
		saveFile("proper-generation", boardTiles, 10037);

		assertEquals(expectedString, loadFile("proper-generation"));
		
	}

	private void saveFile(String filename, Tile[][] boardTiles, int movenumber) {
		try {
			saveBoard.saveGame(filename, boardTiles, movenumber);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("No such file exists");
		}
		catch (IOException e) { 
			e.printStackTrace();
			fail("An IOException occurred");
		}

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
