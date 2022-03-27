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


public class TileMovementPatterns {

    private char color;
    private Tile[][] boardTiles;
    private TileCheckLegalMoves checkLegalMoves;
    

    TileMovementPatterns (char color, TileCheckLegalMoves checkLegalMoves) {
        this.color = color;
        this.checkLegalMoves = checkLegalMoves;
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
        //int moveNumber = ((Pawn)tile.getPiece()).getMoveNumber();

        int moveDirection = 1; 
        if (this.color == 'b') moveDirection = -1;
        
        Tile inFront  = boardTiles[row+(1*moveDirection)][col];
        //Legger til 

        Tile twoInFront = null;

        if (!pawn.getHasMoved()) {
             twoInFront  = boardTiles[row + (2 * moveDirection)][col];
        }

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
            && ((((Pawn)passantLeft.getPiece()).getmoveNumberEnPassant() - checkLegalMoves.getMoveNUmber()) == 0))
        {
            legalPawnMoves.add(new int[]{passantLeft.getRow() + 1 * moveDirection, passantLeft.getCol()});
        }

        if (passantRight != null 
            && passantRight.getPiece() instanceof Pawn
            && passantRight.getPiece().getColor() != this.color 
            && ((Pawn)passantRight.getPiece()).getMovedTwoLastTurn()
            && ((((Pawn)passantRight.getPiece()).getmoveNumberEnPassant() - checkLegalMoves.getMoveNUmber()) == 0))

        {
            legalPawnMoves.add(new int[]{passantRight.getRow() + 1 * moveDirection, passantRight.getCol()});
        } 

