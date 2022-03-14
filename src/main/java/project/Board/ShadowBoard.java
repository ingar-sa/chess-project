package project.Board;


public class ShadowBoard implements Cloneable {
    
    Tile[][] shadowTiles = new Tile[8][8];

    Chessboard shadowBoard = new Chessboard();

    public Object clone() throws CloneNotSupportedException {
        
        ShadowBoard shadowBoard2 = (ShadowBoard)super.clone();

        shadowBoard2.shadowBoard = new Chessboard();
        shadowBoard2.shadowBoard.boardTiles = shadowBoard.boardTiles; 

        return; 
    }
}
