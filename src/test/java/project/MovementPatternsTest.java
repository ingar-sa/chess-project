package project;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Board.Tile;
import project.Movement.Game;
import project.Movement.MovementPatterns;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Pawn;
import project.Pieces.Rook;

public class MovementPatternsTest {
    
    private Game game;
    private Tile[][] boardTiles;
    private MovementPatterns whiteMovement;
    private MovementPatterns blackMovement;


    @BeforeEach
    public void setup() {
        this.whiteMovement = new MovementPatterns('w');
        this.blackMovement = new MovementPatterns('b');
        this.boardTiles = new Tile[8][8];
        makeEmptyBoard(this.boardTiles);

    }
    
    /*
        With the exception of pawns, the two colors use the same
        algorithm for movement, so which color is used in those tests
        is arbitrary.

        The queen combines the rook and the bishops movement algorithms, 
        and it is therefore unnecessary to test her method specifically.
    */

    @Test
    @DisplayName("Tests that the method throws exception when there is illegal input")
    public void moveHandlerTest() {
        Tile[][] wrongDimensionsTiles1 = new Tile[8][7];
        makeEmptyBoard(wrongDimensionsTiles1);
                                        
        Tile[][] wrongDimensionsTiles2 = new Tile[7][8];
        makeEmptyBoard(wrongDimensionsTiles2);

        Tile[][] wrongDimensionsTiles3 = new Tile[5][1];
        makeEmptyBoard(wrongDimensionsTiles3);
        
        int negativeMovenumber = -1;
        int zeroMovenumber     =  0;
        int positiveMovenumber =  1;

        assertThrows(IllegalArgumentException.class, 
                     () -> whiteMovement.moveHandler(null, 
                                        boardTiles, 
                                        positiveMovenumber));

         assertThrows(IllegalArgumentException.class, 
                     () -> whiteMovement.moveHandler(boardTiles[1][1], 
                                        null, 
                                        positiveMovenumber));


        assertThrows(IllegalArgumentException.class, 
                     () -> whiteMovement.moveHandler(wrongDimensionsTiles1[0][0], 
                                                     wrongDimensionsTiles1, 
                                                     positiveMovenumber));

        assertThrows(IllegalArgumentException.class, 
                     () -> whiteMovement.moveHandler(wrongDimensionsTiles2[0][0], 
                                                     wrongDimensionsTiles2, 
                                                     positiveMovenumber));
                                                      

        assertThrows(IllegalArgumentException.class, 
                     () -> whiteMovement.moveHandler(wrongDimensionsTiles3[0][0], 
                                                     wrongDimensionsTiles3, 
                                                     positiveMovenumber));

        assertThrows(IllegalArgumentException.class, 
                     () -> whiteMovement.moveHandler(this.boardTiles[0][0], 
                                                     this.boardTiles, 
                                                     negativeMovenumber));

        assertDoesNotThrow(() -> whiteMovement.moveHandler(this.boardTiles[0][0], 
                                                           this.boardTiles, 
                                                           zeroMovenumber)); 
                     
        assertDoesNotThrow(() -> whiteMovement.moveHandler(this.boardTiles[0][0], 
                                                           this.boardTiles, 
                                                           positiveMovenumber));    
    }

