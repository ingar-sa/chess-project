package project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.Node;
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
import project.Files.SaveGame;

/*

it's possible to show exceptions in javafx, e.g. alert class
Examples of javafx code in self-checkout example in school code x   

THE ROWS IN THE JAVAFX CHESSBOARD STARTS WITH 0 AT THE TOP,  NOT THE BOTTOM LIKE OURS!!!!
Translate to our coordinate system -> ourY = 7 - guiY
Translate to their coordinate system -> guiY = 7 - ourY

   Necessary data

    which player has the current turn (currentPlayerTurn) = white
    has a piece already been chosen (hasPieceBeenChosen) = false
    which piece has been chosen (pieceChosen) = null;
    string of legal moves used to draw circles on tiles

    ImageView.onclick() {

        if !hasPieceBeenChosen:

            if !(piece || ourColor):
                do nothing

            get coordinates
            get url // what image the piece is

            send coordinate to Game // Game creates a variable 

            get a string in the form of "123446" representing legal moves
            draw circles on legal moves
        
            hasPieceBeenChosen = true;

            return;

        else:
            //a piece is already chosen
            get coordinates
            (maybe) send coordinates to Game
            
            if coordinate in legal moves:
                en passant and castling needs to move two pieces
                send coordinates of new tile to Game
                move piece from old tile

            Check with game if pawn has promoted 
                fix queen promotion

            hasPieceBeenChosen = false
            currentPlayerTurn = opposite color
            remove all circles drawn
            return;
    }


*/

public class ChessController implements Serializable {

    @FXML
    private MenuItem saveGame;

    @FXML
    private MenuItem loadGame;

    @FXML
    private GridPane tileColors;

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

                tileColors.add(new Rectangle(100, 100, tileColor), col, row); 
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

            pieceHasBeenChosen = false;
            
            System.out.println("Check if the new piece is legal.");

            ImageView moveToImageView = (ImageView)event.getSource();
            String legalMoveId = moveToImageView.getId();
            

            if (!legalMovesStrings.contains(legalMoveId)) 
                return;
            
            chosenPieceImageView.setImage(null);
            moveToImageView.setImage(new Image(chosenPieceSpriteUrl));

            int chosenPieceRow = GridPane.getRowIndex(chosenPieceImageView); 
            int chosenPieceCol = GridPane.getColumnIndex(chosenPieceImageView);
            
            int moveToPieceRow = GridPane.getRowIndex(moveToImageView); 
            int moveToPieceCol = GridPane.getColumnIndex(moveToImageView);
            
            chosenPieceRow = 7 - chosenPieceRow;
            moveToPieceRow = 7 - moveToPieceRow;

            //Retrieves the game state. 0 represents pat, 1 check mate for black and 2 is check mate for white 
            int gameOver = game.updateGameState(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol);

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

        SaveGame saveGame = new SaveGame();
        
        saveGame.WriteObjectToFile(game, "src/main/java/project/Files/savegames/save1.binary");
    }

    @FXML
    public void loadGame() {

        SaveGame loadGame = new SaveGame();
        game = (Game)loadGame.ReadObjectFromFile("src/main/java/project/Files/savegames/save1.binary");
        recreateBoardFromLoadedGame();
        game.chessboard.printBoard();
    }

    @FXML
    private void recreateBoardFromLoadedGame() {

        HashMap<String, String> piecePositions = game.loadedGamePiecesPosition();

        for (String positionId : piecePositions.keySet()) {
            String spriteId = piecePositions.get(positionId);
            ImageView placeSpriteOnImageView = (ImageView)sprites.lookup("#" + positionId);
            if (spriteId != null)
                placeSpriteOnImageView.setImage(new Image("file:src/main/resources/project/" + spriteId));      
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
