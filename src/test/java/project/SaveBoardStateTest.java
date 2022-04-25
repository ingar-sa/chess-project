package project;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
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

	private Tile[][] getExampleGame() {
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

		return boardTiles;
	}

    
	private Game game = new Game();
	private static SaveBoardState saveBoard = new SaveBoardState(true);
	private CheckLegalMoves checkLegalMoves = new CheckLegalMoves();

	@BeforeEach
	private void resetBoard() {
		game = new Game();
	}

	@Test
	@DisplayName("Tests that if someone saves an illegal position it will not load")
	public void testDoesNotLoadIllegalPosition() {
		Tile[][] boardTiles = game.getBoardTilesDeepCopy();
		boardTiles[4][4].setPiece(new King("wX1", 'w'));

		assertDoesNotThrow(() ->  saveBoard.saveGame("illegal-position", boardTiles, 0));

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
	@DisplayName("Tests that loading a non-existant file throws an exception")
	public void loadNonExistantFile() {
		assertThrows(FileNotFoundException.class, () -> saveBoard.loadGame("non-existant"));
	}

	@Test
	@DisplayName("Test that the save string is generated properly")
	public void testSaveString() {
		saveFile("generated", getExampleGame(), 42);
		assertEquals(loadFile("correct-generation"), loadFile("generated"));
	}

	@Test
	public void testSave() {

		saveFile("new-game", getExampleGame(), 42);

		byte[] testFile = null, newFile = null;

		try {
			testFile = Files.readAllBytes(Path.of(saveBoard.getFile("test-game")));
		} catch (IOException e) {
			fail("Could not load test file");
		}

		try {
			newFile = Files.readAllBytes(Path.of(saveBoard.getFile("new-game")));
		} catch (IOException e) {
			fail("Could not load saved file");
		}
		assertNotNull(testFile);
		assertNotNull(newFile);
		assertTrue(Arrays.equals(testFile, newFile));

	}
	
	
	@AfterAll
	static void teardown() {
		String illegalPositionPath = saveBoard.getFile("illegal-position");
		String generatedPath = saveBoard.getFile("generated");
		String newGamePath = saveBoard.getFile("new-gave");

		File illegalPosition= new File(illegalPositionPath);
		illegalPosition.delete();

		File generated = new File(generatedPath);
		generated.delete();

		File newGame = new File(newGamePath);
		newGame.delete();
	}
}
