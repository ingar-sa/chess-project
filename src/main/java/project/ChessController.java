package project;

import java.time.Period;

import javax.crypto.spec.IvParameterSpec;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/*

it's possible to show exceptions in javafx, e.g. alert class
Examples of javafx code in self-checkout example in school code x   

THE ROWS IN THE JAVAFX CHESSBOARD STARTS WITH 0 AT THE TOP,  NOT THE BOTTOM LIKE OURS!!!!
Translate to our coordinate system -> ourY = 7 - guiY
Translate to their coordinate system -> guiY = 7 - ourY

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
    
    
    private String pieceSprite;
    private ImageView prevImageView;
    private boolean turnOver = false;


    @FXML
    void testClick(MouseEvent event) {
        
        ImageView iView = (ImageView)event.getSource();
        Image piece;
        try {
            piece = iView.getImage();

        } catch (NullPointerException e) {
            return;
        }

        //file:/C:/School/oop/TDT4100_prosjekt_ingara/target/classes/project/sprites/wPawn.png
        pieceSprite =  piece.getUrl();

        // Send which tile was clicked to Game and get the legal moves in return.

        }



    public ChessController()
    {
    }

    private void initialize()
    {
    }
}
