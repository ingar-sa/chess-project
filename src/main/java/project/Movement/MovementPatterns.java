package project.Movement;

import java.util.ArrayList;
import java.util.Collections;

import project.Board.Tile;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Pawn;
import project.Pieces.Piece;
import project.Pieces.Queen;
import project.Pieces.Rook;

public class MovementPatterns {

    private char color;

    public MovementPatterns (char color) {
        if (color == 'w' || color == 'b') this.color = color;
        else
            throw new IllegalArgumentException("Illegal color");
    }

    public char getColor() {
        return this.color;
    }

    public ArrayList<int[]> moveHandler(Tile tile, Tile[][] boardTiles, int moveNumber) {

        if (tile == null || boardTiles == null) 
            throw new IllegalArgumentException("Input can't be null");
        
        if (!(boardTiles.length == 8 && boardTiles[0].length == 8 && moveNumber >= 0))
            throw new IllegalArgumentException("Board size must be 8x8 and moveNumber must be greater than or equal to 0");
        
        Piece piece               = tile.getPiece();
        ArrayList<int[]> allMoves = new ArrayList<int[]>();
        
        if      (piece instanceof Pawn)   allMoves = pawnMoves(tile, boardTiles, moveNumber);
        else if (piece instanceof Bishop) allMoves = bishopMoves(tile, boardTiles);
        else if (piece instanceof Rook)   allMoves = rookMoves(tile, boardTiles);
        else if (piece instanceof Knight) allMoves = knightMoves(tile, boardTiles);
        else if (piece instanceof King)   allMoves = kingMoves(tile, boardTiles);
        else if (piece instanceof Queen)  allMoves = queenMoves(tile, boardTiles);

        return allMoves;
    }

    private ArrayList<int[]> pawnMoves(Tile tile, Tile[][] boardTiles, int moveNumber) {   
          
        Pawn             pawn           = (Pawn)tile.getPiece(); 
        int              row            = tile.getRow();
        int              col            = tile.getCol();
        ArrayList<int[]> legalPawnMoves = new ArrayList<int[]>();
        
        int moveDirection = 1; 
        if (this.color == 'b') moveDirection = -1; //If the player is black, we subtract instead of adding to the coordinates, so the piece moves downwards
        
        Tile inFront  = boardTiles[row+(1*moveDirection)][col];
        Tile twoInFront = null;

        if (!pawn.getHasMoved()) 
             twoInFront  = boardTiles[row + (2 * moveDirection)][col];
        
        Tile attackLeft = null;
        Tile attackRight = null;
         
        if (!(col == 0)) 
            attackLeft = boardTiles[row+(1*moveDirection)][col-1];
        

        if (!(col == 7)) 
            attackRight = boardTiles[row+(1*moveDirection)][col+1];
        
        Tile passantLeft = null; 
        Tile passantRight = null;
        
        if (this.color == 'w' && row == 4 && col != 0) 
            passantLeft = boardTiles[row][col-1];

        if (this.color == 'b' && row == 3 && col != 0) 
            passantLeft = boardTiles[row][col-1];

        if (this.color == 'w' && row == 4 && col != 7) 
            passantRight = boardTiles[row][col+1];
    
        if (this.color == 'b' && row == 3 && col != 7) 
            passantRight = boardTiles[row][col+1];
        
        if (!inFront.isOccupied()) 
            legalPawnMoves.add(new int[]{inFront.getRow(), inFront.getCol()});
        
        if (!pawn.getHasMoved() && twoInFront != null) {
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
            && passantLeft.getPiece() instanceof Pawn
            && passantLeft.getPiece().getColor() != this.color
            && ((Pawn)passantLeft.getPiece()).getMovedTwoLastTurn()
            && ((Pawn)passantLeft.getPiece()).getMoveNumberEnPassant() - moveNumber == -1) { //En passent can only be performed if the opponents pawn moved last turn
        
            legalPawnMoves.add(new int[]{passantLeft.getRow() + 1 * moveDirection, passantLeft.getCol()});
        }

        if (passantRight != null 
            && passantRight.getPiece() instanceof Pawn
            && passantRight.getPiece().getColor() != this.color 
            && ((Pawn)passantRight.getPiece()).getMovedTwoLastTurn()
            && ((Pawn)passantRight.getPiece()).getMoveNumberEnPassant() - moveNumber == -1) {

            legalPawnMoves.add(new int[]{passantRight.getRow() + 1 * moveDirection, passantRight.getCol()});
        } 

        return legalPawnMoves;
    }
    
