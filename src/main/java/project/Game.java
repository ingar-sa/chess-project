package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import project.Board.Chessboard;
import project.Board.Tile;
import project.Movement.CheckLegalMoves;
import project.Movement.TileCheckLegalMoves;
import project.Pieces.Piece;

public class Game {
    
    Chessboard chessboard;
    TileCheckLegalMoves checkLegalMoves;
    Tile[][] currentGamePositionTiles;
    HashMap<Tile, ArrayList<int[]>> allLegalMovesAfterControl;

    public Game() {
        this.chessboard                = new Chessboard();        //Only necessary for printing board 
        this.currentGamePositionTiles  = chessboard.getBoardTiles();
        this.checkLegalMoves           = new TileCheckLegalMoves(currentGamePositionTiles);
        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat();
    }

    public int isGameOver() {
        return checkLegalMoves.getGameStatus();
    }

    public ArrayList<String> getLegalMoves(int row, int col) {

        //Piece pieceToMove                                       = this.currentGamePositionTiles[row][col].getPiece();
        Tile             tilePieceToMoveIsOn       = currentGamePositionTiles[row][col]; 
        ArrayList<int[]> legalMovesForPieceToMove  = this.allLegalMovesAfterControl.get(tilePieceToMoveIsOn);
        Set<Tile>        allPiecesThatCanMove      = this.allLegalMovesAfterControl.keySet();
        boolean          legalPiece                = false;

        for (Tile piece : allPiecesThatCanMove) {
            if (piece == tilePieceToMoveIsOn && this.allLegalMovesAfterControl.get(tilePieceToMoveIsOn).size() != 0) {
                    legalPiece = true;
            }
        }

        ArrayList<String> coordinateString = new ArrayList<String>();
        
        if (!legalPiece) {
            return coordinateString;
        }

        for (int[] legalMove : legalMovesForPieceToMove) {
            coordinateString.add(legalMove[0] + "" + legalMove[1]);
        }
        
        return new ArrayList<String>(coordinateString);
    }

    public int updateGameState(int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {
        Piece pieceToMove = currentGamePositionTiles[chosenPieceRow][chosenPieceCol].getPiece();
        
        pieceToMove.setHasMoved(true);
        
        this.currentGamePositionTiles[chosenPieceRow][chosenPieceCol].removePiece();
        this.currentGamePositionTiles[moveToPieceRow][moveToPieceCol].setPiece(pieceToMove);
        
        checkLegalMoves.increaseMoveNumber();

        this.allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat();

        int gameStatus = checkLegalMoves.getGameStatus(); //gameStatus returns 1 if it is pat, 2 if it is check mate, and else 0
        if (gameStatus == 1) {
            return 0;
        }
        else if (gameStatus == 2) {
            return (checkLegalMoves.getMoveNumber() % 2 == 0) ? 1 : 2;  //If movenumber is even, black has check mate. Otherwise white has check mate. 
        }
        
        return -1;
    }
        

    public static void main(String[] args) {
                
        String legalMovesString = "";
        ArrayList<int[]> test = new ArrayList<int[]>() {
            {
                add(new int[] {1, 2});
                add(new int[] {4, 3});
            }
        };




        int[] yo= new int[] {1, 2};
        test.add(yo);
        for (int[] legalMove : test) {
            legalMovesString += legalMove[0];
            legalMovesString += legalMove[1];
        }

        System.out.println(legalMovesString);
        
    }

}
