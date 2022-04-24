package project;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @DisplayName("")
    public void checkforCheckMateAndPatTest() {

        game.loadedGamePiecesPosition("wR=0-wK-wB-00-wX=0-00-wK-wR=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-wP=0=0=0-wP=0=0=0-wP=0=0=0-00-00-00-00-00-00-00-00-00-00-wB-00-wP=1=1=0-00-00-00-bP=1=0=5-00-00-00-bP=1=1=1-00-00-wQ-00-00-00-00-00-00-00-00-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-00-bP=0=0=0-bP=0=0=0-bP=0=0=0-bR=0-bK-bB-bQ-bX=0-bB-bK-bR=0-6");
        Tile[][] board = game.getBoardTilesDeepCopy();

        checklegalmoves.setMoveNumber(6);
        
        ArrayList<int[]> expectedKeys = new ArrayList<int[]>(Arrays.asList(new int[]{0, 1}, new int[]{1, 6}, new int[]{0, 7}, new int[]{1, 5}, new int[]{1, 1}, new int[]{0, 6}, new int[]{0, 4}, new int[]{4, 7}, new int[]{3, 4}, new int[]{1, 3}, new int[]{1, 0}, new int[]{1, 7}, new int[]{0, 0}, new int[]{1, 2}, new int[]{0, 2}, new int[]{3, 2}));
        HashMap<int[], ArrayList<int[]>> allMoves = checklegalmoves.checkforCheckMateAndPat(board);
        ArrayList<int[]> actualKeys = new ArrayList<int[]>(allMoves.keySet());

        assertEquals(expectedKeys.size(), actualKeys.size());

        //Sorts the lists in a equal way so that they can be compared
        Collections.sort(expectedKeys, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] == o2[0])
                    return o1[1] - o2[1];
                
                return o1[0] - o2[0];
            }
        });
        
        Collections.sort(actualKeys, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] == o2[0])
                    return o1[1] - o2[1];
                
                return o1[0] - o2[0];
            }
        });
        
        for (int index = 0; index < actualKeys.size(); index++) {
            assertTrue(compareCoordinates(expectedKeys.get(index), actualKeys.get(index)));
        }
        
        //Tests that the pawn that is pinned by the queen cant move becasue of check 
        for (int[] key: actualKeys) {
            if (compareCoordinates(key, new int[]{6, 5})) {
                assertTrue(allMoves.get(key).isEmpty());
            }
        }

        assertEquals(Consts.GAME_NOT_OVER, checklegalmoves.getGameStatus());
        
        //Moves the queen so white wins
        board[4][7].removePiece();
        board[6][5].setPiece(new Queen("w Q", 'w'));
        checklegalmoves.setMoveNumber(7);
        checklegalmoves.checkforCheckMateAndPat(board);
        
        //White won by checkmate 
        assertEquals(Consts.CHECKMATE, checklegalmoves.getGameStatus());

        game.loadedGamePiecesPosition("wR=0-00-wB-00-00-wX=1-wK-00-00-00-00-00-00-00-00-00-wK-00-00-wB-00-wR=0-00-00-bP=1=0=10001-00-00-00-00-00-00-00-00-00-00-wP=1=0=10002-wP=1=0=10004-00-00-wP=1=0=18-00-00-00-00-00-00-00-wQ-00-00-00-00-bX=1-00-00-00-wQ-00-00-00-00-00-00-00-10040");
        Tile[][] patBoard = game.getBoardTilesDeepCopy();

        patBoard[2][3].removePiece();
        patBoard[4][1].setPiece(new Bishop("w B", 'w'));
        
        checklegalmoves.setMoveNumber(10041);
        checklegalmoves.checkforCheckMateAndPat(patBoard);
        
        //Draw by pat 
        assertEquals(Consts.PAT, checklegalmoves.getGameStatus());
    }   

    

    private boolean compareCoordinates(int[] actual, int[] expected) {
        return (actual[0] == expected[0] && actual[1] == expected[1]) ? true : false;
    }
}