package project.Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import project.Board.Tile;
import project.Pieces.King;
import project.Pieces.Pawn;
import project.Pieces.Piece;
import project.Pieces.Rook;

public class SaveBoardState implements ISaveHandler {

	 /*  SAVE INFO STRING STRUCTURE
                                 (w/b)Char       (1 or 0)         (1 or 0)               Positive int
        Pawn(P):            color and piece type=has moved=moved two spaces last turn=en passen move number-
        King or rook(X/R):  color and piece type=has moved-
        Other piece(Q/K/B): color and piece type-
        Empty tile:         00-
    */

	private String folderPath;

	public SaveBoardState() {
		String separator = System.getProperty("file.separator"); //Gets correct filepath separator for the OS
		folderPath = String.format("src%1$smain%1$sjava%1$sproject%1$sFiles%1$ssavegames%1$s", separator);
	}

	//Made this to avoid having to rewrite existing code
	public SaveBoardState(boolean useForTest) {
		if (!useForTest) 
			throw new IllegalArgumentException("Use constructor with no parameter");

		String separator = System.getProperty("file.separator"); //Gets correct filepath separator for the OS
		folderPath = String.format("src%1$smain%1$sjava%1$sproject%1$sFiles%1$stestsaves%1$s", separator);
	}

	@Override
	public void saveGame(String saveName, Tile[][] chessboard, int moveNumber) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFile(saveName)))) { //(new FileWriter("src/main/java/project/Files/savegames/" + saveName + ".txt"))) {

			String saveGameData = new String();

			for (Tile[] row : chessboard) {
				for (Tile tile : row) {
					if (tile.isOccupied()) {
						
						Piece piece = tile.getPiece();
						
						if (piece instanceof Pawn) {
							Pawn pawn = (Pawn)piece;                

							saveGameData += pawn.getSpriteId() + "=";
							saveGameData += ((pawn.getHasMoved()) ? "1" : "0") + "=";
							saveGameData += ((pawn.getMovedTwoLastTurn()) ? "1" : "0") + "=";
							saveGameData += pawn.getMoveNumberEnPassant();
						}
						else if (piece instanceof Rook || piece instanceof King) {

							saveGameData += piece.getSpriteId() + "=";
							saveGameData += ((piece.getHasMoved()) ? "1" : "0");
						}
						else {
							saveGameData += piece.getSpriteId();
						}
					}
					else {
						saveGameData += "00";
					}

					saveGameData += "-";
				}
			}
			

			saveGameData += moveNumber;
			writer.write(saveGameData);
		}
	}

	@Override
	public String loadGame(String saveName) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(getFile(saveName)))) {
			String saveData = new String();
			saveData = reader.readLine();
			return saveData;
		}
	}
		
	public String getFile(String filename) {
		String filePath = folderPath + filename + ".txt";
		return filePath;
		// return SaveBoardState.class.getResource("saves/").getFile() + filename + ".txt";
	}
	
}