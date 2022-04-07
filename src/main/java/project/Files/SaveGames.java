package project.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;


public class SaveGames implements Serializable {
    
	public void saveGame(String saveName, String saveDataString) {

		try {
			FileWriter saveGameWriter = new FileWriter("savegames/" + saveName + ".txt");
			saveGameWriter.write(saveDataString);
			saveGameWriter.close();

		} catch (IOException e) {
			System.err.println("An error has occured.");
			e.printStackTrace();
		}
	}
    
	public String loadGame(String saveName) {
	
		Scanner loadGameScanner = new Scanner("savegames/" + saveName + ".txt");
		String saveData = new String();

		while(loadGameScanner.hasNextLine()) {
			saveData += loadGameScanner.nextLine();
		}
		
		loadGameScanner.close();
		return saveData;
	}

}