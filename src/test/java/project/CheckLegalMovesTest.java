package project;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Board.Tile;
import project.Movement.CheckLegalMoves;
import project.Movement.Game;
import project.Movement.MovementPatterns;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Pawn;
import project.Pieces.Piece;
import project.Pieces.Queen;

public class CheckLegalMovesTest {

    //Key - brikke - int[]
    //Expected keys

    //for key.erlik == Expected keys:
    //

    //expectedValue = Arraylist<int[]> = [[2,1], [2,3], [4,4]]

    private Game game;
    private MovementPatterns movementPattern;
    private CheckLegalMoves checklegalmoves;

    @BeforeEach
    public void setup() {
        this.game = new Game();
        this.movementPattern = new MovementPatterns('w');
        this.checklegalmoves = new CheckLegalMoves();
    }

    @Test
    @DisplayName("Tests that only the correct pieces can move")
    public void checkLegalMovesTest() {

        game.loadedGamePiecesPosition("wR=0-wK-wB-00-00-wB-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-wX=1-00-00-00-00-00-00-00-00-00-wP=1=1=0-00-wQ-00-00-00-00-bP=1=1=3-bP=1=1=1-bP=1=1=5-00-00-00-00-bQ-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-00-00-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-00-bX=0-bB-bK-bR=0-10");
        Tile[][] board = game.getBoardTilesDeepCopy();

        ArrayList<int[]> expectedKeys = new ArrayList<int[]>(Arrays.asList(new int[]{3, 4}, new int[]{3, 6}, new int[]{2, 2}, new int[]{1, 0}, new int[]{1, 1}, new int[]{1, 2}, new int[]{1, 3}, new int[]{1, 5}, new int[]{1, 6}, new int[]{1, 7}, new int[]{0, 0}, new int[]{0, 1}, new int[]{0, 2}, new int[]{0, 5}, new int[]{0, 6}, new int[]{0, 7}));
        checklegalmoves.setMoveNumber(10);
        HashMap<int[], ArrayList<int[]>> allMoves = checklegalmoves.checkforCheckMateAndPat(board);
        ArrayList<int[]> actualKeys = new ArrayList<int[]>(allMoves.keySet());

        //Test the the ArrayList<int[]> have the same size
        assertEquals(expectedKeys.size(), actualKeys.size());

        //Sorts the lists in the same way so that they can be compared
        Collections.sort(expectedKeys, new SortByCoordinates());
        Collections.sort(actualKeys, new SortByCoordinates());

        //The keys are the same 
        for (int index = 0; index < actualKeys.size(); index++) {
            assertTrue(compareCoordinates(expectedKeys.get(index), actualKeys.get(index)));
        }
        
        assertEquals(Consts.GAME_NOT_OVER, checklegalmoves.getGameStatus());

        //In the loaded position only the white king and bishop can move - all other moves break check

        ArrayList<int[]> expectedMovesKing = new ArrayList<int[]>(Arrays.asList(new int[]{2, 1}, new int[]{2, 3}));
        ArrayList<int[]> expectedMovesBishop = new ArrayList<int[]>(Arrays.asList(new int[]{3, 2}));



    }
    
    @Test
    @DisplayName("Tests that the game knows when it is check mate and that the correct pieces can move")
    public void checkMateTest() {
    
        game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6");
        Tile[][] board = game.getBoardTilesDeepCopy();

        checklegalmoves.setMoveNumber(6);
        
        ArrayList<int[]> expectedKeys = new ArrayList<int[]>(Arrays.asList(new int[]{0, 1}, new int[]{1, 6}, new int[]{0, 7}, new int[]{1, 5}, new int[]{1, 1}, new int[]{0, 6}, new int[]{0, 4}, new int[]{4, 7}, new int[]{3, 4}, new int[]{1, 3}, new int[]{1, 0}, new int[]{1, 7}, new int[]{0, 0}, new int[]{1, 2}, new int[]{0, 2}, new int[]{3, 2}));
        HashMap<int[], ArrayList<int[]>> allMoves = checklegalmoves.checkforCheckMateAndPat(board);
        ArrayList<int[]> actualKeys = new ArrayList<int[]>(allMoves.keySet());

        //Test the the ArrayList<int[]> have the same size
        assertEquals(expectedKeys.size(), actualKeys.size());

        //Sorts the lists in the same way so that they can be compared
        Collections.sort(expectedKeys, new SortByCoordinates());
        Collections.sort(actualKeys, new SortByCoordinates());
        
        //The keys are the same 
        for (int index = 0; index < actualKeys.size(); index++) {
            assertTrue(compareCoordinates(expectedKeys.get(index), actualKeys.get(index)));
        }
        
        assertEquals(Consts.GAME_NOT_OVER, checklegalmoves.getGameStatus());
        
        //Moves the queen so white wins
        board[4][7].removePiece();
        board[6][5].setPiece(new Queen("w Q", 'w'));
        checklegalmoves.setMoveNumber(7);
        checklegalmoves.checkforCheckMateAndPat(board);
        
        //White won by checkmate 
        assertEquals(Consts.CHECKMATE, checklegalmoves.getGameStatus());
    
    }