    private ArrayList<int[]> bishopMoves(Tile tile, Tile[][] boardTiles) {
        
        ArrayList<int[]> allMoves = new ArrayList<int[]>();

        int row = tile.getRow();
        int col = tile.getCol();

        //Up-right diagonal
        int whileRow = row + 1;
        int whileCol = col + 1;
        while (whileRow < 8 && whileCol < 8) {
            if (boardTiles[whileRow][whileCol].isOccupied()) {
                allMoves.add(new int[]{whileRow, whileCol});
                break;
            }
            allMoves.add(new int[]{whileRow, whileCol});
            
            ++whileRow;
            ++whileCol;
        }

        //Up-left diagonal
        whileRow = row + 1;
        whileCol = col - 1;
        while (whileRow < 8 && whileCol >= 0) {
            if (boardTiles[whileRow][whileCol].isOccupied()) {
                allMoves.add(new int[]{whileRow, whileCol});
                break;
            }
            allMoves.add(new int[]{whileRow, whileCol});
            
            ++whileRow;
            --whileCol;
        }

        //Down-right diagonal
        whileRow = row - 1;
        whileCol = col + 1;
        while (whileRow >= 0 && whileCol < 8) {
            if (boardTiles[whileRow][whileCol].isOccupied()) {
                allMoves.add(new int[]{whileRow, whileCol});
                break;
            }
            allMoves.add(new int[]{whileRow, whileCol});

            --whileRow;
            ++whileCol;
        }

         //Down-left diagonal
         whileRow = row - 1;
         whileCol = col - 1;
         while (whileRow >= 0 && whileCol >= 0) {
             if (boardTiles[whileRow][whileCol].isOccupied()) {
                 allMoves.add(new int[]{whileRow, whileCol});
                 break;
             }
             allMoves.add(new int[]{whileRow, whileCol});
                  
             --whileRow;
             --whileCol;
         } 

        return removeFriendlyTiles(allMoves, boardTiles);
    }

    private ArrayList<int[]> rookMoves(Tile tile, Tile[][] boardTiles) {
        
        ArrayList<int[]> allMoves = new ArrayList<int[]>();

        int row = tile.getRow();
        int col = tile.getCol();
        

        //Up
        int whileRow = row + 1;
        while (whileRow < 8) {
            if (boardTiles[whileRow][col].isOccupied()) {
                allMoves.add(new int[]{whileRow, col});
                break;
            }
            allMoves.add(new int[]{whileRow, col});
            
            ++whileRow;
        }
        
        //Down
        whileRow = row - 1;
        while (whileRow >= 0) {
            if (boardTiles[whileRow][col].isOccupied()) {
                allMoves.add(new int[]{whileRow, col});
                break;
            }
            allMoves.add(new int[]{whileRow, col});
            
            --whileRow;
        }

        //Left
        int whileCol = col - 1;
        while (whileCol >= 0) {
            if (boardTiles[row][whileCol].isOccupied()) {
                allMoves.add(new int[]{row, whileCol});
                break;
            }
            allMoves.add(new int[]{row, whileCol});
            
            --whileCol;
        }

        //Right
        whileCol = col + 1;
        while (whileCol < 8) {
            if (boardTiles[row][whileCol].isOccupied()) {
                allMoves.add(new int[]{row, whileCol});
                break;
            }
            allMoves.add(new int[]{row, whileCol});
            
            ++whileCol;
        }

        return removeFriendlyTiles(allMoves, boardTiles);
    }

    
    private ArrayList<int[]> removeFriendlyTiles(ArrayList<int[]> allMoves, Tile[][] boardTiles) {
        
        //This saves having to check if a piece is of the same color in every loop in bishop and rook moves
         ArrayList<int[]> legalMoves = new ArrayList<int[]>();

         for (int[] move : allMoves) {
            Tile checkIfFriendlyTile = boardTiles[move[0]][move[1]]; 

            if (checkIfFriendlyTile.isOccupied() && checkIfFriendlyTile.getPiece().getColor() == this.color)
                continue;

            legalMoves.add(move);
        }

        return legalMoves;
    } 

