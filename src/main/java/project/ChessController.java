package project;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;


public class ChessController {

    @FXML
    private GridPane sprites;

    @FXML
    private GridPane tileColor;

    private Chess game;
    
    private void initChess() {
        game = new Chess();
    }


}
