package project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import project.Files.SaveGames;


public class ChessController implements Serializable {

    @FXML
    private Button saveGame;

    @FXML
    private Button loadGame;

    @FXML
    private GridPane tileColors;

    // @FXML
    // private Menu save;

    // @FXML
    // private Menu load;

    @FXML
    private GridPane sprites;
    
    //Generating the imageviews in javafx like we do with colorTiles() was more cumbersome 
    //than writing a python script to generate the fxml. The script can be found in src/python.
    @FXML
    private ImageView sprite00, sprite01, sprite02, sprite03, sprite04, sprite05, sprite06, sprite07, sprite10, sprite11, sprite12, sprite13, sprite14, sprite15, sprite16, sprite17, sprite20, sprite21, sprite22, sprite23, sprite24, sprite25, sprite26, sprite27, sprite30, sprite31, sprite32, sprite33, sprite34, sprite35, sprite36, sprite37, sprite40, sprite41, sprite42, sprite43, sprite44, sprite45, sprite46, sprite47, sprite50, sprite51, sprite52, sprite53, sprite54, sprite55, sprite56, sprite57, sprite60, sprite61, sprite62, sprite63, sprite64, sprite65, sprite66, sprite67, sprite70, sprite71, sprite72, sprite73, sprite74, sprite75, sprite76, sprite77;
    
    private boolean pieceHasBeenChosen = false;
    private String chosenPieceSpriteUrl;
    private ImageView chosenPieceImageView;
    private ArrayList<String> legalMovesStrings;
    private Game game = new Game();

    @FXML
    private void colorTiles() {

        Color light = new Color(122.0/255, 74.0/255, 61.0/255, 1);
        Color dark =  new Color(81.0/255, 42.0/255, 42.0/255, 1);
        Paint tileColor;

        for (int row = 0; row < tileColors.getRowCount(); ++row ) {
            for (int col = 0; col < tileColors.getColumnCount(); ++col) {

                //Starting color for each row alternates
                tileColor = (row % 2 == 1) ?               
                            (col % 2 == 1) ? light : dark  
                           :(col % 2 == 1) ? dark : light;

                tileColors.add(new Rectangle(70, 70, tileColor), col, row); 
            }
        }
    }

