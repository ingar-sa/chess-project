package project.Movement;

import java.util.ArrayList;

import project.Board.Board;
import project.Board.Tile;
import project.Pieces.Piece;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Pawn;
import project.Pieces.Queen;
import project.Pieces.Rook;

// Husk en passant må skje rett etter motstander har flyttet + queen promotion


public class PlayerMove {

    private Board board;
    private char color;

    PlayerMove (Board board, char color) {
        this.board = board;
        this.color = color;
    }

    public void moveHandler(Tile tile) {
        if (!(tile.isOccupied() && tile.getPiece().getColor() == this.color)) return;
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        Piece piece = tile.getPiece();
        
        if (piece instanceof Pawn) legalMoves = legalPawnMove(tile);
        else if (piece instanceof Bishop) legalBishopMove(tile);
        System.out.println(legalMoves);
        // else if (piece instanceof Rook) legalRookMove(tile);
        // else if (piece instanceof Knight) legalKnightMove(tile);
        // else if (piece instanceof King) legalKingMove(tile);
        // else if (piece instanceof Queen) legalQueenMove(tile);
    }

    

    private Tile getTile(Tile tile) {
        if (tile.isOccupied() && tile.getPiece().getColor() == this.color) {
            return tile;
        }
        
        return null;
    }

    private ArrayList<int[]> legalPawnMove(Tile tile) {     
        ArrayList<int[]> legalPawnMoves = new ArrayList<int[]>();
        
        Tile[][] boardTiles = board.getBoardTiles();
        
        Pawn pawn = (Pawn)tile.getPiece(); 
        int row = tile.getRow();
        int col = tile.getCol();

        //Check what the move number is, how should this be handeled? There is a attribute in pawn linked to this right now   
        int moveNumber = ((Pawn)tile.getPiece()).getMoveNumber();

        //A possible solution is implementing a prepass check on just the coordinates
        //of the possible moves, and discarding any that fall outside of the bounds of the board
        //ArrayList<int[]> legalCoordinates = new ArrayList<int[]>();
        
        int moveDirection = 1; 
        if (this.color == 'b') moveDirection = -1;
        
        Tile inFront  = boardTiles[row+(1*moveDirection)][col];
        Tile twoInFront  = boardTiles[row+(2*moveDirection)][col]; 

        //Need to handle edge cases for pawns on column 0 and 7
        Tile attackLeft = null;
        Tile attackRight = null;
        
        if (!(col == 0)) { 
            attackLeft = boardTiles[row+(1*moveDirection)][col-1];
        }

        if (!(col == 7)) {
            attackRight = boardTiles[row+(1*moveDirection)][col+1];
        }

        Tile passantLeft = null; 
        Tile passantRight = null;
        
        if (this.color == 'w' && row == 4 && col != 0) {
            passantLeft = boardTiles[row][col-1];
        }

        if (this.color == 'b' && row == 3 && col != 0) {
            passantLeft = boardTiles[row][col-1];
        }

        if (this.color == 'w' && row == 4 && col != 7) {
            passantRight = boardTiles[row][col+1];
        }

        if (this.color == 'b' && row == 3 && col != 7) {
            passantRight = boardTiles[row][col+1];
        }
        

        if (!inFront.isOccupied()) {
            legalPawnMoves.add(new int[]{inFront.getRow(), inFront.getCol()});
        }    
        
        if (!pawn.hasMoved()) {
            if (!twoInFront.isOccupied() 
                && legalPawnMoves.size() != 0) {
                    
                  legalPawnMoves.add(new int[]{twoInFront.getRow(), twoInFront.getCol()});
            }
        }

        if (attackLeft != null 
            && attackLeft.isOccupied() 
            && attackLeft.getPiece().getColor() != this.color) {

            legalPawnMoves.add(new int[]{attackLeft.getRow(), attackLeft.getCol()});
        }

        if (attackRight != null  
            && attackRight.isOccupied() 
            && attackRight.getPiece().getColor() != this.color) {

            legalPawnMoves.add(new int[]{attackRight.getRow(), attackRight.getCol()});
        }

        if (passantLeft != null 
            && passantLeft.isOccupied() 
            && ((moveNumber - 1) == (((Pawn)passantLeft.getPiece()).getMoveNumber())) 
            && passantLeft.getPiece().getColor() != this.color 
            && ((Pawn)passantLeft.getPiece()).hasMovedTwo()) {

            legalPawnMoves.add(new int[]{passantLeft.getRow() + 1 * moveDirection, passantLeft.getCol()});
        }

        if (passantRight != null 
            && passantRight.isOccupied() 
            && ((moveNumber - 1) == (((Pawn)passantRight.getPiece()).getMoveNumber())) 
            && passantRight.getPiece().getColor() != this.color 
            && ((Pawn)passantRight.getPiece()).hasMovedTwo()) {
                
            legalPawnMoves.add(new int[]{passantRight.getRow() + 1 * moveDirection, passantRight.getCol()});
        } 

        return legalPawnMoves;
    }


