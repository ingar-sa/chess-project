package project;

import java.util.Iterator;
import java.util.NoSuchElementException;

import project.Board.Tile;
import project.Game;

public class BoardTileIterator implements Iterator<String[]> {

    private Game game;
    private int row, col = 0;

    public BoardTileIterator(Game game) {
        this.game = game;
    }

    @Override
    public boolean hasNext() {
        return (!(row == 7 && col == 8));
    }

    @Override
    public String[] next() {  //TODO: Change from tile to give pieceinfo array directly from game

        if (!hasNext())
            throw new NoSuchElementException("The iterator is empty.");
        
        if (col == 8) {
            col = 0;
            ++row;
        }

        String[] pieceInfo = game.getPieceInfoFromTile(row, col);
        
        ++col;
        return pieceInfo;
    }
    
    public static void main(String[] args) {
    //     Game game = new Game();
    //     BoardTileIterator iterator = new BoardTileIterator(game.getBoardTiles());

    //     iterator.manipulator();
    //     System.out.println("yoyo");
    }
}
