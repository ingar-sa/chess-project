package project.Movement;

import java.io.Serializable;
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

public class MovementPatterns implements Serializable {

    private char            color;

    public MovementPatterns (char color) {
        validationOfLegalColor(color);
        this.color = color;
    }

    private void validationOfLegalColor(char color) {
        char[] blackAndWhite= {'b', 'w'};

        boolean legalColor = false;

        for (char c : blackAndWhite) {
            if (c == color);
                legalColor = true;
                break;
        }

        if (!legalColor) {
            throw new IllegalArgumentException("Illegal color for MovementPatterns!");
        }
    }

    ArrayList<int[]> moveHandler(Tile tile, Tile[][] boardTiles, int moveNumber) {

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
        
        Pawn pawn                       = (Pawn)tile.getPiece(); 
        int row                         = tile.getRow();
        int col                         = tile.getCol();
        ArrayList<int[]> legalPawnMoves = new ArrayList<int[]>();
        
        int moveDirection = 1; 
        if (this.color == 'b') moveDirection = -1;
        
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
            && ((Pawn)passantLeft.getPiece()).getMoveNumberEnPassant() - moveNumber == - 1) {
        
            legalPawnMoves.add(new int[]{passantLeft.getRow() + 1 * moveDirection, passantLeft.getCol()});
        }

        if (passantRight != null 
            && passantRight.getPiece() instanceof Pawn
            && passantRight.getPiece().getColor() != this.color 
            && ((Pawn)passantRight.getPiece()).getMovedTwoLastTurn()
            && ((Pawn)passantRight.getPiece()).getMoveNumberEnPassant() - moveNumber == - 1) {

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

            if (leftCorner.isOccupied() 
                && leftCorner.getPiece() instanceof Rook 
                && !leftCorner.getPiece().getHasMoved()
                && !boardTiles[castleRow][1].isOccupied()
                && !boardTiles[castleRow][2].isOccupied()
                && !boardTiles[castleRow][3].isOccupied()) {

                    legalMoves.add(new int[]{castleRow, 2});
                
            }
        
            if (rightCorner.isOccupied() 
                && rightCorner.getPiece() instanceof Rook 
                && !rightCorner.getPiece().getHasMoved()
                && !boardTiles[castleRow][5].isOccupied()
                && !boardTiles[castleRow][6].isOccupied()) {
                    
                    legalMoves.add(new int[]{castleRow, 6});
            }

        }