        return legalPawnMoves;
    }
    
    private ArrayList<int[]> bishopMoves(Tile tile) {
        
        ArrayList<int[]> allMoves = new ArrayList<int[]>();

        int row = tile.getRow();
        int col = tile.getCol();
        
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

        return removeFriendlyTiles(allMoves);
    }

    private ArrayList<int[]> rookMoves(Tile tile) {
        
        ArrayList<int[]> allMoves = new ArrayList<int[]>();

        int row = tile.getRow();
        int col = tile.getCol();
        
        int whileRow = row + 1;
        while (whileRow < 8) {
            if (boardTiles[whileRow][col].isOccupied()) {
                allMoves.add(new int[]{whileRow, col});
                break;
            }
            allMoves.add(new int[]{whileRow, col});
            
            ++whileRow;
        }
            
        whileRow = row - 1;
        while (whileRow >= 0) {
            if (boardTiles[whileRow][col].isOccupied()) {
                allMoves.add(new int[]{whileRow, col});
                break;
            }
            allMoves.add(new int[]{whileRow, col});
            
            --whileRow;
        }

        int whileCol = col + 1;
        while (whileCol < 8) {
            if (boardTiles[row][whileCol].isOccupied()) {
                allMoves.add(new int[]{row, whileCol});
                break;
            }
            allMoves.add(new int[]{row, whileCol});
            
            ++whileCol;
        }

        whileCol = col - 1;
        while (whileCol >= 0) {
            if (boardTiles[row][whileCol].isOccupied()) {
                allMoves.add(new int[]{row, whileCol});
                break;
            }
            allMoves.add(new int[]{row, whileCol});
            
            --whileCol;
        }

        return removeFriendlyTiles(allMoves);
    }

    private ArrayList<int[]> removeFriendlyTiles(ArrayList<int[]> allMoves) {
         ArrayList<int[]> legalMoves = new ArrayList<int[]>();

         for (int[] move : allMoves) {
            Tile checkIfLegalTile = boardTiles[move[0]][move[1]]; 

            if (checkIfLegalTile.isOccupied() && checkIfLegalTile.getPiece().getColor() == this.color)
                continue;

            legalMoves.add(move);
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

        if(!king.getHasMoved()) {
            int castleRow = 0;
            if (this.color == 'b') castleRow = 7;
            
            Tile leftCorner = boardTiles[castleRow][0];
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
        Chessboard chessboard = new Chessboard();
        
        Tile[][] tiles = chessboard.getBoardTiles();
        
        Rook rook = new Rook("bR1",'b');
        Rook rook2 = new Rook("bR2",'b');
        Rook rook3 = new Rook("bR3", 'b');
        Rook rook4 = new Rook("bR4", 'b');
        Bishop bishop = new Bishop("bB1", 'b');
        Bishop bishop2 = new Bishop("wB1", 'w');

        Pawn enpawn = new Pawn("bP1", 'b');
        Pawn enpawn2 = new Pawn("wP2", 'w');
        Pawn pawn3 = new Pawn("bP3", 'b');
        Pawn wpawn1 = new Pawn("wP2", 'w');
        Pawn wpawn3 = new Pawn("wP3", 'w');

        King testKing = new King("wX2", 'w');

        Queen queen = new Queen("bQ1", 'b');

        Knight kn1 = new Knight("wK1", 'b');
        Knight kn2 = new Knight("wK2", 'w');
        Knight kn3 = new Knight("bK1", 'b');

        // tiles[0][2].setPiece(rook);
        //tiles[2][6].setPiece(bishop);
        //tiles[1][4].setPiece(bishop);
        tiles[1][0].removePiece();
        tiles[1][1].removePiece();
        tiles[1][2].removePiece();
        tiles[1][3].removePiece();
        tiles[1][4].removePiece();
        tiles[1][5].removePiece();
        tiles[1][6].removePiece();
        tiles[1][7].removePiece();
        //tiles[6][8].removePiece();

        tiles[0][1].removePiece();
        tiles[0][2].removePiece();
        tiles[0][3].removePiece();
        tiles[0][0].removePiece();
        tiles[0][5].removePiece();
        tiles[0][6].removePiece();
        tiles[0][7].removePiece();
        tiles[0][4].removePiece();
        // tiles[0][3].removePiece();
        // tiles[1][1].removePiece();
        // tiles[1][2].removePiece();
        // tiles[1][3].removePiece();
        // tiles[1][4].removePiece();
        // tiles[1][7].removePiece();
        // tiles[0][7].removePiece();
        // tiles[0][7].setPiece(rook);
        // tiles[7][5].setPiece(rook2);
        // tiles[1][7].setPiece(rook);
        // tiles[7][3].setPiece(rook3);

        // //tiles[4][4].setOccupied(true);
        // tiles[7][1].removePiece();
        // tiles[7][2].removePiece();
        // //tiles[7][3].removePiece();
        // //tiles[7][5].removePiece();
        // tiles[7][6].removePiece();
        // tiles[7][4].removePiece();
        // tiles[1][0].removePiece();
        // tiles[7][0].removePiece();

        //tiles[4][4].setPiece(rook2);
        // tiles[0][5].setPiece(enpawn);
        //tiles[2][2].setPiece(bishop);
        //tiles[2][5].setPiece(testKing);
        //tiles[1][7].setPiece(rook);
        //tiles[1][0].setPiece(enpawn);
        // tiles[1][5].removePiece();
        // tiles[2][5].removePiece();
        // tiles[7][2].setPiece(testKing);
        //tiles[3][5].setPiece(rook2);
        //tiles[3][7].setPiece(bishop);
        //tiles[2][5].getPiece().setHasMoved();
        //((Pawn)tiles[4][5].getPiece()).setMovedTwoLastTurn(true);
        // ((Pawn)tiles[4][5].getPiece()).setMovedTwo();

        //tiles[4][4].setPiece(enpawn2);

        
        


                
        // tiles[0][6].removePiece();
        tiles[0][5].removePiece();
        tiles[0][6].removePiece();
        tiles[0][1].removePiece();
        tiles[1][3].removePiece();

        tiles[7][0].removePiece();
        tiles[7][5].removePiece();
        tiles[7][6].removePiece();
        tiles[6][0].removePiece();
        tiles[6][2].removePiece();
        tiles[6][5].removePiece();
        
        
        // tiles[1][4].setPiece(kn1);
        // tiles[3][0].setPiece(kn2);
        // tiles[5][5].setPiece(kn3);

        // tiles[0][5].setPiece(rook);

    
        // tiles[3][1].setPiece(bishop);
        // tiles[3][2].setPiece(bishop2);
        
        // tiles[2][3].setPiece(wpawn1);
        // tiles[4][4].setPiece(wpawn3);

        tiles[1][4].setPiece(testKing);
        tiles[0][6].setPiece(queen);
        tiles[2][4].setPiece(kn1);
        tiles[3][1].setPiece(bishop);
        tiles[4][3].setPiece(rook);
        tiles[3][6].setPiece(pawn3);

        tiles[2][2].setPiece(enpawn2);
        tiles[3][6].setPiece(pawn3);
        //tiles[0][3].setPiece(queen);
        tiles[3][4].setPiece(rook3);
    
        tiles[3][5].setPiece(enpawn2);
        enpawn2.setMovedTwoLastTurn(true);
        


                
        tiles[0][6].removePiece();
        chessboard.printBoard();


        MovementPatterns white = new MovementPatterns('w');
        MovementPatterns black = new MovementPatterns('b');
        white.setBoardTiles(tiles);
        black.setBoardTiles(tiles);
        black.moveHandler(tiles[3][6]);

        
    }
}