    private ArrayList<int[]> queenMoves(Tile tile, Tile[][] boardTiles) {

        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        legalMoves.addAll(this.bishopMoves(tile, boardTiles));
        legalMoves.addAll(this.rookMoves(tile, boardTiles));

        return legalMoves;
            
    }

    private ArrayList<int[]> kingMoves(Tile tile, Tile[][] boardTiles) {

        ArrayList<int[]> legalMoves = new ArrayList<int[]>();

        King king = (King)tile.getPiece();

        int row = tile.getRow();
        int col = tile.getCol();

        if(!king.getHasMoved()) {
            int castleRow = 0;
            if (this.color == 'b') castleRow = 7;
            
            Tile leftCorner  = boardTiles[castleRow][0];
            Tile rightCorner = boardTiles[castleRow][7];
        
            if (leftCorner.isOccupied() //Castling to the left
                && leftCorner.getPiece() instanceof Rook 
                && !leftCorner.getPiece().getHasMoved()
                && !boardTiles[castleRow][1].isOccupied()
                && !boardTiles[castleRow][2].isOccupied()
                && !boardTiles[castleRow][3].isOccupied()) {

                    legalMoves.add(new int[]{castleRow, 2});
                
            }
        
            if (rightCorner.isOccupied() //Castling to the right
                && rightCorner.getPiece() instanceof Rook 
                && !rightCorner.getPiece().getHasMoved()
                && !boardTiles[castleRow][5].isOccupied()
                && !boardTiles[castleRow][6].isOccupied()) {
                    
                    legalMoves.add(new int[]{castleRow, 6});
            }

        }

        //Traverses all of tiles in the 3x3 area around the king, row by row, starting at the one above it
        for (int boundedRow = row + 1; boundedRow >= row - 1; boundedRow--) {
            
            //If the row is inside the board, traverse the tiles in the row
            if (boundedRow < 8 && boundedRow >= 0) {
                for (int boundedCol = col - 1; boundedCol <= col + 1; boundedCol++) {
                    
                    //if the tile is inside the board, check if the king can move to it
                    if (boundedCol >= 0 && boundedCol < 8) {

                            Tile checkedTile = boardTiles[boundedRow][boundedCol];

                            if (checkedTile.isOccupied() 
                                && !(checkedTile.getPiece().getColor() == this.color)) {
                                    legalMoves.add(new int[]{boundedRow, boundedCol});
                            }
                            
                            if (!checkedTile.isOccupied()) {
                                    legalMoves.add(new int[]{boundedRow, boundedCol});
                            }
                    }
                }
            }
        }

        return legalMoves;
    } 
    
    private ArrayList<int[]> knightMoves(Tile tile, Tile[][] boardTiles) {

        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        ArrayList<int[]> moveCoordinates = new ArrayList<int[]>();        
        int row = tile.getRow();
        int col = tile.getCol();

        //Create an arraylist with coordinates for all possible 
        //moves for the knight, even if they are out of bounds
        Collections.addAll(moveCoordinates, 
                           new int[]{row + 1, col - 2},
                           new int[]{row + 2, col - 1},
                           new int[]{row + 1, col + 2},
                           new int[]{row + 2, col + 1},
                           new int[]{row - 1, col - 2},
                           new int[]{row - 2, col - 1},
                           new int[]{row - 1, col + 2},
                           new int[]{row - 2, col + 1});

        //Only add coordinates that are inside the chessboard, and is 
        //either unoccupied or occupied by a piece of the opposite color 
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

    //TODO: Slett!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static void arraylistStringForm(ArrayList<int[]> allMoves) {

        String stringOfAllMoves = new String();
        for (int[] move : allMoves) {
            stringOfAllMoves += "new " + "int[]" + "{" + move[0] + ", " + move[1] + "}" + ", ";
        }
    
        System.out.println(stringOfAllMoves);
        }
    
    public static void makeBoard(Tile[][] boardTiles) {
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {

                Tile tile = new Tile(row, col);
                boardTiles[row][col] = tile;
            }
        }
    }      
}
