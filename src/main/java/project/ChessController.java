package project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import project.Files.SaveBoardState;


public class ChessController implements Serializable {

    @FXML
    private Button saveGame;

    @FXML
    private Button loadGame;

    @FXML
    private Button promotePawn;

    @FXML
    private Button resetGame;

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
    private ImageView chosenPieceImageView;
    private ArrayList<String> legalMovesStrings;
    private boolean isPawnPromoted = false;
    private Game game;
    private SaveBoardState saveBoardState;
    private boolean gameIsOver = false;

    @FXML
    private void initialize()
    {    
        resetGame();
        saveBoardState = new SaveBoardState();
    }

    @FXML
    private void resetGame() {
        game = new Game();
        gameIsOver = false;
        pawnPromotion = "";
        promotionName.setText("");
        messageDisplay.setText("");
        isPawnPromoted = false;
        placeSprites();
        colorTiles();
    }

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

    private void placeSprites() { 
        for (String[] tileInfo : game) {
            ImageView tileView = (ImageView)sprites.lookup("#" + tileInfo[0]);
            Image image = (tileInfo[1].equals("")) ? null : new Image(getSpriteFilepath() + tileInfo[1] + ".png");
            tileView.setImage(image);
        }        
    }

    @FXML
    private void clickedOnBoard(MouseEvent event) {

        if (gameIsOver) {
            return;
        }

        if (isPawnPromoted) {
            messageDisplay.setText("");
            isPawnPromoted = false;
        }

        if (!(pawnPromotion.equals(""))) return;
        
        messageDisplay.setText("");
        
        if (!pieceHasBeenChosen){
            ImageView pieceImageView = (ImageView)event.getSource();
            Image sprite;
            
            sprite = pieceImageView.getImage();
            
            //Fix for opposite row coordinates
            int col = GridPane.getColumnIndex(pieceImageView);
            int row = GridPane.getRowIndex(pieceImageView); 
            row = 7 - row;

            //if legalMovesString is zero, there is no piece on the tile, and we exit the method
            legalMovesStrings = game.getLegalMoves(row, col);
            if (legalMovesStrings.size() == 0)
                return;

            drawCirclesForLegalMoves(legalMovesStrings);
            pieceHasBeenChosen = true;
            chosenPieceImageView = pieceImageView;
            chosenPieceSpriteUrl = sprite.getUrl();

        }
        else {

            ImageView moveToImageView = (ImageView)event.getSource();
            String legalMoveId = moveToImageView.getId();

            int moveToPieceRow = GridPane.getRowIndex(moveToImageView); 
            int moveToPieceCol = GridPane.getColumnIndex(moveToImageView);
            moveToPieceRow = 7 - moveToPieceRow;

            //If the player clicks on a piece of the same color, it is chosen as the new piece if it can perform any moves
            if (game.allLegalPieces(moveToPieceRow, moveToPieceCol)) {
                legalMovesStrings = game.getLegalMoves(moveToPieceRow, moveToPieceCol);
                if (legalMovesStrings.size() == 0) {
                    pieceHasBeenChosen = false;
                    removeCirclesForLegalMoves();
                    return;
                }
                else {
                    chosenPieceImageView = (ImageView)event.getSource();
                    Image sprite = chosenPieceImageView.getImage();
                    chosenPieceSpriteUrl = sprite.getUrl();
                }
                removeCirclesForLegalMoves();
                drawCirclesForLegalMoves(legalMovesStrings);
                return;
            }

            pieceHasBeenChosen = false;

            int chosenPieceRow = GridPane.getRowIndex(chosenPieceImageView); 
            int chosenPieceCol = GridPane.getColumnIndex(chosenPieceImageView);
            chosenPieceRow = 7 - chosenPieceRow;

            //Exits the method if the tile does not have piece on it
            if (!legalMovesStrings.contains(legalMoveId)) {
                removeCirclesForLegalMoves(); 
                return;
            }

            String pieceMoveInfo = game.moveChosenPiece(chosenPieceRow, chosenPieceCol, moveToPieceRow, moveToPieceCol);

            if (pieceMoveInfo.length() != 0) {
                if (pieceMoveInfo.startsWith("0") || pieceMoveInfo.startsWith("7")) {
                    castling(pieceMoveInfo);
                }
                else {
                    ImageView pieceTakenByEnPassent = (ImageView)sprites.lookup("#" + pieceMoveInfo);
                    pieceTakenByEnPassent.setImage(null);
                }
            }

            chosenPieceImageView.setImage(null);
            moveToImageView.setImage(new Image(chosenPieceSpriteUrl));
            
            removeCirclesForLegalMoves();

            pawnPromotion = game.pawnPromotionStringCoordinates();

            if (!(pawnPromotion.equals(""))) {
                messageDisplay.setText("Write name of piece in right field: bishop, knight, rook or queen.");
                return;
            }

            isGameOver();
        }
    }

