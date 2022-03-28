package project;

import project.Board.Chessboard;
import project.Board.Tile;
import project.Movement.CheckLegalMoves;
import project.Pieces.Bishop;
import project.Pieces.King;
import project.Pieces.Knight;
import project.Pieces.Pawn;
import project.Pieces.Piece;
import project.Pieces.Queen;
import project.Pieces.Rook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class TerminalChess {
    
    Chessboard chessboard;
    CheckLegalMoves checkLegalMoves;
    Tile[][] currentGamePositionTiles;

    public TerminalChess() {
        Chessboard chessboard = new Chessboard();

        this.chessboard = chessboard;
        Tile[][] currentGamePositionTiles = chessboard.getBoardTiles();
        this.currentGamePositionTiles = currentGamePositionTiles;


        CheckLegalMoves checkLegalMoves = new CheckLegalMoves(currentGamePositionTiles);  
        this.checkLegalMoves = checkLegalMoves;
    }


    
    public void movePieces() {
    
        Scanner userInputForMove = new Scanner(System.in);

        while (true) {
        
            chessboard.printBoard();
            
            HashMap<int[], ArrayList<int[]>> allLegalMovesAfterControl = checkLegalMoves.CheckforCheckMateAndPat();

            if (checkLegalMoves.getGameStatus() != 0) {
                break;
            }

            System.out.println("row: ");
            int xCoordinateForPiece = userInputForMove.nextInt();
            System.out.println("col: "); // Read user input
            int yCoordinateForPiece = userInputForMove.nextInt();
            
            int[] coordinatesForPieceToMove = new int[] {xCoordinateForPiece, yCoordinateForPiece}; 

            Piece pieceToMove  = this.currentGamePositionTiles[xCoordinateForPiece][yCoordinateForPiece].getPiece();

            Set<int[]> allPiecesThatCanMove = allLegalMovesAfterControl.keySet();

            boolean legalPiece = false;

            int [] keyForPieceToMove = null; 

            for (int[] Pieces : allPiecesThatCanMove) {
                if (Arrays.equals(Pieces, coordinatesForPieceToMove)) {
                    keyForPieceToMove = Pieces; 
                    legalPiece = true;
                }
            
            }

            if (!legalPiece) {
                System.out.println("Ulovelig Brikke!");
                continue;
            }

            ArrayList<int[]> legalMovesForPieceToMove = allLegalMovesAfterControl.get(keyForPieceToMove);

            System.out.println("row: ");
            int xCoordinateForMove = userInputForMove.nextInt();
            System.out.println("col: "); // Read user input
            int yCoordinateForMove = userInputForMove.nextInt();
            
            boolean legalMove = false;
            
            if (legalMovesForPieceToMove.size() == 0) {
                System.out.println("Brikken har ingen lovlige trekk!");
                continue;
            }
            else {
                for (int[] aLegalMove : legalMovesForPieceToMove) {
                    if (aLegalMove[0] == xCoordinateForMove && aLegalMove[1] == yCoordinateForMove) {
                        if (pieceToMove instanceof Pawn) {
                            if (pieceToMove.getHasMoved()) {
                                ((Pawn)pieceToMove).setMovedTwoLastTurn(false);
                            }
                            else if (!pieceToMove.getHasMoved() && Math.abs(xCoordinateForMove - xCoordinateForPiece) == 2) {
                                ((Pawn)pieceToMove).setMovedTwoLastTurn(true);
                                ((Pawn)pieceToMove).setMoveNumberEnPassant(checkLegalMoves.getMoveNumber() + 1);
                            }

                            //Legge til fjerning av brikken an passent!!!
                            //Tror dette fikser det
                            if (!currentGamePositionTiles[xCoordinateForMove][yCoordinateForMove].isOccupied()) {
                                if (Math.abs(yCoordinateForMove - yCoordinateForPiece) == 1) {
                                    if (pieceToMove.getColor() == 'w') {
                                        this.currentGamePositionTiles[xCoordinateForMove - 1][yCoordinateForMove].removePiece();
                                    }
                                    else if (pieceToMove.getColor() == 'b') {
                                        this.currentGamePositionTiles[xCoordinateForMove + 1][yCoordinateForMove].removePiece();
                                    }
                                }
                            }

                        } //Håndtering av rokade 
                        else if (pieceToMove instanceof King) {
                            if (!pieceToMove.getHasMoved())  {
                                if (pieceToMove.getColor() == 'w') {
                                    if (yCoordinateForMove == 6) {
                                        Rook castlingRook = ((Rook)this.currentGamePositionTiles[0][7].getPiece());
                                        this.currentGamePositionTiles[0][7].removePiece();
                                        this.currentGamePositionTiles[0][5].setPiece(castlingRook);
                                    }
                                    else if (yCoordinateForMove == 2) {
                                        Rook castlingRook = ((Rook)this.currentGamePositionTiles[0][0].getPiece());
                                        this.currentGamePositionTiles[0][0].removePiece();
                                        this.currentGamePositionTiles[0][3].setPiece(castlingRook);
                                    }
                                }
                                else if (pieceToMove.getColor() == 'b') {
                                    if (yCoordinateForMove == 6) {
                                        Rook castlingRook = ((Rook)this.currentGamePositionTiles[7][7].getPiece());
                                        this.currentGamePositionTiles[7][7].removePiece();
                                        this.currentGamePositionTiles[7][5].setPiece(castlingRook);
                                    }
                                    else if (yCoordinateForMove == 2) {
                                        Rook castlingRook = ((Rook)this.currentGamePositionTiles[7][0].getPiece());
                                        this.currentGamePositionTiles[7][0].removePiece();
                                        this.currentGamePositionTiles[7][3].setPiece(castlingRook);
                                    }
                                }
                            }
                        }
                        
                        pieceToMove.setHasMoved(true);
                        this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForMove].setPiece(pieceToMove);
                        this.currentGamePositionTiles[xCoordinateForPiece][yCoordinateForPiece].removePiece();
                        legalMove = true;
                    }
                }
            }

            if (!legalMove) {
                System.out.println("Ulovlig trekk!");
                continue;
            }

            if (pieceToMove instanceof Pawn && xCoordinateForMove == 7) {

                System.out.println("What to promote to (0 = B, 1 = K, 2 = Q, 3 = Rook): ");
                int queenPromotionPiece = userInputForMove.nextInt();
                

                if (queenPromotionPiece == 0) {
                    Bishop promotionBishop = new Bishop ("wBP", 'w');
                    this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForPiece].setPiece(promotionBishop);
                }

                else if (queenPromotionPiece == 3) {
                    Rook promotionRook = new Rook("wRP", 'w');
                    this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForPiece].setPiece(promotionRook);
                }

                else if (queenPromotionPiece == 2) {
                    Queen promotionQueen = new Queen("wQP", 'w');
                    this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForPiece].setPiece(promotionQueen);
                }

                else if (queenPromotionPiece == 1) {
                    Knight promotionKnight = new Knight("wKP", 'w');
                    this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForPiece].setPiece(promotionKnight);
                }
                
            }

            if (pieceToMove instanceof Pawn && xCoordinateForMove == 0) {

                
                System.out.println("What to promote to (0 = B, 1 = K, 2 = Q, 3 = Rook): ");
                int queenPromotionPiece = userInputForMove.nextInt();
                

                if (queenPromotionPiece == 0) {
                    Bishop promotionBishop = new Bishop ("bBP", 'b');
                    this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForPiece].setPiece(promotionBishop);
                }

                else if (queenPromotionPiece == 3) {
                    Rook promotionRook = new Rook("bRP", 'b');
                    this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForPiece].setPiece(promotionRook);
                }

                else if (queenPromotionPiece == 2) {
                    Queen promotionQueen = new Queen("bQP", 'b');
                    this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForPiece].setPiece(promotionQueen);
                }

                else if (queenPromotionPiece == 1) {
                    Knight promotionKnight = new Knight("bKP", 'b');
                    this.currentGamePositionTiles[xCoordinateForMove][yCoordinateForPiece].setPiece(promotionKnight);
                }

            }
            
            //chessboard.printBoard();
            checkLegalMoves.increaseMoveNumber();
        }

        userInputForMove.close();

        if (checkLegalMoves.getGameStatus() == Consts.CHECKMATE) {
            if (checkLegalMoves.getMoveNumber() % 2 == 1) {
                System.out.println("White won by checkmate!");
            }
            else {
                System.out.println("Black won by checkmate!");
            }
        }
        else {
            System.out.println("Pat!");
        }
        

    }
    

    public static void main(String[] args) {
       TerminalChess game = new TerminalChess();
       game.movePieces();
   }
}
