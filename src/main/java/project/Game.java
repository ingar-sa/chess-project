package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import project.Board.Chessboard;
import project.Board.Tile;
import project.Movement.TileCheckLegalMoves;
import project.Pieces.Piece;

public class Game {
    
    Chessboard chessboard;
    TileCheckLegalMoves checkLegalMoves;
    Tile[][] currentGamePositionTiles;

    public Game() {
        this.chessboard               = new Chessboard();        //Only necessary for printing board 
        this.currentGamePositionTiles = chessboard.getBoardTiles();
        this.checkLegalMoves          = new TileCheckLegalMoves(currentGamePositionTiles);  
    }

    public ArrayList<String> getLegalMoves(int row, int col) {

        HashMap<Tile, ArrayList<int[]>> allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat();
        Tile tilePieceToMoveIsOn = currentGamePositionTiles[row][col]; 
        Piece pieceToMove = this.currentGamePositionTiles[row][col].getPiece();
        ArrayList<int[]> legalMovesForPieceToMove = allLegalMovesAfterControl.get(tilePieceToMoveIsOn);
        Set<Tile> allPiecesThatCanMove = allLegalMovesAfterControl.keySet();
        
        boolean legalPiece = false;

        for (Tile piece : allPiecesThatCanMove) {
            if (piece == tilePieceToMoveIsOn && allLegalMovesAfterControl.get(tilePieceToMoveIsOn).size() != 0) {
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

    public void updateBoard(int chosenPieceRow, int chosenPieceCol, int moveToPieceRow, int moveToPieceCol) {
        Piece pieceToMove = currentGamePositionTiles[chosenPieceRow][chosenPieceCol].getPiece();
        this.currentGamePositionTiles[chosenPieceRow][chosenPieceCol].removePiece();
        this.currentGamePositionTiles[moveToPieceRow][moveToPieceCol].setPiece(pieceToMove);
        checkLegalMoves.increaseMoveNumber();
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