    private void castling(String newRookPosition) {

        //Moves the castling rook to the correct location after castling
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
    private void pawnPromotion() {

        messageDisplay.setText("");

        if (gameIsOver) {
            messageDisplay.setText("The game is over, there are no pawns to promote!");
            promotionName.setText("");
            return;
        }

        if (pawnPromotion.equals("")) {
            messageDisplay.setText("You have no pawns to promote!");
            promotionName.setText("");
            return;
        }

        String userInput = promotionName.getText().toLowerCase();
        promotionName.setText("");
        ImageView pawnImageView = (ImageView)sprites.lookup("#" + pawnPromotion);
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
        game.promotePawn(tileRow, tileCol, pieceType, color);

        pawnImageView.setImage(new Image(getSpriteFilepath() + color + pieceType + ".png"));
        promotionName.setText("");

        isPawnPromoted = true;

        pawnPromotion = "";

        isGameOver();
    }

  
    @FXML
    private void saveGame() {

        messageDisplay.setText("");

        String saveName = saveNameField.getText();
        saveNameField.setText("");

        if (saveName.equals("") || (saveName.contains("\\")) || (saveName.contains("/"))) {
            messageDisplay.setText("Illegal character(s) in file name");
            return;
        }

        if (!(pawnPromotion.equals(""))) {
            messageDisplay.setText("Promote pawn before saving");
            return;
        }

        if (gameIsOver) {
            messageDisplay.setText("Can't save a game that is over");
            return;
        }

        try {   
            saveBoardState.saveGame(saveName, game.getBoardTilesDeepCopy(), game.getMoveNumber());
            
        } 
        catch (IOException e) {
            messageDisplay.setText("An IOException occurred");
            System.err.println(e.toString());
            e.printStackTrace();
        }

    }

    @FXML
    private void loadGame() {

        String fileName = loadNameField.getText();
        String saveGameString = new String();
        loadNameField.setText("");

        if (!(pawnPromotion.equals(""))) {
            messageDisplay.setText("Promote pawn before loading game");
            return;
        }

        if (gameIsOver) {
            messageDisplay.setText("Game is over, reset the game");
            return;
        }
        try {
            saveGameString = saveBoardState.loadGame(fileName);

            try {
                game.loadedGamePiecesPosition(saveGameString);
            }
            catch(IllegalArgumentException e) {
                pieceHasBeenChosen = false;
                messageDisplay.setText("The formatting for the file is wrong or the game is over (press reset)");
                return;
            }

            saveNameField.setText("");
            messageDisplay.setText("");

            pieceHasBeenChosen = false;
            removeCirclesForLegalMoves();
            placeSprites();
        }
        catch (FileNotFoundException e) {
            messageDisplay.setText("No such file exists");
            e.printStackTrace();   
        }
        catch (IOException e) {
            messageDisplay.setText("An IOException occurred");
            e.printStackTrace();
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
        
        if (gameOver == Consts.PAT) {
            messageDisplay.setText("Pat");
            gameIsOver = true;
        }
        else if (gameOver == Consts.CHECKMATE_FOR_BLACK) { 
            messageDisplay.setText("Check Mate for Black!");
            gameIsOver = true;
        }
        else if (gameOver == Consts.CHECKMATE_FOR_WHITE) {
            messageDisplay.setText("Check Mate for White!");
            gameIsOver = true;
        }
    }

    private String getSpriteFilepath() {
        String separator = System.getProperty("file.separator"); //Gets correct filepath separator for the OS
		return String.format("file:src%1$smain%1$sresources%1$sproject%1$s", separator);
    }
}
