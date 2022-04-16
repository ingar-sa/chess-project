package project.Files;

import java.io.IOException;

import project.Board.Tile;

public interface ISaveHandler {

    public void saveGame(String saveName, Tile[][] board, int moveNumber) throws IOException, Exception;

    public String loadGame(String saveName) throws IOException, Exception;


    //  SaveGames saveGameWriter = new SaveGames();
    //     String saveGameData = new String();

    //     for (Tile[] row : boardTiles) {
    //         for (Tile tile : row) {
    //             if (tile.isOccupied()) {
                    
    //                 Piece piece = tile.getPiece();
                    
    //                 if (piece instanceof Pawn) {
    //                     Pawn pawn = (Pawn)piece;                

    //                     saveGameData += pawn.getSpriteId() + "=";
    //                     saveGameData += ((pawn.getHasMoved()) ? "1" : "0") + "=";
    //                     saveGameData += ((pawn.getMovedTwoLastTurn()) ? "1" : "0") + "=";
    //                     saveGameData += pawn.getMoveNumberEnPassant();
    //                 }
    //                 else if (piece instanceof Rook || piece instanceof King) {

    //                     saveGameData += piece.getSpriteId() + "=";
    //                     saveGameData += ((piece.getHasMoved()) ? "1" : "0");
    //                 }
    //                 else {
    //                     saveGameData += piece.getSpriteId();
    //                 }

    //                 saveGameData += "-";
    //             }
    //             else {
    //                 saveGameData += "00-";
    //             }
    //         }
    //     }

    //     saveGameData += checkLegalMoves.getMoveNumber();
        
    //     saveGameWriter.saveGame(saveName, saveGameData);

    //     System.out.println(saveGameData);
}