    @Test
    @DisplayName("Tests movement for all pieces for both colors in the start position")
    public void startPosTest() {
        game = new Game();
        boardTiles = game.getBoardTilesDeepCopy();
        
        //Test white pawns start position moves
        for (int col = 0; col < 8; col++) {
            
            ArrayList<int[]> expectedMovesStartWhitePawns = new ArrayList<>(Arrays.asList(new int[]{2, col}, new int[]{3, col}));
            ArrayList<int[]> actualMovesStartWhitePawn = whiteMovement.moveHandler(boardTiles[1][col], boardTiles, 0);
            
            assertEquals(expectedMovesStartWhitePawns.size(), actualMovesStartWhitePawn.size());
            assertEquals(compareCoordinates(expectedMovesStartWhitePawns.get(0), actualMovesStartWhitePawn.get(0)), true);
            assertEquals(compareCoordinates(expectedMovesStartWhitePawns.get(1), actualMovesStartWhitePawn.get(1)), true);
        }
        
        //Test black pawns start position moves
        for (int col = 0; col < 8; col++) {
            
            ArrayList<int[]> expectedMovesStartBlackPawns = new ArrayList<>(Arrays.asList(new int[]{5, col}, new int[]{4, col}));
            ArrayList<int[]> actualMovesStartBlackPawns = blackMovement.moveHandler(boardTiles[6][col], boardTiles, 0);

            assertEquals(expectedMovesStartBlackPawns.size(), actualMovesStartBlackPawns.size());
            assertEquals(compareCoordinates(expectedMovesStartBlackPawns.get(0), actualMovesStartBlackPawns.get(0)), true);
            assertEquals(compareCoordinates(expectedMovesStartBlackPawns.get(1), actualMovesStartBlackPawns.get(1)), true);
        }

        //Test for all other white start pieces
        //This tests edge-of-board bound checking and friendly collision for all pieces except pawns. 
        for (int col = 0; col < 8; ++col) {
            if (col == 1) {
                ArrayList<int[]> expectedWhiteKnightMoves = new ArrayList<>(Arrays.asList(new int[]{2, 0}, new int[]{2, 2}));
                ArrayList<int[]> actualWhiteKnightMoves = whiteMovement.moveHandler(boardTiles[0][col], boardTiles, 0);
                
                assertEquals(actualWhiteKnightMoves.size(), expectedWhiteKnightMoves.size());
                assertEquals(compareCoordinates(actualWhiteKnightMoves.get(0), expectedWhiteKnightMoves.get(0)), true);
                assertEquals(compareCoordinates(actualWhiteKnightMoves.get(1), expectedWhiteKnightMoves.get(1)), true);   
                continue;
            }

            if (col == 6) {
                ArrayList<int[]> expectedWhiteKnightMoves = new ArrayList<>(Arrays.asList(new int[]{2, 5}, new int[]{2, 7}));
                ArrayList<int[]> actualWhiteKnightMoves = whiteMovement.moveHandler(boardTiles[0][col], boardTiles, 0);
                
                assertEquals(actualWhiteKnightMoves.size(), expectedWhiteKnightMoves.size());
                assertEquals(compareCoordinates(actualWhiteKnightMoves.get(0), expectedWhiteKnightMoves.get(0)), true);
                assertEquals(compareCoordinates(actualWhiteKnightMoves.get(1), expectedWhiteKnightMoves.get(1)), true);
                continue;
            }
            
            assertEquals(whiteMovement.moveHandler(boardTiles[0][col], boardTiles, 0).size(), 0);
        }

        //Test for all other black pieces
        //Like white, this tests edge-of-board bound checking and friendly collision
        for (int col = 0; col < 8; ++col) {
            
            if (col == 1) {
                ArrayList<int[]> expectedBlackKnightMoves = new ArrayList<>(Arrays.asList(new int[]{5, 0}, new int[]{5, 2}));
                ArrayList<int[]> actualBlackKnightMoves = blackMovement.moveHandler(boardTiles[7][col], boardTiles, 0);

                assertEquals(actualBlackKnightMoves.size(), expectedBlackKnightMoves.size());
                assertEquals(compareCoordinates(actualBlackKnightMoves.get(0), expectedBlackKnightMoves.get(0)), true);
                assertEquals(compareCoordinates(actualBlackKnightMoves.get(1), expectedBlackKnightMoves.get(1)), true);   
                continue;
            }

            if (col == 6) {
                ArrayList<int[]> expectedBlackKnightMoves = new ArrayList<>(Arrays.asList(new int[]{5, 5}, new int[]{5, 7}));
                ArrayList<int[]> actualBlackKnightMoves = blackMovement.moveHandler(boardTiles[7][col], boardTiles, 0);
                
                assertEquals(actualBlackKnightMoves.size(), expectedBlackKnightMoves.size());
                assertEquals(compareCoordinates(actualBlackKnightMoves.get(0), expectedBlackKnightMoves.get(0)), true);
                assertEquals(compareCoordinates(actualBlackKnightMoves.get(1), expectedBlackKnightMoves.get(1)), true);
                continue;                
            }
            
            assertEquals(blackMovement.moveHandler(boardTiles[7][col], boardTiles, 0).size(), 0);
        }
    }