    @FXML
    private void testClick (MouseEvent event) {

        if (!pieceHasBeenChosen){
            ImageView pieceImageView = (ImageView)event.getSource();
            Image sprite;
            
            sprite = pieceImageView.getImage();
            
            //Fix for opposite row coordinates
            int col = GridPane.getColumnIndex(pieceImageView);
            int row = GridPane.getRowIndex(pieceImageView); 
            row = 7 - row;
            
            System.out.println(row + " " + col);

            legalMovesStrings = game.getLegalMoves(row, col);
            if (legalMovesStrings.size() == 0)
                return;
            
            System.out.println("Legal Move!");
            pieceHasBeenChosen = true;
            chosenPieceImageView = pieceImageView;
            chosenPieceSpriteUrl = sprite.getUrl();

        }
        else {
        
            System.out.println("Check if the new piece is legal.");

            ImageView moveToImageView = (ImageView)event.getSource();
            String legalMoveId = moveToImageView.getId();

            int moveToPieceRow = GridPane.getRowIndex(moveToImageView); 
            int moveToPieceCol = GridPane.getColumnIndex(moveToImageView);
            moveToPieceRow = 7 - moveToPieceRow;

            if (game.allLegalPieces(moveToPieceRow, moveToPieceCol)) {
                legalMovesStrings = game.getLegalMoves(moveToPieceRow, moveToPieceCol);
                if (legalMovesStrings.size() == 0) {
                    pieceHasBeenChosen = false;
                    return;
                }
                else {
                    this.chosenPieceImageView = (ImageView)event.getSource();
                    Image sprite = this.chosenPieceImageView.getImage();
                    this.chosenPieceSpriteUrl = sprite.getUrl();
                }
                return;
            }

            pieceHasBeenChosen = false;

            int chosenPieceRow = GridPane.getRowIndex(chosenPieceImageView); 
            int chosenPieceCol = GridPane.getColumnIndex(chosenPieceImageView);
            chosenPieceRow = 7 - chosenPieceRow;

            if (!legalMovesStrings.contains(legalMoveId)) 
                return;

            String enPassentMove = game.isMoveEnPassent(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol);
            
            //Check if the move was en passent
            if (enPassentMove.length() != 0) {
                ImageView pieceTakenByEnPassent = (ImageView)sprites.lookup("#" + enPassentMove);
                pieceTakenByEnPassent.setImage(null);
            }

            String castelingMove = game.isMoveCasteling(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol);
            
            if (castelingMove.length() != 0) {
                //White Castling rigth
                if (castelingMove.equals("05")) {
                    ImageView rigthCastlingRookOriginalPos = (ImageView)sprites.lookup("#" + "07");
                    Image sprite = rigthCastlingRookOriginalPos.getImage();
                    //Hardkode dette også - vi vet jo urlen?
                    String urlRook = sprite.getUrl();
                    rigthCastlingRookOriginalPos.setImage(null);
                    ImageView rigthCastlingRookNewPos = (ImageView)sprites.lookup("#" + "05");
                    rigthCastlingRookNewPos.setImage(new Image(urlRook));
                }
                //White castling left 
                else if (castelingMove.equals("03")) { 
                    ImageView rigthCastlingRookOriginalPos = (ImageView)sprites.lookup("#" + "00");
                    Image sprite = rigthCastlingRookOriginalPos.getImage();
                    //Hardkode dette også?
                    String urlRook = sprite.getUrl();
                    rigthCastlingRookOriginalPos.setImage(null);
                    ImageView rigthCastlingRookNewPos = (ImageView)sprites.lookup("#" + "03");
                    rigthCastlingRookNewPos.setImage(new Image(urlRook));
                }
                //Black castling rigth 
                else if (castelingMove.equals("75")) {
                    ImageView rigthCastlingRookOriginalPos = (ImageView)sprites.lookup("#" + "77");
                    Image sprite = rigthCastlingRookOriginalPos.getImage();
                    //Hardkode dette også?
                    String urlRook = sprite.getUrl();
                    rigthCastlingRookOriginalPos.setImage(null);
                    ImageView rigthCastlingRookNewPos = (ImageView)sprites.lookup("#" + "75");
                    rigthCastlingRookNewPos.setImage(new Image(urlRook));
                }
                //Black castling left 
                else if (castelingMove.equals("73")) {
                    ImageView rigthCastlingRookOriginalPos = (ImageView)sprites.lookup("#" + "70");
                    Image sprite = rigthCastlingRookOriginalPos.getImage();
                    //Hardkode dette også?
                    String urlRook = sprite.getUrl();
                    rigthCastlingRookOriginalPos.setImage(null);
                    ImageView rigthCastlingRookNewPos = (ImageView)sprites.lookup("#" + "73");
                    rigthCastlingRookNewPos.setImage(new Image(urlRook));
                }
            }
            //Retrieves the game state. 0 represents pat, 1 check mate for black and 2 is check mate for white 
            int gameOver = game.updateGameState(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol);

            chosenPieceImageView.setImage(null);
            moveToImageView.setImage(new Image(chosenPieceSpriteUrl));

            if (gameOver == Consts.PAT)
                System.out.println("Pat");
            else if (gameOver == Consts.CHECKMATE_FOR_BLACK) 
                System.out.println("Check Mate for Black.");
            else if (gameOver == Consts.CHECKMATE_FOR_WHITE) 
                System.out.println("Check Mate for White.");
        }
    }


    //This is without a doubt a cool feature to have, but... focusing on oop principles takes precedence
    private void drawCirclesForLegalMoves(ArrayList<String> legalMovesStrings) {
        for (String legalMove : legalMovesStrings) {
            ImageView legalMoveImageView = (ImageView)sprites.lookup("#" + legalMove);
            Circle circle = new Circle(0, 0, 50);
            
            
            
            int row = GridPane.getRowIndex(legalMoveImageView);
            int col = GridPane.getColumnIndex(legalMoveImageView);

            tileColors.add(circle, col, row);
        }
    }

    @FXML
    public void saveGame() {
        game.saveGame();
    }

    @FXML
    public void loadGame() {
        return;
    }

    @FXML
    private void recreateBoardFromLoadedGame() {

        HashMap<String, String> piecePositions = game.loadedGamePiecesPosition();

        for (String positionId : piecePositions.keySet()) {
            String spriteId = piecePositions.get(positionId);
            ImageView placeSpriteOnImageView = (ImageView)sprites.lookup("#" + positionId);
            if (spriteId != null)
                placeSpriteOnImageView.setImage(new Image("file:src/main/resources/project/" + spriteId + ".png"));      
            else
                placeSpriteOnImageView.setImage(null);
        }
    }
        

    @FXML
    private void initialize()
    {
        colorTiles();
    }


    public static void main(String[] args) {

    }
}
