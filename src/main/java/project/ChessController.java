package project;

import java.time.Period;
import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import project.Pieces.Piece;

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

public class ChessController {


    @FXML
    private GridPane tileColors;

    @FXML
    private GridPane tileGrid;

    @FXML
    private GridPane sprites;
    
    @FXML
    private ImageView sprite00, sprite01, sprite02, sprite03, sprite04, sprite05, sprite06, sprite07, sprite10, sprite11, sprite12, sprite13, sprite14, sprite15, sprite16, sprite17, sprite20, sprite21, sprite22, sprite23, sprite24, sprite25, sprite26, sprite27, sprite30, sprite31, sprite32, sprite33, sprite34, sprite35, sprite36, sprite37, sprite40, sprite41, sprite42, sprite43, sprite44, sprite45, sprite46, sprite47, sprite50, sprite51, sprite52, sprite53, sprite54, sprite55, sprite56, sprite57, sprite60, sprite61, sprite62, sprite63, sprite64, sprite65, sprite66, sprite67, sprite70, sprite71, sprite72, sprite73, sprite74, sprite75, sprite76, sprite77;
    
    
    @FXML
    private Pane tile00, tile01, tile02, tile03, tile04, tile05, tile06, tile07, tile10, tile11, tile12, tile13, tile14, tile15, tile16, tile17, tile20, tile21, tile22, tile23, tile24, tile25, tile26, tile27, tile30, tile31, tile32, tile33, tile34, tile35, tile36, tile37, tile40, tile41, tile42, tile43, tile44, tile45, tile46, tile47, tile50, tile51, tile52, tile53, tile54, tile55, tile56, tile57, tile60, tile61, tile62, tile63, tile64, tile65, tile66, tile67, tile70, tile71, tile72, tile73, tile74, tile75, tile76, tile77;
    
    
    private char currentPlayerTurn = 'w';
    private boolean hasPieceBeenChosen = false;
    private Piece chosenPiece;
    private String pieceSprite;
    private ImageView prevImageView;
    // private boolean turnOver = false;
    private Game game = new Game();
    private ArrayList<String> legalMovesStrings;


    @FXML
    private void testClick (MouseEvent event) {
        if (!hasPieceBeenChosen){
            ImageView iView = (ImageView)event.getSource();
            Image piece;
            
            piece = iView.getImage();
            
            if (piece == null)
                return;

            pieceSprite = piece.getUrl();
            char blackOrWhite =  pieceSprite.charAt(pieceSprite.length() - 6);
            if (blackOrWhite != this.currentPlayerTurn) {
                return;
            }

            int row = GridPane.getRowIndex(iView);
            int col = GridPane.getColumnIndex(iView);
            
            System.out.println(row + " " + col);

            legalMovesStrings = game.getLegalMoves(row, col);
            hasPieceBeenChosen = true;

        }

        ImageView iView = (ImageView)event.getSource();
        String legalMoveId = null;
        
        for (String coordinate : legalMovesStrings) {
            if (coordinate.equals(iView.getId())) {
                legalMoveId = coordinate;
                break;
            }
        }

        if (legalMoveId == null)
            return;
        
        
    }

    private void drawCirclesForLegalMoves(ArrayList<String> legalMovesStrings) {
        for (String legalMove : legalMovesStrings) {
            ImageView legalMoveImageView = (ImageView)sprites.lookup("#" + legalMove);
            Circle circle = new Circle(0, 0, 50);
            
            
            
            int row = GridPane.getRowIndex(legalMoveImageView);
            int col = GridPane.getColumnIndex(legalMoveImageView);

            tileColors.add(circle, col, row);
        }
    }

    private void getCoordinatesFromString() {
        
    }


    private void initialize()
    {
    }


    public static void main(String[] args) {

    }
}
