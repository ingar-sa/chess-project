package project;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.Board.Tile;
import project.Movement.Game;
import project.Movement.MovementPatterns;
import project.Pieces.King;
import project.Pieces.Pawn;
import project.Pieces.Rook;

public class GameTest {
    
    private Game game;

    @BeforeEach
    public void setup() {
        //Makes a empty board 
        this.game = new Game();
    }
    
    //TODO: Add explenations 
    //TODO; can add test for all black pices as well

    @Test
    @DisplayName("")
    public void gameTest() {

    }


    @Test
    @DisplayName("Test that game positions are loaded correctly from Strings")
    public void loadedGamePiecesPositionTest() {

        //Test that the given String has legal values 

        Tile[][] orginalPosition = game.getBoardTilesDeepCopy();

        //Test that the input cant be null
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition(null));
        //Test that the string is split into 65 elements
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("test-test--test"));
        //Test that the string is split into 65 elements; String should split into 64 in this particular case
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0"));
        //Test that the string is split into 65 elements; String should split into 66 in this particular case
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6-7"));
        
        //Test that all the elements after split() are correct; the string is the correct length 

        //00 changed to 01
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("wR=0-wK-wB-01-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6"));
        //00 changed to letters (hihaho)
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-hihaho-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6"));
        //wK changed to wE
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("wR=0-wE-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6"));
        //wP=0=0=1 changed to wP=0=0=-1 
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("wR=0-wE-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6"));

        //Tests of legal String format, but illegal game position: e.g. pawns in row 1 and 8

        //Pawn in row 1
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("wP=1=0=0-wE-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6"));
        //Pawn in row 8 
        assertThrows(IllegalArgumentException.class,
        () -> game.loadedGamePiecesPosition("wR=0-wE-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-wP=1=0=3-6"));
        
        //Test that the old, orginal position is the position after illegal game positions are loaded
        Tile[][] positionAfterIllegalInputs = game.getBoardTilesDeepCopy();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (orginalPosition[row][col].getPiece() != null) {
                    assertTrue(orginalPosition[row][col].getPiece().getClass().equals(positionAfterIllegalInputs[row][col].getPiece().getClass()));
                }
                else {
                    assertTrue(orginalPosition[row][col].getPiece() == null && positionAfterIllegalInputs[row][col].getPiece() == null);
                }
            }
        }

    }

    @Test
    @DisplayName("Test that tiles return correct String[] with information")
    public void getPieceInfoFromTileTest() {
        
        //Checks that the empty tiles on the board from start position returns expected String[]
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                
                assertTrue(compareStringArrays(new String[]{row + "" + col, ""}, game.getPieceInfoFromTile(row, col)));
            }
        }
        
        this.game = new Game();

        //White pieces 
        String[] expectedWhitePawnString = new String[]{"10", "wP"};
        String[] expectedwhiteRookString = new String[]{"00", "wR"};
        String[] expectedwhiteKnightString = new String[]{"01", "wK"};
        String[] expectedwhiteBishopString = new String[]{"02", "wB"};
        String[] expectecWhiteQueenString = new String[]{"03", "wQ"};
        String[] expectedwhiteKingString = new String[]{"04", "wX"}; 

        //Black pieces
        String[] expectedBlackPawnString = new String[]{"67", "bP"};

        //Checks that placed pieces return expected information
        assertTrue(compareStringArrays(expectedWhitePawnString, game.getPieceInfoFromTile(1, 0)));
        assertTrue(compareStringArrays(expectedwhiteRookString, game.getPieceInfoFromTile(0, 0)));
        assertTrue(compareStringArrays(expectedwhiteKnightString, game.getPieceInfoFromTile(0, 1)));
        assertTrue(compareStringArrays(expectedwhiteBishopString, game.getPieceInfoFromTile(0, 2)));
        assertTrue(compareStringArrays(expectecWhiteQueenString, game.getPieceInfoFromTile(0, 3)));
        assertTrue(compareStringArrays(expectedwhiteKingString, game.getPieceInfoFromTile(0, 4)));
        assertTrue(compareStringArrays(expectedBlackPawnString, game.getPieceInfoFromTile(6, 7)));
    }


    @Test
    @DisplayName("Tests that a correct ArrayList<String> of legeal moves is returned for a chosen piece")
    public void getLegalMovesTest() {
        
        //Tests that coordinates outside the board are illegal - this tests the private method validationOfCoordinates
        for (int row = - 50; row < 50; row++) {
            for (int col = - 50; col < 50; col++) {
                int rowNow = row;
                int colNow = col;

                if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                    continue;
                }
                else {
                    assertThrows(IllegalArgumentException.class,
                    () -> game.getLegalMoves(rowNow, colNow));
                }
            }
        }
   
        //Checks that the tiles without a piece and the pieces that are not moving (black in this case) returns a empty ArrayList
        for (int row = 2; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(true, game.getLegalMoves(row, col).isEmpty());
            }
        } 

        //Tests that a white horse returns expected string from start position
        ArrayList<String> expectedArrayListStringLeftwK = new ArrayList<>(Arrays.asList("20", "22"));
        assertEquals(expectedArrayListStringLeftwK, game.getLegalMoves(0, 1));
        
        //Loads inn specific game position
        game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6");
        //Tests that the white king returns the expected string from a generated position
        ArrayList<String> expectedArrayListStringwX = new ArrayList<>(Arrays.asList("14", "03", "05"));
        assertEquals(expectedArrayListStringwX, game.getLegalMoves(0, 4));

    }

    @Test
    @DisplayName("Test that en passent works as expected")
    public void moveChosenPieceEnPassentTest() {
        
        //Test of en passent 

        //Loads position where en passent can be preformed for white
        this.game.loadedGamePiecesPosition("wR=0-wK-wB-wQ-wX=0-wB-wK-wR=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-wP=1=0=2-bP=1=1=3-00-00-00-00-00-bP=1=1=1-00-00-00-00-00-00-00-00-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-4");
        String ExpectedStringEnPassent1 = "41"; 
        //Tests that a white pawn has en passent move, which it should have 
        assertEquals(ExpectedStringEnPassent1, this.game.moveChosenPiece(4, 0, 5, 1));
        Tile[][] enPassentPreformed = this.game.getBoardTilesDeepCopy();
        //Checks that the en passent was correctly preformed and that the board state is correct 
        assertTrue(enPassentPreformed[4][0].getPiece() == null);
        assertTrue(enPassentPreformed[5][1].getPiece() instanceof Pawn);
        assertTrue(enPassentPreformed[4][1].getPiece() == null);
        //Test that you cant perform a new move before the board is updated
        assertThrows(IllegalStateException.class,
        () -> this.game.moveChosenPiece(1, 1, 2, 1));
    }

    @Test
    @DisplayName("Test that castling works as expected")
    public void moveChosenPieceCastlingTest() {
        
        //Test of castling 

        //Loads position where black can perform castling to the right, none of the castling tiles are under threat
        this.game.loadedGamePiecesPosition("wR=0-wK-wB-wQ-wX=0-wB-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-wP=0=0=0-00-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-00-wP=1=1=6-00-00-wP=1=1=4-00-00-00-00-00-00-bP=1=1=1-00-00-00-00-00-00-00-wP=1=0=8-bP=1=0=5-bB-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-00-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-00-00-bR=0-9");
        String expectedCastlingString = "75";
        //Tests that the king and rook can perform the move as expected 
        assertEquals(expectedCastlingString, this.game.moveChosenPiece(7, 4, 7, 6));
        Tile[][] castlingPerformed = this.game.getBoardTilesDeepCopy();
        //Tests that the board is updaeted correctly 
        assertTrue(castlingPerformed[7][4].getPiece() == null);
        assertTrue(castlingPerformed[7][5].getPiece() instanceof Rook);
        assertTrue(castlingPerformed[7][6].getPiece() instanceof King);

        //Loads position where white can perform castling to the right, none of the castling tiles are under threat
        this.game.loadedGamePiecesPosition("wR=0-wK-wB-wQ-wX=0-00-00-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-wK-bP=1=0=5-00-00-00-00-00-00-00-00-wP=1=0=6-bP=1=1=3-00-00-00-00-bP=1=0=11-bP=1=0=7-bQ-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-00-00-00-bP=0=0=0-bR=0-bK-bB-00-bX=0-bB-bK-bR=0-12");
        expectedCastlingString = "05";
        //Tests that the king and rook can perform the move as expected 
        assertEquals(expectedCastlingString, this.game.moveChosenPiece(0, 4, 0, 6));
        castlingPerformed = this.game.getBoardTilesDeepCopy();
        //Tests that the board is updated correctly 
        assertTrue(castlingPerformed[0][4].getPiece() == null);
        assertTrue(castlingPerformed[0][5].getPiece() instanceof Rook);
        assertTrue(castlingPerformed[0][6].getPiece() instanceof King);

        //Loads similar position as the one above, but now one of the castling tiles can be taken by the black queen
        this.game.loadedGamePiecesPosition("wR=0-wK-wB-wQ-wX=0-00-00-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-wP=0=0=0-wP=0=0=0-00-wK-00-00-00-00-00-00-00-00-00-00-bP=1=0=5-00-00-00-00-wB-00-00-00-bQ-bP=1=1=3-00-00-00-bP=1=0=13-bP=1=0=11-bP=1=0=7-00-00-00-bP=0=0=0-bP=0=0=0-00-00-00-00-00-bP=0=0=0-bR=0-bK-bB-00-bX=0-bB-bK-bR=0-16");
        //Test that the castling move is not allowed 
        assertThrows(IllegalArgumentException.class,
        () -> this.game.moveChosenPiece(0, 4, 0, 6));

    }

    @Test
    @DisplayName("Test that moving pieces works as expected")
    public void moveChosenPieceTest() {

        //Test of "normal" moves

        //Test of pawn move from start position 
        //Moves white pawn two steps forward
        this.game.moveChosenPiece(1, 3, 3, 3);
        Tile[][] pawnMovedTwoSteps = this.game.getBoardTilesDeepCopy();
        //Tests that the board is updated correctly 
        assertTrue(pawnMovedTwoSteps[1][3].getPiece() == null);
        assertTrue(pawnMovedTwoSteps[3][3].getPiece() instanceof Pawn);
        //Tests that the attributes are updated correctly 
        Pawn pawn1 = ((Pawn)pawnMovedTwoSteps[3][3].getPiece());
        assertTrue(pawn1.getHasMoved());
        assertTrue(pawn1.getMovedTwoLastTurn());
        assertTrue(pawn1.getMoveNumberEnPassant() == 0);
    }
    
    @Test
    @DisplayName("Test that the returned string is as expected. The string contains information about what pawn that should be promoted")
    public void pawnPromotionStringCoordinatesTest() {
        
        String expectedStartPositionString = "";
        //Tests that there are no pawns that can be promoted
        assertEquals(expectedStartPositionString, game.pawnPromotionStringCoordinates());

        //Load position where white pawn is located at the top row
        game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-bP=1=0=15-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-wQ-00-00-00-00-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bQ-bP=0=0=0-00-wP=1=0=14-bR=0-bK-bB-00-bX=0-bB-bK-bR=0-16");
        //Moves the white pawn to the 8th row 
        game.moveChosenPiece(6, 7, 7, 6);
        String expectedWhitePromotionPawn = "76";
        //Tests that the game returns the expected "promotion String"
        assertEquals(expectedWhitePromotionPawn, game.pawnPromotionStringCoordinates());
    }

    @Test
    @DisplayName("Test that chcek mate and pat works as expected, and that checkForGameOverTest only can be called under the correct condidtions")
    public void checkForGameOverTest() {
        
        //Tests that you need to move a piece before you can call checkForGameOver
        assertThrows(IllegalStateException.class,
        () -> this.game.checkForGameOver());

        //Load position where white pawn is located at the top row
        game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-bP=1=0=15-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-wQ-00-00-00-00-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bQ-bP=0=0=0-00-wP=1=0=14-bR=0-bK-bB-00-bX=0-bB-bK-bR=0-16");
        //Moves the white pawn to the 8th row 
        game.moveChosenPiece(6, 7, 7, 6);
        //Test that you need to promote pawn before using checkForGameOver
        assertThrows(IllegalStateException.class,
        () -> this.game.checkForGameOver());

        //Load position one move away from checkmate
        game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6");
        //Moves the Queen, and white wins by checkmate 
        game.moveChosenPiece(4, 7, 6, 5);
        //Test that the game is over bu checkmate for white 
        assertEquals(game.checkForGameOver(), Consts.CHECKMATE_FOR_WHITE);
        //Test that you can not call checkForGameOver, when the game is over
        assertThrows(IllegalStateException.class,
        () -> this.game.checkForGameOver());

    }

    @Test
    @DisplayName("")
    public void promotePawnTest() {

        //Tests that it throws exception if there is no pawn to promote
        assertThrows(IllegalStateException.class, () -> game.promotePawn(7, 5, 'Q', 'w'));
        
        //Loads position where white pawn is one tile away from promotion on the far rigth
        game.loadedGamePiecesPosition("wR=0-wK-wB-wQ-wX=0-wB-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-bK-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-wP=1=0=6-bR=0-bK-bB-bQ-bX=0-bB-00-00-8");
        //Moves the pawn to the 7th row, ready for promotion 
        game.moveChosenPiece(6, 7, 7, 7);
        //Tests that the promotePawn do not allow illegal coordinates - validationOfCoordinates
        assertThrows(IllegalArgumentException.class, () -> game.promotePawn(-1, 8, 'Q', 'w'));
        //Checks that the promotion piece needs to be white        
        assertThrows(IllegalArgumentException.class, () -> game.promotePawn(7, 7, 'Q', 'b'));        
        //Test that the pawn is located at the input coordinates
        assertThrows(IllegalArgumentException.class, () -> game.promotePawn(7, 5, 'Q', 'w'));
        //Tests that input must be a legal piece
        assertThrows(IllegalArgumentException.class, () -> game.promotePawn(7, 7, 'X', 'w'));
        //promotes the pawn
        game.promotePawn(7, 7, 'R', 'w');
        //Loads inn current position 
        Tile[][] promotedPawnToRook = game.getBoardTilesDeepCopy();
        //Checks that the pawn is promoted and that the new piece is a rook
        assertTrue(promotedPawnToRook[7][7].getPiece() instanceof Rook);
        assertTrue(promotedPawnToRook[6][7].getPiece() == null);

        //Loads inn position where black can promote pawn 
        game.loadedGamePiecesPosition("wR=0-wK-wB-wQ-wX=0-wB-00-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-bP=1=0=7-00-00-00-00-00-00-00-00-00-00-00-wK-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-9");
        //Moves a black pawn so it can be promoted    
        game.moveChosenPiece(1, 7, 0, 7);
        //Tests that it throws exception if we try to promote a white pawn
        assertThrows(IllegalArgumentException.class, () -> game.promotePawn(7, 7, 'Q', 'w'));
        //Tests that it can promote the black pawn
        assertDoesNotThrow(() -> game.promotePawn(0, 7, 'Q', 'b'));
    }

    @Test
    @DisplayName("")
    public void allLegalPiecesTest() {

        //Tests that the coordinates for white pieces returns true and all black/empty coordinates return false 
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row < 2) {
                    assertTrue(game.allLegalPieces(row, col));
                }
                else {
                    assertFalse(game.allLegalPieces(row, col));
                }
            }
        }
    }

    private boolean compareStringArrays(String[] expected, String[] actual) {
        
        if (expected[0].equals(actual[0]) && expected[1].equals(actual[1])) {
            return true;
        }
        return false;
    }

    



}