    @Test
    @DisplayName("Tests the pawns movement algorithm")
    public void pawnMovesTest() {

        //Tests that en passent move and other legal moves are returned if en passent is allowed
        Pawn whitePawn1 = new Pawn("wP1", 'w');
        Pawn blackPawn1 = new Pawn("bP1", 'b');
        
        whitePawn1.setHasMoved(true);
        blackPawn1.setMovedTwoLastTurn(true);
        blackPawn1.setMoveNumberEnPassant(10);
        
        boardTiles[4][2].setPiece(whitePawn1);
        boardTiles[4][3].setPiece(blackPawn1);

        ArrayList<int[]> expectedEnPassentPawnMoves = new ArrayList<>(Arrays.asList(new int[]{5, 2}, new int[]{5, 3}));
        ArrayList<int[]> actualWhiteEnPassentPawnMoves = whiteMovement.moveHandler(boardTiles[4][2], boardTiles, 11);

        assertEquals(expectedEnPassentPawnMoves.size(), actualWhiteEnPassentPawnMoves.size());
        assertEquals(compareCoordinates(actualWhiteEnPassentPawnMoves.get(0),  expectedEnPassentPawnMoves.get(0)), true);
        assertEquals(compareCoordinates(actualWhiteEnPassentPawnMoves.get(1), expectedEnPassentPawnMoves.get(1)), true);


        //Tests if the pawn that could be taken by en passent moved last turn
        blackPawn1.setMoveNumberEnPassant(9);

        ArrayList<int[]> expectedPawnMoves = new ArrayList<>(Arrays.asList(new int[]{5, 2}));
        ArrayList<int[]> actualPawnMoves = whiteMovement.moveHandler(boardTiles[4][2], boardTiles, 11);

        assertEquals(expectedPawnMoves.size(), actualPawnMoves.size());
        assertEquals(compareCoordinates(expectedPawnMoves.get(0),  actualPawnMoves.get(0)), true);

        //Tests if en passent is not allowed given that the opponent did not move his pawn two tiles last turn
        blackPawn1.setMoveNumberEnPassant(10);
        blackPawn1.setMovedTwoLastTurn(false);

        expectedPawnMoves = new ArrayList<>(Arrays.asList(new int[]{5, 2}));
        actualPawnMoves = whiteMovement.moveHandler(boardTiles[4][2], boardTiles, 11);
        
        assertEquals(expectedPawnMoves.size(), actualPawnMoves.size());
        assertEquals(compareCoordinates(expectedPawnMoves.get(0),  actualPawnMoves.get(0)), true);

        //Tests that a white pawn attacks on diagonal, and is blocked by a piece directly in front of it

        makeEmptyBoard(this.boardTiles); //Reset the board

        Pawn whitePawn2 = new Pawn("wP2", 'w');
        Pawn blackPawn2 = new Pawn("bP2", 'b');
        Pawn blackPawn3 = new Pawn("bP3", 'b');
        Pawn blackPawn4 = new Pawn("bP4", 'b');
        
        boardTiles[3][3].setPiece(whitePawn2);
        boardTiles[4][2].setPiece(blackPawn2);
        boardTiles[4][3].setPiece(blackPawn3);
        boardTiles[4][4].setPiece(blackPawn4);

        expectedPawnMoves = new ArrayList<>(Arrays.asList(new int[]{4, 2}, new int[]{4, 4}));
        actualPawnMoves = whiteMovement.moveHandler(boardTiles[3][3], boardTiles, 1);
        
        assertEquals(expectedPawnMoves.size(), actualPawnMoves.size());
        for (int index = 0; index < expectedPawnMoves.size(); index++) {
            assertEquals(compareCoordinates(expectedPawnMoves.get(index), actualPawnMoves.get(index)), true);
       }

        //Tests that a black pawn attacks on diagonal, and is blocked by a piece directly in front of it

        makeEmptyBoard(this.boardTiles); //Reset the board

        Pawn whitePawn3 = new Pawn("wP3", 'w');
        Pawn whitePawn4 = new Pawn("wP4", 'w');
        Pawn whitePawn5 = new Pawn("wP5", 'w');
        Pawn blackPawn5 = new Pawn("bP45:", 'b');
        
        boardTiles[6][3].setPiece(blackPawn5);
        boardTiles[5][2].setPiece(whitePawn3);
        boardTiles[5][4].setPiece(whitePawn4);
        boardTiles[5][3].setPiece(whitePawn5);

        expectedPawnMoves = new ArrayList<>(Arrays.asList(new int[]{5, 2}, new int[]{5, 4}));
        actualPawnMoves = blackMovement.moveHandler(boardTiles[6][3], boardTiles, 1);
        
        assertEquals(expectedPawnMoves.size(), actualPawnMoves.size());
        for (int index = 0; index < expectedPawnMoves.size(); index++) {
            assertEquals(compareCoordinates(expectedPawnMoves.get(index), actualPawnMoves.get(index)), true);
       }
        
    }

