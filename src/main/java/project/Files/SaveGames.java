package project.Files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class SaveGames implements Serializable {
    
	public void saveGame(String saveName, String saveDataString) {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/project/Files/savegames/" + saveName + ".txt"))) {
			writer.write(saveDataString);
		} catch (IOException e) {
			System.err.println("An IO-exception occurred.");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("An unknown error has occurred.");
		}
	}
    
	public String loadGame(String saveName) {
		// //TODO: try catch
		// File saveFile = new File("src/main/java/project/Files/savegames/" + saveName + ".txt");
		// Scanner loadGameScanner = new Scanner(saveFile);
		// String saveData = new String();

		// while(loadGameScanner.hasNextLine()) {
		// 	saveData += loadGameScanner.nextLine();
		// 	System.out.println(saveData);
		// }
		
		// loadGameScanner.close();
		// return saveData;
		try {
		Path fileName = Path.of(saveName + ".txt");
	    String actual = Files.readString(fileName);
	    System.out.println(actual);
		return actual;
		} catch (IOException e) {
			System.err.println("IOexception.");
			e.printStackTrace();
		}

		return "";
	}

}