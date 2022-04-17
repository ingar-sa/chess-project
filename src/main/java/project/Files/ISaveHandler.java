package project.Files;

import java.io.IOException;

import project.Board.Tile;

public interface ISaveHandler {

    public void saveGame(String saveName, Tile[][] board, int moveNumber) throws IOException, Exception;

    public String loadGame(String saveName) throws IOException, Exception;

}
