package project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import project.Board.Chessboard;
import project.Board.Tile;
import project.Files.SaveGame;
import project.Movement.TileCheckLegalMoves;
import project.Pieces.Piece;

public class Game implements Serializable {
    
    Chessboard                      chessboard;
    TileCheckLegalMoves             checkLegalMoves;
    Tile[][]                        currentGamePositionTiles;
    HashMap<Tile, ArrayList<int[]>> allLegalMovesAfterControl;

    public Game() {
        this.chessboard                = new Chessboard();        //Only necessary for printing board 
        this.currentGamePositionTiles  = chessboard.getBoardTiles();
        this.checkLegalMoves           = new TileCheckLegalMoves(currentGamePositionTiles);
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(); //allLegalMovesAfterControl is initialized with whites available moves
    }

    public int isGameOver() {
        return checkLegalMoves.getGameStatus();
    }

    public ArrayList<String> getLegalMoves(int row, int col) {

        //Piece pieceToMove                                       = this.currentGamePositionTiles[row][col].getPiece();
        Tile             tilePieceToMoveIsOn       = currentGamePositionTiles[row][col]; 
        ArrayList<int[]> legalMovesForPieceToMove  = this.allLegalMovesAfterControl.get(tilePieceToMoveIsOn);
        Set<Tile>        allPiecesThatCanMove      = this.allLegalMovesAfterControl.keySet();
        boolean          pieceCanMove              = false;

        for (Tile piece : allPiecesThatCanMove) {
            if (piece == tilePieceToMoveIsOn 
                && this.allLegalMovesAfterControl.get(tilePieceToMoveIsOn).size() != 0) {
                    
                    pieceCanMove = true;
            }
        }

        ArrayList<String> coordinateString = new ArrayList<String>();
        
        if (!pieceCanMove) {
            return coordinateString; //The controller ignores the user input if the arraylist is empty
        }

        for (int[] legalMove : legalMovesForPieceToMove) {
            coordinateString.add(legalMove[0] + "" + legalMove[1]); //This string representing the coordinates allows us to fetch imageviews by id
        }
        
        return new ArrayList<String>(coordinateString);
    }

    public int updateGameState(int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int pieceToMoveCol) {
        
        Piece pieceToMove = currentGamePositionTiles[chosenPieceRow][chosenPieceCol].getPiece();
        pieceToMove.setHasMoved(true);
        
        this.currentGamePositionTiles[chosenPieceRow][chosenPieceCol].removePiece();
        this.currentGamePositionTiles[moveToPieceRow][pieceToMoveCol].setPiece(pieceToMove);
        
        checkLegalMoves.increaseMoveNumber();

        //allLegalMovesAfterControl is updated with the opposite player's moves here, as opposed to updating it 
        //during the next call of getLegalMoves. This allows us to use getGameStatus() to check for a mate or pat
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat(); 
        
        int gameStatus = checkLegalMoves.getGameStatus(); //gameStatus returns 1 if it is pat, 2 if it is check mate, and otherwise 0

        if (gameStatus == Consts.PAT) {
            return Consts.PAT;
        }
        else if (gameStatus == Consts.CHECKMATE) {
            return (checkLegalMoves.getMoveNumber() % 2 == 0) ? Consts.CHECKMATE_FOR_BLACK : Consts.CHECKMATE_FOR_WHITE;  //When movenumber is even, black has check mate, when it's odd, white has check mate. 
        }
        else {
            return Consts.GAME_NOT_OVER;
        }
    }

    public HashMap loadedGamePiecesPosition() {

        HashMap<String, String> piecePositions = new HashMap<>();

        int rowNumber = 0;
        for (Tile[] row : currentGamePositionTiles) {
            int colNumber = 0;
            for (Tile tile : row) {
                piecePositions.put(rowNumber + "" + colNumber, tile.getPiece().getSpriteId());
                ++colNumber;
            }

            ++rowNumber;
        }

        return piecePositions;
    }

    public static void main(String[] args) {
        
    }

}