        //TODO: Add explanatory comments
        for (int boundedRow = row + 1; boundedRow >= row - 1; boundedRow--) {

            if (boundedRow < 8 && boundedRow >= 0) {

                for (int boundedCol = col - 1; boundedCol <= col + 1; boundedCol++) {

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
    
    void makeMove(Tile ourTile, ArrayList<int[]> legalMoves, int[] debugChoice, Tile[][] boardTiles) {
        
        Tile newTile = boardTiles[debugChoice[0]][debugChoice[1]];

        newTile.setPiece(ourTile.getPiece());
        ourTile.setPiece(null);
    }

    public char getColor() {
        return this.color;
    }

    
    public static void main(String[] args) {

        // Piece p = new Pawn("yo", 'w');
        // System.out.println(p instanceof Pawn);
        // Pawn p1 = new Pawn("tt", 'b');
        // System.out.println(p instanceof Piece);
        // p = new Knight("tt", 'b');
        // p1 = new Knight("tt", 'w')
        // Chessboard chessboard = new Chessboard();
        
        // Tile[][] tiles = chessboard.getBoardTiles();
        
        // Rook rook = new Rook("bR1",'b');
        // Rook rook2 = new Rook("bR2",'b');
        // Rook rook3 = new Rook("bR3", 'b');
        // Rook rook4 = new Rook("bR4", 'b');
        // Bishop bishop = new Bishop("bB1", 'b');
        // Bishop bishop2 = new Bishop("wB1", 'w');

        // Pawn enpawn = new Pawn("bP1", 'b');
        // Pawn enpawn2 = new Pawn("wP2", 'w');
        // Pawn pawn3 = new Pawn("bP3", 'b');
        // Pawn wpawn1 = new Pawn("wP2", 'w');
        // Pawn wpawn3 = new Pawn("wP3", 'w');

        // King testKing = new King("wX2", 'w');

        // Queen queen = new Queen("bQ1", 'b');

        // Knight kn1 = new Knight("wK1", 'b');
        // Knight kn2 = new Knight("wK2", 'w');
        // Knight kn3 = new Knight("bK1", 'b');

        // // tiles[0][2].setPiece(rook);
        // //tiles[2][6].setPiece(bishop);
        // //tiles[1][4].setPiece(bishop);
        // tiles[1][0].removePiece();
        // tiles[1][1].removePiece();
        // tiles[1][2].removePiece();
        // tiles[1][3].removePiece();
        // tiles[1][4].removePiece();
        // tiles[1][5].removePiece();
        // tiles[1][6].removePiece();
        // tiles[1][7].removePiece();
        // //tiles[6][8].removePiece();

        // tiles[0][1].removePiece();
        // tiles[0][2].removePiece();
        // tiles[0][3].removePiece();
        // tiles[0][0].removePiece();
        // tiles[0][5].removePiece();
        // tiles[0][6].removePiece();
        // tiles[0][7].removePiece();
        // tiles[0][4].removePiece();
        // // tiles[0][3].removePiece();
        // // tiles[1][1].removePiece();
        // // tiles[1][2].removePiece();
        // // tiles[1][3].removePiece();
        // // tiles[1][4].removePiece();
        // // tiles[1][7].removePiece();
        // // tiles[0][7].removePiece();
        // // tiles[0][7].setPiece(rook);
        // // tiles[7][5].setPiece(rook2);
        // // tiles[1][7].setPiece(rook);
        // // tiles[7][3].setPiece(rook3);

        // // //tiles[4][4].setOccupied(true);
        // // tiles[7][1].removePiece();
        // // tiles[7][2].removePiece();
        // // //tiles[7][3].removePiece();
        // // //tiles[7][5].removePiece();
        // // tiles[7][6].removePiece();
        // // tiles[7][4].removePiece();
        // // tiles[1][0].removePiece();
        // // tiles[7][0].removePiece();

        // //tiles[4][4].setPiece(rook2);
        // // tiles[0][5].setPiece(enpawn);
        // //tiles[2][2].setPiece(bishop);
        // //tiles[2][5].setPiece(testKing);
        // //tiles[1][7].setPiece(rook);
        // //tiles[1][0].setPiece(enpawn);
        // // tiles[1][5].removePiece();
        // // tiles[2][5].removePiece();
        // // tiles[7][2].setPiece(testKing);
        // //tiles[3][5].setPiece(rook2);
        // //tiles[3][7].setPiece(bishop);
        // //tiles[2][5].getPiece().setHasMoved();
        // //((Pawn)tiles[4][5].getPiece()).setMovedTwoLastTurn(true);
        // // ((Pawn)tiles[4][5].getPiece()).setMovedTwo();

        // //tiles[4][4].setPiece(enpawn2);

        
        


                
        // // tiles[0][6].removePiece();
        // tiles[0][5].removePiece();
        // tiles[0][6].removePiece();
        // tiles[0][1].removePiece();
        // tiles[1][3].removePiece();

        // tiles[7][0].removePiece();
        // tiles[7][5].removePiece();
        // tiles[7][6].removePiece();
        // tiles[6][0].removePiece();
        // tiles[6][2].removePiece();
        // tiles[6][5].removePiece();
        
        
        // // tiles[1][4].setPiece(kn1);
        // // tiles[3][0].setPiece(kn2);
        // // tiles[5][5].setPiece(kn3);

        // // tiles[0][5].setPiece(rook);

    
        // // tiles[3][1].setPiece(bishop);
        // // tiles[3][2].setPiece(bishop2);
        
        // // tiles[2][3].setPiece(wpawn1);
        // // tiles[4][4].setPiece(wpawn3);

        // tiles[1][4].setPiece(testKing);
        // tiles[0][6].setPiece(queen);
        // tiles[2][4].setPiece(kn1);
        // tiles[3][1].setPiece(bishop);
        // tiles[4][3].setPiece(rook);
        // tiles[3][6].setPiece(pawn3);

        // tiles[2][2].setPiece(enpawn2);
        // tiles[3][6].setPiece(pawn3);
        // //tiles[0][3].setPiece(queen);
        // tiles[3][4].setPiece(rook3);
    
        // tiles[3][5].setPiece(enpawn2);
        // enpawn2.setMovedTwoLastTurn(true);
        


                
        // tiles[0][6].removePiece();
        // chessboard.printBoard();


        // MovementPatterns white = new MovementPatterns('w');
        // MovementPatterns black = new MovementPatterns('b');
        // white.setBoardTiles(tiles);
        // black.setBoardTiles(tiles);
        // black.moveHandler(tiles[3][6]);

        
    }
    
}