    private ArrayList<int[]> legalBishopMove(Tile tile) {
        
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        Tile[][] boardTiles = board.getBoardTiles();
        
        Bishop bishop = (Bishop)tile.getPiece(); 
        int row = tile.getRow();
        int col = tile.getCol();

        int whileRow = row + 1;
        int whileCol = col + 1;
          
        while(whileRow < 8 && whileCol < 8) {
            
            Tile checkedTile = boardTiles[whileRow][whileCol]; 
            
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() == this.color) {
                break;
            }
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() != this.color) {
                legalMoves.add(new int[]{whileRow, whileCol});
                break;
                }

            legalMoves.add(new int[]{whileRow, whileCol});
            
            whileRow++;
            whileCol++;
        }

        whileRow = row + 1;
        whileCol = col - 1;
        while(whileRow < 8 && whileCol > 0) {
            
            Tile checkedTile = boardTiles[whileRow][whileCol]; 
            
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() == this.color) {
                break;
            }
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() != this.color) {
                legalMoves.add(new int[]{whileRow, whileCol});
                break;
                }

            legalMoves.add(new int[]{whileRow, whileCol});    

            whileRow++;
            whileCol--;
        }

        whileRow = row - 1;
        whileCol = col + 1;
        while(whileRow > 0 && whileCol < 8) {
            
            Tile checkedTile = boardTiles[whileRow][whileCol]; 
            
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() == this.color) {
                break;
            }
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() != this.color) {
                legalMoves.add(new int[]{whileRow, whileCol});
                break;
                }

            legalMoves.add(new int[]{whileRow, whileCol});

            whileRow--;
            whileCol++;
        }
        
        whileRow = row - 1;
        whileCol = col - 1; 
        while(whileRow > 0 && whileCol > 0) {
            
            Tile checkedTile = boardTiles[whileRow][whileCol]; 
            
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() == this.color) {
                break;
            }
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() != this.color) {
                legalMoves.add(new int[]{whileRow, whileCol});
                break;
                }

            legalMoves.add(new int[]{whileRow, whileCol});

            whileRow--;
            whileCol--;
        }

        return legalMoves;
    }


    /*
    private ArrayList<int[]> legalRookMove(Tile tile) {
    }

    private ArrayList<int[]> legalKnightMove(Tile tile) {
    }

    

    private ArrayList<int[]> legalKingMove(Tile tile) {
    }

    private ArrayList<int[]> legalQueenMove(Tile tile) {
    }
    */
    private void makeMove(Tile ourTile, ArrayList<int[]> legalMoves, int[] debugChoice) {
        //Some code to choose which move the player picks from the interface
        Tile[][] boardTiles = board.getBoardTiles();
        Tile newTile = boardTiles[debugChoice[0]][debugChoice[1]];
        
        newTile.setPiece(ourTile.getPiece());
        ourTile.setPiece(null);
    }

    public static void main(String[] args) {
        Board testBoard = new Board();

        PlayerMove white = new PlayerMove(testBoard, 'w');
        PlayerMove black = new PlayerMove(testBoard, 'b');

        Tile[][] boardTiles = testBoard.getBoardTiles();

        Bishop b1 = new Bishop("bB1", 'b');
        Bishop b2 = new Bishop("bB2", 'b');
        Bishop w1 = new Bishop("wB1", 'w');
        Bishop w2 = new Bishop("wB1", 'w');

        boardTiles[0][0].setPiece(b1);
        testBoard.printBoard();
        black.moveHandler(boardTiles[0][0]);
        

        /* PAWN
        Pawn testPawn2 = new Pawn("yes", 'w');
        testPawn2.setMovedTwo();
        Pawn testPawn3 = new Pawn("fri", 'w');

        Pawn testPawn = new Pawn("wEp", 'w');
        testPawn.setMovedTwo(); 
        testPawn.setMoveNumber(2);

        Pawn testPawn4 = new Pawn("bEP", 'b');
        testPawn4.setMovedTwo();
        testPawn4.setHasMoved();
        testPawn4.setMoveNumber(1);

        boardTiles[4][6].setPiece(testPawn);
        boardTiles[3][7].setPiece(testPawn2);
        boardTiles[5][3].setPiece(testPawn3);
        boardTiles[4][7].setPiece(testPawn4);
        */

    }
}
