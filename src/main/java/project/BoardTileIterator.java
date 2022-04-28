package project;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
    public String[] next() {

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
}
