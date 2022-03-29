package project.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class SaveGame implements Serializable {
    

    private static final long serialVersionUID = 1L;

    public Object ReadObjectFromFile(String filepath) {

        Object obj = new Object();

		try {

			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			obj = objectIn.readObject();

			System.out.println("Game loaded!");
			objectIn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

        return obj;
	}

    public void WriteObjectToFile(Object serObj, String filepath) {

		try {

			FileOutputStream fileOut = new FileOutputStream(new File(filepath));
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(serObj);
			objectOut.close();
			System.out.println("Game saved!");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}