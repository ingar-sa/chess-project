package project;

import java.util.Iterator;

import project.Board.Tile;

public class BoardTileIterator implements Iterator<String[]> {

    private Game game;
    private int row, col = 0;

    BoardTileIterator(Game game) {
        this.game = game;
    }

    @Override
    public boolean hasNext() {
        return (row < 8);
    }

    @Override
    public String[] next() { //TODO: Change from tile to give pieceinfo array directly from game
     
        String[] pieceInfo = new String[2];
        
        while (row < 8) {
            if (col == 8) {
                col = 0;
                ++row;
            }

            Tile tile = game.getTile(row, col);
            if (tile.isOccupied()) {
                pieceInfo = new String[] {tile.coordinatesToString(), tile.getPiece().getSpriteId()};
                ++col;
                break;
            }

            ++col;
        }

        return pieceInfo; //TODO: error handling if empty
    }
    
    // //Just for testing manipulation of Tile[][]
    // public void manipulator () {
    //     for (Tile[] rowWithTiles : game) {
    //         rowWithTiles[0] = new Tile(0, 6, 'w');
    //     }
    // }
    
    public static void main(String[] args) {
    //     Game game = new Game();
    //     BoardTileIterator iterator = new BoardTileIterator(game.getBoardTiles());

    //     iterator.manipulator();
    //     System.out.println("yoyo");
    }
}
