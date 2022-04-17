package project;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import project.Files.SaveBoardState;
import project.Movement.Game;



public class ChessController implements Serializable {

    @FXML
    private Button saveGame;

    @FXML
    private Button loadGame;

    @FXML
    private Button promotePawn;

    @FXML
    private TextField saveNameField;

    @FXML
    private TextField loadNameField;

    @FXML
    private TextField promotionName;

    @FXML
    private GridPane tileColors;
    
    @FXML
    private Label messageDisplay;

    @FXML
    private GridPane sprites;
    
    @FXML
    private ImageView sprite00, sprite01, sprite02, sprite03, sprite04, sprite05, sprite06, sprite07, sprite10, sprite11, sprite12, sprite13, sprite14, sprite15, sprite16, sprite17, sprite20, sprite21, sprite22, sprite23, sprite24, sprite25, sprite26, sprite27, sprite30, sprite31, sprite32, sprite33, sprite34, sprite35, sprite36, sprite37, sprite40, sprite41, sprite42, sprite43, sprite44, sprite45, sprite46, sprite47, sprite50, sprite51, sprite52, sprite53, sprite54, sprite55, sprite56, sprite57, sprite60, sprite61, sprite62, sprite63, sprite64, sprite65, sprite66, sprite67, sprite70, sprite71, sprite72, sprite73, sprite74, sprite75, sprite76, sprite77;
    
    private boolean pieceHasBeenChosen = false;
    private String pawnPromotion = "";
    private String chosenPieceSpriteUrl;
    private String spritesFilePath = "file:src/main/resources/project/";
    private ImageView chosenPieceImageView;
    private ArrayList<String> legalMovesStrings;
    private boolean isPawnPromoted = false;
    private Game game;
    private SaveBoardState saveBoardState;
    private boolean gameIsOver = false;

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

    private void drawCirclesForLegalMoves(ArrayList<String> legalMovesStrings) {
        for (String legalMove : legalMovesStrings) {
            
            ImageView legalMoveImageView = (ImageView)sprites.lookup("#" + legalMove);

            Circle circle = new Circle(10);
            Circle outerRim = new Circle(33);
            Circle inside = new Circle(27);
            Shape donut = Shape.subtract(outerRim, inside);
            donut.setFill(Color.LIGHTGRAY);
            circle.setFill(Color.LIGHTGRAY);
            circle.setOpacity(0.5);
            donut.setOpacity(0.5);
            int row = GridPane.getRowIndex(legalMoveImageView);
            int col = GridPane.getColumnIndex(legalMoveImageView);
            
            if (legalMoveImageView.getImage() != null) {
                tileColors.add(donut, col, row);
                GridPane.setHalignment(donut, HPos.CENTER);
                GridPane.setValignment(donut, VPos.CENTER);
            }
            else {
                tileColors.add(circle, col, row);
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
            }
        }
    }

    @FXML
    private void castling(String newRookPosition) {

        HashMap<String, String> castlingMoves = new HashMap<>() {{
            put("05", "07");
            put("03", "00");
            put("75", "77");
            put("73", "70");
        }};

        if (newRookPosition.length() != 0) {
            
            String oldRookPosition = new String();
            for (String move : castlingMoves.keySet()) {
                if (move.equals(newRookPosition))
                    oldRookPosition = castlingMoves.get(move);                
            }
        
            ImageView rigthCastlingRookOriginalPos = (ImageView)sprites.lookup("#" + oldRookPosition);
            Image sprite = rigthCastlingRookOriginalPos.getImage();
            String urlRook = sprite.getUrl();
            rigthCastlingRookOriginalPos.setImage(null);
            ImageView rigthCastlingRookNewPos = (ImageView)sprites.lookup("#" + newRookPosition);
            rigthCastlingRookNewPos.setImage(new Image(urlRook));
        }
    }

