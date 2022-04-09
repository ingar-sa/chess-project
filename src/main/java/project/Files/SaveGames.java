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
		
		String saveData = new String();

		try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/project/Files/savegames/" + saveName + ".txt"))) {
			saveData = reader.readLine();
		} catch (IOException e) {
			System.err.println("An IOEexception has occurred.");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("An unknown error has occurred.");
			e.printStackTrace();
		}

		return saveData;
	}
}