    @Test
    @DisplayName("Tests that the game registers pat")
    public void patTest() {

        game.loadedGamePiecesPosition("wR=0-00-wB-00-00-wX=1-wK-00-00-00-00-00-00-00-00-00-wK-00-00-wB-00-wR=0-00-00-bP=1=0=10001-00-00-00-00-00-00-00-00-00-00-wP=1=0=10002-wP=1=0=10004-00-00-wP=1=0=18-00-00-00-00-00-00-00-wQ-00-00-00-00-bX=1-00-00-00-wQ-00-00-00-00-00-00-00-10040");
        Tile[][] patBoard = game.getBoardTilesDeepCopy();

        patBoard[2][3].removePiece();
        patBoard[4][1].setPiece(new Bishop("w B", 'w'));
        
        checklegalmoves.setMoveNumber(10041);
        checklegalmoves.checkforCheckMateAndPat(patBoard);
        
        //Draw by pat 
        assertEquals(Consts.PAT, checklegalmoves.getGameStatus());

    }   


    //Muligens splitte opp i flere tester. Det er gjort i eksempelprosjektene
    @Test
    @DisplayName("Tests that the validation of a position happens correctly")
    public void validationOfGameStateTest() {
        Tile[][] boardTiles = game.getBoardTilesDeepCopy();
    
        Tile[][] wrongDimensionsTiles1 = new Tile[8][7];
        makeBoardCustomSize(wrongDimensionsTiles1);
                                        
        Tile[][] wrongDimensionsTiles2 = new Tile[7][8];
        makeBoardCustomSize(wrongDimensionsTiles2);

        Tile[][] wrongDimensionsTiles3 = new Tile[5][1];
        makeBoardCustomSize(wrongDimensionsTiles3);
        
        int negativeMovenumber = -1;
        int zeroMovenumber     =  0;
        int positiveMovenumber =  1;

        //Check that the method dosen't accept null values as input 
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(null, zeroMovenumber));
        