    @Test
    @DisplayName("Tests the kings movement algorithm")
    public void kingMovesTest() {

        King whiteKing1 = new King("wK1", 'w');
        Rook whiteRook1 = new Rook("wR1", 'w');
        Rook whiteRook2 = new Rook("wR2", 'w');
        
        boardTiles[0][4].setPiece(whiteKing1);
        boardTiles[0][0].setPiece(whiteRook1);
        boardTiles[0][7].setPiece(whiteRook2);

        //Tests castling, board bounds checking, and general movement
        ArrayList<int[]> expectedKingMoves = new ArrayList<>(Arrays.asList(new int[]{0, 2}, new int[]{0, 6}, new int[]{1, 3}, new int[]{1, 4}, new int[]{1, 5}, new int[]{0, 3}, new int[]{0, 5}));
        ArrayList<int[]> actualKingMoves = whiteMovement.moveHandler(boardTiles[0][4], boardTiles, 0);
        
        assertEquals(expectedKingMoves.size(), actualKingMoves.size());
        for (int index = 0; index < expectedKingMoves.size(); index++) {
            assertEquals(compareCoordinates(expectedKingMoves.get(index), actualKingMoves.get(index)), true);
        }


        //Tests that king can move and take all squares around it, and that it ignores being in check
        //or that a move will put it in check, since this is handled in checkLegalMoves
        game = new Game();
        boardTiles = game.getBoardTilesDeepCopy();
        boardTiles[5][3].setPiece(whiteKing1);
        
        expectedKingMoves = new ArrayList<>(Arrays.asList(new int[]{6, 2}, new int[]{6, 3}, new int[]{6, 4}, new int[]{5, 2}, new int[]{5, 4}, new int[]{4, 2}, new int[]{4, 3}, new int[]{4, 4}));
        actualKingMoves = whiteMovement.moveHandler(boardTiles[5][3], boardTiles, 0);
       
        assertEquals(expectedKingMoves.size(), actualKingMoves.size());
        for (int index = 0; index < expectedKingMoves.size(); index++) {
            assertEquals(compareCoordinates(expectedKingMoves.get(index), actualKingMoves.get(index)), true);
        }
    }

