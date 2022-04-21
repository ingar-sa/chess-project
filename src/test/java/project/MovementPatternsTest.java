package project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Board.Tile;
import project.Movement.Game;
import project.Movement.MovementPatterns;
import project.Pieces.Pawn;

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

        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {

                Tile tile = new Tile(row, col);
                this.boardTiles[row][col] = tile;
            }
        }
    }
    
    
    //TODO: Add comment 
    @Test
    @DisplayName("Start position test")
    public void startPosTest() {
        game = new Game();
        boardTiles = game.getBoardDeepCopyUsingSerialization();
        
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
    @DisplayName("Pawn test")
    public void pawnTest() {

        //Tests that en passent move and normal moves are returned if en passent is allowed
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

        //Tests if en passent (for white) is not allowed given that the player did not move his pawn two tiles last turn (black)
        blackPawn1.setMoveNumberEnPassant(10);
        blackPawn1.setMovedTwoLastTurn(false);

        expectedPawnMoves = new ArrayList<>(Arrays.asList(new int[]{5, 2}));
        actualPawnMoves = whiteMovement.moveHandler(boardTiles[4][2], boardTiles, 11);
        
        assertEquals(expectedPawnMoves.size(), actualPawnMoves.size());
        assertEquals(compareCoordinates(expectedPawnMoves.get(0),  actualPawnMoves.get(0)), true);

        //Tests that pawn attacks on diagonal, and is blocked by a piece directly in front of it
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
        assertEquals(compareCoordinates(expectedPawnMoves.get(0),  actualPawnMoves.get(0)), true);
        assertEquals(compareCoordinates(expectedPawnMoves.get(1), actualPawnMoves.get(1)), true);
    }

    @Test
    @DisplayName("King test")
    public void kingTest() {
        
    }

    @Test
    @DisplayName("Knight test")
    public void knightTest() {
        
    }

    @Test
    @DisplayName("Bishop test")
    public void bishopTest() {
        // Bishop blackBishop1
        
    }

    @Test
    @DisplayName("Rook test")
    public void rookTest() {
        
    }

    @Test
    @DisplayName("Queen test")
    public void queenTest() {
        
    }

    private boolean compareCoordinates(int[] actual, int[] expected) {
        return (actual[0] == expected[0] && actual[1] == expected[1]) ? true : false;
    }

}