        //Checks that the method doesn't accept tile arrays of the wrong dimension, or negative move numbers
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(wrongDimensionsTiles1, positiveMovenumber));
        
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(wrongDimensionsTiles2, positiveMovenumber));
        
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(wrongDimensionsTiles3, positiveMovenumber));
        
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(boardTiles, negativeMovenumber));
        
        assertDoesNotThrow(() -> 
                        checklegalmoves.validationOfGameState(boardTiles, positiveMovenumber));

        assertDoesNotThrow(() -> 
                        checklegalmoves.validationOfGameState(boardTiles, zeroMovenumber));

        /**
         * TESTS FOR KINGS
         */
        
        boardTiles[5][7].setPiece(new King("w X", 'w'));
        boardTiles[5][7].getPiece().setHasMoved(true);

        //Tests that only TWO kings are allowed 
        assertThrows(IllegalArgumentException.class, () ->
                        checklegalmoves.validationOfGameState(boardTiles, 0));
        
        boardTiles[5][7].removePiece(); //Remove extra king

        //Tests that it is not allowed with only ONE king
        boardTiles[7][4].removePiece();
        assertThrows(IllegalArgumentException.class, () ->
                        checklegalmoves.validationOfGameState(boardTiles, 0));
        
        boardTiles[4][4].setPiece(new King("b X", 'b'));
        boardTiles[4][4].getPiece().setHasMoved(false);

        //Tests that the black king can only be in its tart position if it has not moved
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(boardTiles, 9));

        boardTiles[0][4].removePiece();
        boardTiles[4][4].setPiece(new King("w X", 'w'));
        boardTiles[4][4].getPiece().setHasMoved(false);
        
        //Tests that the white king can only be in its start position if it has not moved
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(boardTiles, 0));
        
        //Returns kings to their start positions
        boardTiles[4][4].removePiece();
        boardTiles[0][4].setPiece(new King("w X", 'w'));
        boardTiles[7][4].setPiece(new King("b X", 'b'));
        
        //Check that the board is now in a legal state
        assertDoesNotThrow(() -> 
                        checklegalmoves.validationOfGameState(boardTiles, positiveMovenumber));
        
        /**
         * TESTS FOR PAWNS
         */

        //Reset board 
        Tile[][] pawnBoardTiles = game.getBoardTilesDeepCopy(); //Hard reset board

        pawnBoardTiles[0][1].setPiece(new Pawn("w P", 'w'));
        pawnBoardTiles[0][1].getPiece().setHasMoved(true);
        
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(pawnBoardTiles, 0));
        
        //Removes pawn
        pawnBoardTiles[0][1].removePiece();

        pawnBoardTiles[3][2].setPiece(new Pawn("w P", 'w'));

        //Tests that pawn can only be in start position if it has not moved 
        assertThrows(IllegalArgumentException.class, () -> 
                        checklegalmoves.validationOfGameState(pawnBoardTiles, 1));
        
        //Removes pawn
        pawnBoardTiles[3][2].removePiece();

        pawnBoardTiles[3][4].setPiece(new Pawn("w P", 'w'));
        Pawn pawn = (Pawn)pawnBoardTiles[3][4].getPiece();
        pawn.setMovedTwoLastTurn(true);
        pawn.setHasMoved(true);

        //Tests that the pawn can stand at row 4 (3) with these attributes
        assertDoesNotThrow(() -> 
                        checklegalmoves.validationOfGameState(pawnBoardTiles, 10));
        
        pawn.setHasMoved(false);

        //Tests that the pawn needs to have moved to stand at this row
        assertThrows(IllegalArgumentException.class, () ->
                        checklegalmoves.validationOfGameState(pawnBoardTiles, 4));
                        
        //Removes pawn
        pawnBoardTiles[3][4].removePiece();
        
        /**
         * TESTS FOR ILLEGAL MOVE NUMBERS
         */

        Tile[][] movenumberBoardTiles = game.getBoardTilesDeepCopy();

        //Tests that the pawns movenumber cannot be greater than 0
        pawn = (Pawn)movenumberBoardTiles[1][0].getPiece();
        pawn.setMoveNumberEnPassant(1);
        assertThrows(IllegalArgumentException.class, () -> checklegalmoves.validationOfGameState(movenumberBoardTiles, 0));
        
        //Tests that the pawns movenumber cannot be equal to the game movenumber 
        pawn.setMoveNumberEnPassant(4);
        assertThrows(IllegalArgumentException.class, () -> checklegalmoves.validationOfGameState(movenumberBoardTiles, 4));

        //Tests that the pawns movenumber cannot be greater than the game movenumber
        pawn.setMoveNumberEnPassant(100);
        assertThrows(IllegalArgumentException.class, () -> checklegalmoves.validationOfGameState(movenumberBoardTiles, 4));
        
        //Tests that the pawn movenumber can be less than the game movenumber
        pawn.setMoveNumberEnPassant(3);
        assertDoesNotThrow(() -> checklegalmoves.validationOfGameState(boardTiles, 4));

        //Tests that two pawns cannot have the same movenumber
        Pawn pawn2 = (Pawn)movenumberBoardTiles[1][1].getPiece();
        pawn.setMoveNumberEnPassant(4);
        pawn2.setMoveNumberEnPassant(4);
        assertThrows(IllegalArgumentException.class, () -> checklegalmoves.validationOfGameState(movenumberBoardTiles, 5));
        
        //Tests that two pawns can have different movenumbers
        pawn2.setMoveNumberEnPassant(3);
        assertDoesNotThrow(() -> checklegalmoves.validationOfGameState(movenumberBoardTiles, 5));
        
        /**
         * TEST THAT THE PLAYER THAT ISN'T MOVING CAN'T BE IN CHECK
         */

        Tile[][] playerInCheckBoardTiles = game.getBoardTilesDeepCopy();
        
        playerInCheckBoardTiles[0][4].removePiece();
        playerInCheckBoardTiles[4][4].setPiece(new King("w X", 'w'));
        playerInCheckBoardTiles[4][4].getPiece().setHasMoved(true);
        
        playerInCheckBoardTiles[4][2].setPiece(new Queen("bQ2", 'b'));
        playerInCheckBoardTiles[4][2].getPiece().setHasMoved(true);

        assertThrows(IllegalArgumentException.class, () -> checklegalmoves.validationOfGameState(playerInCheckBoardTiles, 5));
    }


    private boolean compareCoordinates(int[] actual, int[] expected) {
        return (actual[0] == expected[0] && actual[1] == expected[1]) ? true : false;
    }

    private void makeBoardCustomSize(Tile[][] tileArray) {
        for (int row = 0; row < tileArray.length; ++row) {
            for (int col = 0; col < tileArray[0].length; ++col) {

                Tile tile = new Tile(row, col);
                tileArray[row][col] = tile;
            }
        }
    }
}