    @FXML
    private void testClick (MouseEvent event) {

        if (isPawnPromoted) {
            messageDisplay.setText("");
            this.isPawnPromoted = false;
        }

        if (!(this.pawnPromotion.equals(""))) return; 
        
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

            drawCirclesForLegalMoves(legalMovesStrings);
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
                    removeCirclesForLegalMoves();
                    return;
                }
                else {
                    this.chosenPieceImageView = (ImageView)event.getSource();
                    Image sprite = this.chosenPieceImageView.getImage();
                    this.chosenPieceSpriteUrl = sprite.getUrl();
                }
                removeCirclesForLegalMoves();
                drawCirclesForLegalMoves(legalMovesStrings);
                return;
            }

            pieceHasBeenChosen = false;

            int chosenPieceRow = GridPane.getRowIndex(chosenPieceImageView); 
            int chosenPieceCol = GridPane.getColumnIndex(chosenPieceImageView);
            chosenPieceRow = 7 - chosenPieceRow;

            if (!legalMovesStrings.contains(legalMoveId)) {
                removeCirclesForLegalMoves(); 
                return;
            }

            String enPassentMove = game.isMoveEnPassent(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol);
            
            //Check if the move was en passent
            if (enPassentMove.length() != 0) {
                ImageView pieceTakenByEnPassent = (ImageView)sprites.lookup("#" + enPassentMove);
                pieceTakenByEnPassent.setImage(null);
            }

            String castlingMove = game.isMoveCastling(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol);
            //Updates the GUI if the move was castling 
            castling(castlingMove);
            
            //Retrieves the game state. 0 represents pat, 1 check mate for black and 2 is check mate for white 
            game.updateGameState(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol);
            chosenPieceImageView.setImage(null);
            moveToImageView.setImage(new Image(chosenPieceSpriteUrl));
            
            removeCirclesForLegalMoves();

            this.pawnPromotion = game.pawnPromotion();

            if (!(this.pawnPromotion.equals(""))) {
                messageDisplay.setText("Pawn promotion. Input must be: bishop, knight, rook or queen.");
                return;
            }

            isGameOver();
        }
    }

    private void removeCirclesForLegalMoves() {

        ObservableList<Node> childeren = tileColors.getChildren();
        ArrayList<Node> circlesToBeRemoved = new ArrayList<Node>();

        for (Node node : childeren) {
            if (!(node instanceof Rectangle)) {
                circlesToBeRemoved.add(node);
            }
        }
        tileColors.getChildren().removeAll(circlesToBeRemoved);
    }

    private void isGameOver() {

        int gameOver = game.checkForGameOver();

        /*
        chosenPieceImageView.setImage(null);
        moveToImageView.setImage(new Image(chosenPieceSpriteUrl));
        */
        
        if (gameOver == Consts.PAT) {
            messageDisplay.setText("Pat");
            gameIsOver = true;
            System.out.println("Pat");
        }
        else if (gameOver == Consts.CHECKMATE_FOR_BLACK) { 
            messageDisplay.setText("Check Mate for Black!");
            gameIsOver = true;
            System.out.println("Check Mate for Black!");
        }
        else if (gameOver == Consts.CHECKMATE_FOR_WHITE) {
            messageDisplay.setText("Check Mate for White!");
            gameIsOver = true;
            System.out.println("Check Mate for White!");
        }
    }

    @FXML
    public void pawnPromotion() {

        messageDisplay.setText("");

        if (pawnPromotion.equals("")) {
            messageDisplay.setText("You have no pawns to promote!");
            return;
        }

        String userInput = promotionName.getText().toLowerCase();
        ImageView pawnImageView = (ImageView)sprites.lookup("#" + this.pawnPromotion);
        char color = (pawnPromotion.charAt(0) == '0') ? 'b' : 'w';
        char pieceType = '\0';
        
        switch (userInput) {
            case "bishop":
                pieceType = 'B';
                break;
            case "knight":
                pieceType = 'K';
                break;
            case "rook":
                pieceType = 'R';
                break;
            case "queen":
                pieceType = 'Q';
                break;
            default:
                messageDisplay.setText( "Not a valid piece! Input must be: bishop, knight, rook or queen."); 
                return;
        }

        int tileRow = 7 - GridPane.getRowIndex(pawnImageView);
        int tileCol = GridPane.getColumnIndex(pawnImageView);
        game.changePieceOnTile(tileRow, tileCol, pieceType, color);

        pawnImageView.setImage(new Image(spritesFilePath + color + pieceType + ".png"));
        promotionName.setText("");

        isPawnPromoted = true;

        this.pawnPromotion = "";

        //this.sprites.getChildren().remove(promotionText);
        isGameOver();
    }

    @FXML
    public void saveGame() {

        messageDisplay.setText("");

        String saveName = this.saveNameField.getText();
        System.out.println(saveName);
        this.saveNameField.setText("");

        if (saveName.equals("") || (saveName.contains("\\")) || (saveName.contains("/"))) {
            messageDisplay.setText("Illegal character(s) in file name!");
            return;
        }

        if (!(this.pawnPromotion.equals(""))) {
            messageDisplay.setText("Promote pawn before saving!");
            return;
        }

        if (gameIsOver) {
            messageDisplay.setText("Cant save a game that is over!");
            return;
        }

        try {   
            saveBoardState.saveGame(saveName, game.getBoardDeepCopyUsingSerialization(), game.getMoveNUmber());
        } 
        catch (IOException e) {
            messageDisplay.setText("Illegal character(s) in file name!");
            System.err.println(e.getStackTrace());
        }
        catch (Exception e) {
            messageDisplay.setText("Illegal character(s) in file name!");
            System.out.println(e.getStackTrace());
        }

    }

    @FXML
    public void loadGame() {

        String fileName = loadNameField.getText();
        String saveGameString = new String();

        try {
            saveGameString = saveBoardState.loadGame(fileName);
        }
        catch (IOException e) {
            messageDisplay.setText("There is no file with that name or the file is corrupted");
            System.out.println("yoyo");
            System.err.println(e.getStackTrace());
        }
        catch (Exception e) {
            messageDisplay.setText("There is no file with that name or the file is corrupted");
            System.out.println("hei");
            System.out.println(e.getStackTrace());
        }

        //game.loadedGamePiecesPosition(saveGameString);

        try {
            game.loadedGamePiecesPosition(saveGameString);
        }
        catch(IllegalArgumentException e) {
            messageDisplay.setText("The formatting for the file is wrong!");
            return;
        }

        saveNameField.setText("");
        messageDisplay.setText("");
        
        //If there is a selected piece, it needs to be reset  
        this.pieceHasBeenChosen = false;
        removeCirclesForLegalMoves();
        placeSprites();
    }

    @FXML
    private void placeSprites() {
        for (String[] tileInfo : game) {
            ImageView tileView = (ImageView)sprites.lookup("#" + tileInfo[0]);
            Image image = (tileInfo[1].equals("")) ? null : new Image(spritesFilePath + tileInfo[1] + ".png");
            tileView.setImage(image);
        }        
    }

    @FXML
    private void initialize()
    {   
        this.game = new Game();
        this.saveBoardState= new SaveBoardState();
        placeSprites();
        colorTiles();
    }


    public static void main(String[] args) {

    }
}
