package project.Movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import project.Board.Chessboard;
import project.Board.Tile;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Pawn;
import project.Pieces.Piece;
import project.Pieces.Queen;
import project.Pieces.Rook;

// Husk en passant må skje rett etter motstander har flyttet + queen promotion


public class MovementPatterns {

    private char color;
    private Tile[][] boardTiles;
    

    MovementPatterns (char color) {
        this.color = color;
    }

   
    public ArrayList<int[]> moveHandler(Tile tile) {
        ArrayList<int[]> allMoves = new ArrayList<int[]>();
        Piece piece = tile.getPiece();
        
        if (piece instanceof Pawn) allMoves = pawnMoves(tile);
        else if (piece instanceof Bishop) allMoves = bishopMoves(tile);
        else if (piece instanceof Rook) allMoves = rookMoves(tile);
        else if (piece instanceof Knight) allMoves = knightMoves(tile);
        else if (piece instanceof King) allMoves = kingMoves(tile);
        else if (piece instanceof Queen) allMoves = queenMoves(tile);

        return allMoves;
    }


    private ArrayList<int[]> pawnMoves(Tile tile) {     
        ArrayList<int[]> legalPawnMoves = new ArrayList<int[]>();
        Pawn pawn = (Pawn)tile.getPiece(); 
        int row = tile.getRow();
        int col = tile.getCol();
        int moveNumber = ((Pawn)tile.getPiece()).getMoveNumber();

        int moveDirection = 1; 
        if (this.color == 'b') moveDirection = -1;
        
        Tile inFront  = boardTiles[row+(1*moveDirection)][col];
        Tile twoInFront  = boardTiles[row+(2*moveDirection)][col]; 

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

    private ArrayList<int[]> kingMoves(Tile tile) {

        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

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

    public Tile[][] getBoardTiles() {
        return boardTiles;
    }


    public void setBoardTiles(Tile[][] boardTiles) {
        this.boardTiles = boardTiles;
    }


    private ArrayList<int[]> knightMoves(Tile tile) {

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
        Tile newTile = boardTiles[debugChoice[0]][debugChoice[1]];
        
        newTile.setPiece(ourTile.getPiece());
        ourTile.setPiece(null);
    }

    public char getColor() {
        return this.color;
    }

    public static void main(String[] args) {
        
    }
}