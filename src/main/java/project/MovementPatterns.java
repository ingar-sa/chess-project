package project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import project.Piece;
import project.pieces.Bishop;
import project.pieces.King;
import project.pieces.Knight;
import project.pieces.Pawn;
import project.pieces.Queen;
import project.pieces.Rook;

// Husk en passant må skje rett etter motstander har flyttet + queen promotion


public class MovementPatterns {

    private Board board;
    private char color;
    private Tile[][] boardTiles = board.getBoardTiles();
    

    MovementPatterns (Board board, char color) {
        this.board = board;
        this.color = color;
    }

   
    public void moveHandler(Tile tile) {
        if (!(tile.isOccupied() && tile.getPiece().getColor() == this.color)) return;
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        Piece piece = tile.getPiece();
        
        if (piece instanceof Pawn) legalMoves = pawnMoves(tile);
        else if (piece instanceof Bishop) bishopMoves(tile);
        else if (piece instanceof Rook) rookMoves(tile);
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

    private ArrayList<int[]> pawnMoves(Tile tile) {     
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

    private ArrayList<int[]> bishopMoves(Tile tile) {
        
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        Tile[][] boardTiles = board.getBoardTiles();
        
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
        while(whileRow < 8 && whileCol >= 0) {
            
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
        while(whileRow >= 0 && whileCol < 8) {
            
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
        while(whileRow >= 0 && whileCol >= 0) {
            
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

    private ArrayList<int[]> rookMoves(Tile tile) {
        
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        Tile[][] boardTiles = board.getBoardTiles();
        
        int row = tile.getRow();
        int col = tile.getCol();

        int whileRow = row + 1;
          
        while(whileRow < 8) {
            
            Tile checkedTile = boardTiles[whileRow][col]; 
            
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() == this.color) {
                break;
            }
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() != this.color) {
                legalMoves.add(new int[]{whileRow, col});
                break;
                }

            legalMoves.add(new int[]{whileRow, col});
            
            whileRow++;
        }

        whileRow = row - 1;
        while(whileRow >= 0) {
            
            Tile checkedTile = boardTiles[whileRow][col]; 
            
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() == this.color) {
                break;
            }
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() != this.color) {
                legalMoves.add(new int[]{whileRow, col});
                break;
                }

            legalMoves.add(new int[]{whileRow, col});

            whileRow--;
        }

        int whileCol = col + 1;
        while(whileCol < 8) {
            
            Tile checkedTile = boardTiles[row][whileCol]; 
            
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() == this.color) {
                break;
            }
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() != this.color) {
                legalMoves.add(new int[]{row, whileCol});
                break;
                }

            legalMoves.add(new int[]{row, whileCol});

            whileCol++;
        }
        
        whileCol = col - 1; 
        while(whileCol >= 0) {
            
            Tile checkedTile = boardTiles[row][whileCol]; 
            
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() == this.color) {
                break;
            }
            if (checkedTile.isOccupied() 
                && checkedTile.getPiece().getColor() != this.color) {
                legalMoves.add(new int[]{row, whileCol});
                break;
                }

            legalMoves.add(new int[]{row, whileCol});

            whileCol--;
        }

        return legalMoves;
    }

    private ArrayList<int[]> queenMoves(Tile tile) {

        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        legalMoves.addAll(this.bishopMoves(tile));
        legalMoves.addAll(this.rookMoves(tile));

        return legalMoves;
        
    }

    private ArrayList<int[]> kingMove(Tile tile) {

        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        Tile[][] boardTiles = board.getBoardTiles();
        King king = (King)tile.getPiece();

        int row = tile.getRow();
        int col = tile.getCol();

        if(!king.hasMoved()) {
            int castleRow = 0;
            if (this.color == 'b') castleRow = 7;
            
            Tile leftCorner = boardTiles[castleRow][0];
            Tile rightCorner = boardTiles[castleRow][7];

            if (leftCorner.isOccupied() 
                && leftCorner.getPiece() instanceof Rook 
                && !leftCorner.getPiece().hasMoved()
                && !boardTiles[castleRow][1].isOccupied()
                && !boardTiles[castleRow][2].isOccupied()
                && !boardTiles[castleRow][3].isOccupied()) {

                    legalMoves.add(new int[]{castleRow, 2});
                
            }
        
            if (rightCorner.isOccupied() 
                && rightCorner.getPiece() instanceof Rook 
                && !rightCorner.getPiece().hasMoved()
                && !boardTiles[castleRow][5].isOccupied()
                && !boardTiles[castleRow][6].isOccupied()) {
                    
                    legalMoves.add(new int[]{castleRow, 6});
            }

        }
        
        for (int boundedRow = row + 1; boundedRow >= row - 1; boundedRow--) {

            if (boundedRow < 8 && boundedRow >= 0) {

                for (int boundedCol = col - 1; boundedCol <= col + 1; boundedCol++) {

                    if (boundedCol >= 0 
                        && boundedCol < 8 
                        && boundedRow != row 
                        && boundedCol != col) {

                            Tile checkedTile = boardTiles[boundedRow][boundedCol];

                            if (checkedTile.isOccupied() 
                                && checkedTile.getPiece().getColor() == this.color) {
                                break;
                            }
                            
                            legalMoves.add(new int[]{boundedRow, boundedCol});
                    }
                }
            }
        }

        return legalMoves;
    }   

    private ArrayList<int[]> knightMove(Tile tile) {

        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        ArrayList<int[]> moveCoordinates = new ArrayList<int[]>();        
        int row = tile.getRow();
        int col = tile.getCol();

        Collections.addAll(moveCoordinates, 
                           new int[]{row + 1, col - 2},
                           new int[]{row + 2, col - 1},
                           new int[]{row + 1, col + 2},
                           new int[]{row + 2, col + 1},
                           new int[]{row - 1, col - 2},
                           new int[]{row - 2, col - 1},
                           new int[]{row - 1, col + 2},
                           new int[]{row - 2, col + 1});

        for (int[] coordinate : moveCoordinates) {
            if (coordinate[0] < 8 && coordinate[0] >= 0 && coordinate[1] < 8 && coordinate[1] >= 0) {
                Tile checkedTile = boardTiles[coordinate[0]][coordinate[1]];
                if (!checkedTile.isOccupied() || checkedTile.getPiece().getColor() != this.color) {
                    legalMoves.add(coordinate);
                }
                
            }
        }

        return legalMoves;
    }
    

    
void makeMove(Tile ourTile, ArrayList<int[]> legalMoves, int[] debugChoice) {
        //Some code to choose which move the player picks from the interface
        Tile[][] boardTiles = board.getBoardTiles();
        Tile newTile = boardTiles[debugChoice[0]][debugChoice[1]];
        
        newTile.setPiece(ourTile.getPiece());
        ourTile.setPiece(null);
    }

    public static void main(String[] args) {
        Board testBoard = new Board();

        MovementPatterns white = new MovementPatterns(testBoard, 'w');
        MovementPatterns black = new MovementPatterns(testBoard, 'b');

        Tile[][] boardTiles = testBoard.getBoardTiles();

        Queen q1 = new Queen("wQ1", 'w');

        boardTiles[3][3].setPiece(q1);
        testBoard.printBoard();
        black.moveHandler(boardTiles[3][3]);
        

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
