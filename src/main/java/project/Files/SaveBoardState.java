package project.Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import project.Board.Tile;
import project.Pieces.King;
import project.Pieces.Pawn;
import project.Pieces.Piece;
import project.Pieces.Rook;

public class SaveBoardState implements ISaveHandler {

	@Override
	public void saveGame(String saveName, Tile[][] chessboard, int moveNumber) throws IOException, Exception {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/project/Files/savegames/" + saveName + ".txt"))) {

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

                    saveGameData += "-";
                }
                else {
                    saveGameData += "00-";
                }
            }
        }

		saveGameData += moveNumber;

		writer.write(saveGameData);
		}
	}

	@Override
	public String loadGame(String saveName) throws IOException, Exception {

		String saveData = new String();

		try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/project/Files/savegames/" + saveName + ".txt"))) {
			saveData = reader.readLine();
		}
		return saveData;
	}
		
		
		

}

    
	// public void saveGame(String saveName, String saveDataString) {

	// 	try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/project/Files/savegames/" + saveName + ".txt"))) {
	// 		writer.write(saveDataString);
	// 	} catch (IOException e) {
	// 		System.err.println("An IO-exception occurred.");
	// 		e.printStackTrace();
	// 	} catch (Exception e) {
	// 		System.err.println("An unknown error has occurred.");
	// 	}
	// }
    
	// public String loadGame(String saveName) {
		
	// 	String saveData = new String();

	// 	try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/project/Files/savegames/" + saveName + ".txt"))) {
	// 		saveData = reader.readLine();
	// 	} catch (IOException e) {
	// 		System.err.println("An IOEexception has occurred.");
	// 		e.printStackTrace();
	// 	} catch (Exception e) {
	// 		System.err.println("An unknown error has occurred.");
	// 		e.printStackTrace();
	// 	}

	// 	return saveData;
	// }