    @Test
    @DisplayName("Tests the knights movement algorithm")
    public void knightMovesTest() {
        //Out-of-bounds checking was already performed in startPosTest.
        Knight blackKnight1 = new Knight("bK1", 'b');

        //Tests that Knight can't take pieces in own color and moves correctly
        game = new Game();
        boardTiles = game.getBoardTilesDeepCopy();

        boardTiles[5][3].setPiece(blackKnight1);

        ArrayList<int[]> expectedKnightMoves = new ArrayList<>(Arrays.asList(new int[]{4, 1}, new int[]{3, 2}, new int[]{4, 5}, new int[]{3, 4}));
        ArrayList<int[]> actualKnightMoves = blackMovement.moveHandler(boardTiles[5][3], boardTiles, 0);
       
        assertEquals(expectedKnightMoves.size(), actualKnightMoves.size());
        for (int index = 0; index < expectedKnightMoves.size(); index++) {
            assertEquals(compareCoordinates(expectedKnightMoves.get(index), actualKnightMoves.get(index)), true);
        }

        //Tests that the knight moves correctly and takes enemies 
        boardTiles = game.getBoardTilesDeepCopy();

        boardTiles[2][3].setPiece(blackKnight1);

        expectedKnightMoves = new ArrayList<>(Arrays.asList(new int[]{3, 1}, new int[]{4, 2}, new int[]{3, 5}, new int[]{4, 4}, new int[]{1, 1}, new int[]{0, 2}, new int[]{1, 5}, new int[]{0, 4}));
        actualKnightMoves = blackMovement.moveHandler(boardTiles[2][3], boardTiles, 0);
       
        assertEquals(expectedKnightMoves.size(), actualKnightMoves.size());
        for (int index = 0; index < expectedKnightMoves.size(); index++) {
            assertEquals(compareCoordinates(expectedKnightMoves.get(index), actualKnightMoves.get(index)), true);
        }
    }

    @Test
    @DisplayName("Tests the bishops movement algorithm")
    public void bishopMovesTest() {

        game = new Game();
        boardTiles = game.getBoardTilesDeepCopy();

        Bishop blackBishop1 = new Bishop("bB1", 'b');
        boardTiles[4][5].setPiece(blackBishop1);

        //Tests that a bishop can move across the board as expected, that it cannot take
        //a piece of its own color, that it is bounded by the board and that it cannot
        //take one of the opponents pieces that is behind another of their pieces
        ArrayList<int[]> expectedBishopMoves = new ArrayList<>(Arrays.asList(new int[]{5, 6}, new int[]{5, 4}, new int[]{3, 6}, new int[]{2, 7}, new int[]{3, 4}, new int[]{2, 3}, new int[]{1, 2}));
        ArrayList<int[]> actualBishopMoves = blackMovement.moveHandler(boardTiles[4][5], boardTiles, 0);

        assertEquals(expectedBishopMoves.size(), actualBishopMoves.size());
        for (int index = 0; index < expectedBishopMoves.size(); index++) {
            assertEquals(compareCoordinates(expectedBishopMoves.get(index), actualBishopMoves.get(index)), true);
       }
    }

    @Test
    @DisplayName("Tests the rooks movement algorithm")
    public void rookMovesTest() {

        game = new Game();
        boardTiles = game.getBoardTilesDeepCopy();

        Rook whiteRook1 = new Rook("wR1", 'w');
        boardTiles[3][3].setPiece(whiteRook1);
        
        //Tests that a rook can move across the board as expected, that it cannot take
        //a piece of its own color, that it is bounded by the board and that it cannot
        //take one of the opponents pieces that is behind another of the opponents pieces
        ArrayList<int[]> expectedRookMoves = new ArrayList<>(Arrays.asList( new int[]{4, 3}, new int[]{5, 3}, new int[]{6, 3}, new int[]{2, 3}, new int[]{3, 2}, new int[]{3, 1}, new int[]{3, 0}, new int[]{3, 4}, new int[]{3, 5}, new int[]{3, 6}, new int[]{3, 7}));
        ArrayList<int[]> actualRookMoves   = whiteMovement.moveHandler(boardTiles[3][3], boardTiles, 0);

        assertEquals(expectedRookMoves.size(), actualRookMoves.size());
        for (int index = 0; index < expectedRookMoves.size(); index++) {
            assertEquals(compareCoordinates(expectedRookMoves.get(index), actualRookMoves.get(index)), true);
       }
       
       
    }

    private boolean compareCoordinates(int[] actual, int[] expected) {
        return (actual[0] == expected[0] && actual[1] == expected[1]) ? true : false;
    }

    private void makeEmptyBoard(Tile[][] tileArray) {
        for (int row = 0; row < tileArray.length; ++row) {
            for (int col = 0; col < tileArray[0].length; ++col) {

                Tile tile = new Tile(row, col);
                tileArray[row][col] = tile;
            }
        }
    }

}
