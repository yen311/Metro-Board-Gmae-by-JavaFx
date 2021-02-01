package comp1110.ass2.gui;


import comp1110.ass2.Metro;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

//import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

import static comp1110.ass2.Metro.*;


/**
 * This class contains the game Metro. It does not work fully at the moment- some issues are: it only contains the card in the players hand and the board, the drag and drop feature does not woork properly (when the tile is dropped onto the board it is able to be moved again to another part of the board, when a user clicks a placed on board tile a new card will replace the tile in the players hand). Also for some reason isPlacementSequenceValid does not return true ever? TODO: add multi-player option, add an opponents code.
 */
// This class are made by Brienna Heisner and Yen Kuo.
public class Game extends Application {
    /* board layout */
    private Stage stage;
    private Scene startScene;
    public final StartMenu s = new StartMenu();
    public static int numOfPlayer;
    private static final int SQUARE_SIZE = 70;
    private static Integer rowIndex = 0;
    private static Integer colIndex = 0;
    public static final int VIEWER_WIDTH = 1024;
    public static final int VIEWER_HEIGHT = 768;
    private static final ArrayList<String> validTiles;
    //initiate gridpane board /placement board
    GridPane gridpane = new GridPane();
    GridPane gridpane2 = gridpane;
    GridPane playersHand = new GridPane();
    GridPane score = new GridPane();

    //set up array lists
    String totalHandsString = String.join(", ", totalHands);
    String placementSequenceString = String.join(", ", placementSequence);
    private static final ArrayList<String> playerHand = new ArrayList<>();
    private static final ArrayList<String> placementSequence = new ArrayList<>();
    private static final ArrayList<String> totalHands = new ArrayList<>();


    private final DataFormat imageFormat = new DataFormat("MyButton");
    private ImageView draggingImage;
    public static int indexOfPlayerHand=0;
    public int lastElemIndex = totalHands.size();

    static {
        validTiles = new ArrayList<>(Arrays.asList("aaaa", "aacb", "acba", "accd", "adad", "adbb", "baac", "badb", "bbad", "bbbb",
                "bcbc", "bcdd", "cbaa", "cbcb", "cccc", "ccda", "cdac", "cddb", "dacc", "dada", "dbba", "dbcd", "ddbc", "dddd"));
    }

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group board = new Group();
    private final Group edge1 = new Group();
    private final Group edge2 = new Group();
    private final Group edge3 = new Group();
    private final Group edge4 = new Group();
    private final Group middleStations = new Group();
    private final Group displayTiles = new Group();

    // Score Label
    public static Label PlayerOneScore = new Label();
    public static Label PlayerTwoScore = new Label();
    public static Label PlayerThreeScore = new Label();
    public static Label PlayerFourScore = new Label();
    public static Label PlayerFiveScore = new Label();
    public static Label PlayerSixScore = new Label();

    public void makePlacement(String placement) {
        //Break a placement sequence string into piece placements.
        ArrayList<String> tileList = new ArrayList<>();
        int length = 6;
        String tile;
        for (int i = 0; i < (placement.length() / length); i++) {
            tile = placement.substring((i * length), (i * length) + length);
            tileList.add(tile);
        }

        //for every tile, view it using displayImage
        for (String currentTile : tileList) {
            if (currentTile.length() == 6) {
                String tileType = currentTile.substring(0, 4);
                if (validTiles.contains(tileType)) {
                    int x = Character.getNumericValue(currentTile.charAt(4));
                    int y = Character.getNumericValue(currentTile.charAt(5));
                    if (validCoordinates(x, y)) {
                        displayTiles.getChildren().add(new DisplayTiles(tileType, (x + 1), (y + 1), 0));
                    } else {
                        throw new IllegalArgumentException("Bad co-ordinates: \"" + x + y + "\"");
                    }
                } else {
                    throw new IllegalArgumentException("Bad tile: \"" + tileType + "\"");
                }
            } else {
                throw new IllegalArgumentException("Bad tile: \"" + currentTile + "\"");
            }
        }
    }


    private boolean validCoordinates(int x, int y) {
        boolean notCentre = (x == 3 && y == 3) || (x == 3 && y == 4) || (x == 4 && y == 3) || (x == 4 && y == 4);
        return (x > -1 && x < 8) && (y > -1 && y < 8) && (!notCentre);
    }

    class DisplayTiles extends ImageView {
        /**
         * Display images of pieces in the window (anywhere)
         *
         * @param tile the tile code of length 4
         */
        public DisplayTiles(String tile, int xValue, int yValue, int rotateValue) {
            Image card = new Image(Viewer.class.getResource(URI_BASE + tile + ".jpg").toString());
            ImageView cardScaled = new ImageView();
            cardScaled.setRotate(rotateValue);
            cardScaled.setFitHeight(SQUARE_SIZE);
            cardScaled.setFitWidth(SQUARE_SIZE);
            cardScaled.setImage(card);
            gridpane.getChildren().add(cardScaled);
            GridPane.setRowIndex(cardScaled, yValue);
            GridPane.setColumnIndex(cardScaled, xValue);
        }
    }

    /**
     * The button on the board.
     */

    public void makeControls() {
        // Rule button
        Button rule = new Button("Game Rules!");
        rule.setOnAction(e -> s.ruleImage());

        // Quit button
        Button quit = new Button("Quit Game");
        s.quitButton(quit, stage);

        VBox v = new VBox(30);

        v.getChildren().addAll(rule, quit);
        controls.getChildren().addAll(v);

    }

    /**
     * Display score label and each player's score.
     */

    public void setScore(int player) {
        // Score Label
        Label scoreLabel = new Label();
        scoreLabel.setText("Score: ");
        // Setting constant font for the label.
        Font label = Font.font(20);
        Font content = Font.font(16);
        scoreLabel.setFont(label);
        PlayerOneScore.setFont(content);
        PlayerTwoScore.setFont(content);
        PlayerThreeScore.setFont(content);
        PlayerFourScore.setFont(content);
        PlayerFiveScore.setFont(content);
        PlayerSixScore.setFont(content);

        // The score list for each player at temp.
        int[] scoreList = Metro.getScore(placementSequenceString, numOfPlayer);
        // Condition of 2 players.
        if (player == 2) {
            PlayerOneScore.setText("PlayerOneScore: " + scoreList[0]);
            PlayerTwoScore.setText("PlayerTwoScore: " + scoreList[1]);
            PlayerOneScore.setFont(content);
            PlayerTwoScore.setFont(content);
        }
        // Condition of 3 players.
        if (player == 3) {
            PlayerOneScore.setText("PlayerOneScore: " + scoreList[0]);
            PlayerTwoScore.setText("PlayerTwoScore: " + scoreList[1]);
            PlayerThreeScore.setText("PlayerThreeScore: " + scoreList[2]);
            PlayerOneScore.setFont(content);
            PlayerTwoScore.setFont(content);
        }
        // Condition of 4 players.
        if (player == 4) {
            PlayerOneScore.setText("PlayerOneScore: " + scoreList[0]);
            PlayerTwoScore.setText("PlayerTwoScore: " + scoreList[1]);
            PlayerThreeScore.setText("PlayerThreeScore: " + scoreList[2]);
            PlayerFourScore.setText("PlayerFourScore: " + scoreList[3]);
            PlayerOneScore.setFont(content);
            PlayerTwoScore.setFont(content);
        }
        // Condition of 5 players.
        if (player == 5) {
            PlayerOneScore.setText("PlayerOneScore: " + scoreList[0]);
            PlayerTwoScore.setText("PlayerTwoScore: " + scoreList[1]);
            PlayerThreeScore.setText("PlayerThreeScore: " + scoreList[2]);
            PlayerFourScore.setText("PlayerFourScore: " + scoreList[3]);
            PlayerFiveScore.setText("PlayerFiveScore: " + scoreList[4]);
            PlayerOneScore.setFont(content);
            PlayerTwoScore.setFont(content);
        }
        // Condition of 6 players.
        if (player == 6) {
            PlayerOneScore.setText("PlayerOneScore: " + scoreList[0]);
            PlayerTwoScore.setText("PlayerTwoScore: " + scoreList[1]);
            PlayerThreeScore.setText("PlayerThreeScore: " + scoreList[2]);
            PlayerFourScore.setText("PlayerFourScore: " + scoreList[3]);
            PlayerFiveScore.setText("PlayerFiveScore: " + scoreList[4]);
            PlayerSixScore.setText("PlayerSixScore: " + scoreList[5]);
            PlayerOneScore.setFont(content);
            PlayerTwoScore.setFont(content);
        }
        // Vbox add all score label in.
        VBox s = new VBox(30);
        s.getChildren().addAll(scoreLabel, PlayerOneScore, PlayerTwoScore, PlayerThreeScore, PlayerFourScore, PlayerFiveScore, PlayerSixScore);
        this.score.getChildren().add(s);
    }


    /**
     * Set up the group that represents the graphical elements that make up the game board such as the surrounding
     * stations and the four stations in the middle of the board
     */
    public void makeBoard() {

        gridpane.setGridLinesVisible(true);
        gridpane.getColumnConstraints().add(new ColumnConstraints(SQUARE_SIZE));
        gridpane.getRowConstraints().add(new RowConstraints(SQUARE_SIZE));
        board.getChildren().addAll(edge1, edge2, edge3, edge4, middleStations);
        gridpane.getChildren().add(board);

        playersHand.setVgap(10);
        playersHand.setHgap(20);
        // Location text on board
        ArrayList<Text> text = new ArrayList();
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                Text t = new Text("( " + Integer.toString(x) + " , " + Integer.toString(y) + " )");
                text.add(t);
            }
        }
        for (Text value : text) {
            value.setFont(Font.font(16));
            value.setStyle("-fx-font-weight: bold");
            GridPane.setHalignment(value, HPos.CENTER);
        }
        gridpane.add(text.get(0), 1, 1);
        gridpane.add(text.get(1), 2, 1);
        gridpane.add(text.get(2), 3, 1);
        gridpane.add(text.get(3), 4, 1);
        gridpane.add(text.get(4), 5, 1);
        gridpane.add(text.get(5), 6, 1);
        gridpane.add(text.get(6), 7, 1);
        gridpane.add(text.get(7), 8, 1);
        gridpane.add(text.get(8), 1, 2);
        gridpane.add(text.get(9), 2, 2);
        gridpane.add(text.get(10), 3, 2);
        gridpane.add(text.get(11), 4, 2);
        gridpane.add(text.get(12), 5, 2);
        gridpane.add(text.get(13), 6, 2);
        gridpane.add(text.get(14), 7, 2);
        gridpane.add(text.get(15), 8, 2);
        gridpane.add(text.get(16), 1, 3);
        gridpane.add(text.get(17), 2, 3);
        gridpane.add(text.get(18), 3, 3);
        gridpane.add(text.get(19), 4, 3);
        gridpane.add(text.get(20), 5, 3);
        gridpane.add(text.get(21), 6, 3);
        gridpane.add(text.get(22), 7, 3);
        gridpane.add(text.get(23), 8, 3);
        gridpane.add(text.get(24), 1, 4);
        gridpane.add(text.get(25), 2, 4);
        gridpane.add(text.get(26), 3, 4);
        gridpane.add(text.get(27), 4, 4);
        gridpane.add(text.get(28), 5, 4);
        gridpane.add(text.get(29), 6, 4);
        gridpane.add(text.get(30), 7, 4);
        gridpane.add(text.get(31), 8, 4);
        gridpane.add(text.get(32), 1, 5);
        gridpane.add(text.get(33), 2, 5);
        gridpane.add(text.get(34), 3, 5);
        gridpane.add(text.get(35), 4, 5);
        gridpane.add(text.get(36), 5, 5);
        gridpane.add(text.get(37), 6, 5);
        gridpane.add(text.get(38), 7, 5);
        gridpane.add(text.get(39), 8, 5);
        gridpane.add(text.get(40), 1, 6);
        gridpane.add(text.get(41), 2, 6);
        gridpane.add(text.get(42), 3, 6);
        gridpane.add(text.get(43), 4, 6);
        gridpane.add(text.get(44), 5, 6);
        gridpane.add(text.get(45), 6, 6);
        gridpane.add(text.get(46), 7, 6);
        gridpane.add(text.get(47), 8, 6);
        gridpane.add(text.get(48), 1, 7);
        gridpane.add(text.get(49), 2, 7);
        gridpane.add(text.get(50), 3, 7);
        gridpane.add(text.get(51), 4, 7);
        gridpane.add(text.get(52), 5, 7);
        gridpane.add(text.get(53), 6, 7);
        gridpane.add(text.get(54), 7, 7);
        gridpane.add(text.get(55), 8, 7);
        gridpane.add(text.get(56), 1, 8);
        gridpane.add(text.get(57), 2, 8);
        gridpane.add(text.get(58), 3, 8);
        gridpane.add(text.get(59), 4, 8);
        gridpane.add(text.get(60), 5, 8);
        gridpane.add(text.get(61), 6, 8);
        gridpane.add(text.get(62), 7, 8);
        gridpane.add(text.get(63), 8, 8);

        //make stackpanes for draggable areas (exclude rows 0 and 9, as well as columns 0 and 9)
        //row1
        StackPane row1Col1 = new StackPane();
        StackPane row1Col2 = new StackPane();
        StackPane row1Col3 = new StackPane();
        StackPane row1Col4 = new StackPane();
        StackPane row1Col5 = new StackPane();
        StackPane row1Col6 = new StackPane();
        StackPane row1Col7 = new StackPane();
        StackPane row1Col8 = new StackPane();
        gridpane.add(row1Col1, 1, 1);
        gridpane.add(row1Col2, 1, 2);
        gridpane.add(row1Col3, 1, 3);
        gridpane.add(row1Col4, 1, 4);
        gridpane.add(row1Col5, 1, 5);
        gridpane.add(row1Col6, 1, 6);
        gridpane.add(row1Col7, 1, 7);
        gridpane.add(row1Col8, 1, 8);
        addDropHandling(row1Col1);
        addDropHandling(row1Col2);
        addDropHandling(row1Col3);
        addDropHandling(row1Col4);
        addDropHandling(row1Col5);
        addDropHandling(row1Col6);
        addDropHandling(row1Col7);
        addDropHandling(row1Col8);
        //row2
        StackPane row2Col1 = new StackPane();
        StackPane row2Col2 = new StackPane();
        StackPane row2Col3 = new StackPane();
        StackPane row2Col4 = new StackPane();
        StackPane row2Col5 = new StackPane();
        StackPane row2Col6 = new StackPane();
        StackPane row2Col7 = new StackPane();
        StackPane row2Col8 = new StackPane();
        gridpane.add(row2Col1, 2, 1);
        gridpane.add(row2Col2, 2, 2);
        gridpane.add(row2Col3, 2, 3);
        gridpane.add(row2Col4, 2, 4);
        gridpane.add(row2Col5, 2, 5);
        gridpane.add(row2Col6, 2, 6);
        gridpane.add(row2Col7, 2, 7);
        gridpane.add(row2Col8, 2, 8);
        addDropHandling(row2Col1);
        addDropHandling(row2Col2);
        addDropHandling(row2Col3);
        addDropHandling(row2Col4);
        addDropHandling(row2Col5);
        addDropHandling(row2Col6);
        addDropHandling(row2Col7);
        addDropHandling(row2Col8);
        //row3
        StackPane row3Col1 = new StackPane();
        StackPane row3Col2 = new StackPane();
        StackPane row3Col3 = new StackPane();
        StackPane row3Col4 = new StackPane();
        StackPane row3Col5 = new StackPane();
        StackPane row3Col6 = new StackPane();
        StackPane row3Col7 = new StackPane();
        StackPane row3Col8 = new StackPane();
        gridpane.add(row3Col1, 3, 1);
        gridpane.add(row3Col2, 3, 2);
        gridpane.add(row3Col3, 3, 3);
        gridpane.add(row3Col4, 3, 4);
        gridpane.add(row3Col5, 3, 5);
        gridpane.add(row3Col6, 3, 6);
        gridpane.add(row3Col7, 3, 7);
        gridpane.add(row3Col8, 3, 8);
        addDropHandling(row3Col1);
        addDropHandling(row3Col2);
        addDropHandling(row3Col3);
        addDropHandling(row3Col4);
        addDropHandling(row3Col5);
        addDropHandling(row3Col6);
        addDropHandling(row3Col7);
        addDropHandling(row3Col8);
        //row4
        StackPane row4Col1 = new StackPane();
        StackPane row4Col2 = new StackPane();
        StackPane row4Col3 = new StackPane();
        StackPane row4Col6 = new StackPane();
        StackPane row4Col7 = new StackPane();
        StackPane row4Col8 = new StackPane();
        gridpane.add(row4Col1, 4, 1);
        gridpane.add(row4Col2, 4, 2);
        gridpane.add(row4Col3, 4, 3);
        gridpane.add(row4Col6, 4, 6);
        gridpane.add(row4Col7, 4, 7);
        gridpane.add(row4Col8, 4, 8);
        addDropHandling(row4Col1);
        addDropHandling(row4Col2);
        addDropHandling(row4Col3);
        addDropHandling(row4Col6);
        addDropHandling(row4Col7);
        addDropHandling(row4Col8);
        //row5
        StackPane row5Col1 = new StackPane();
        StackPane row5Col2 = new StackPane();
        StackPane row5Col3 = new StackPane();
        StackPane row5Col6 = new StackPane();
        StackPane row5Col7 = new StackPane();
        StackPane row5Col8 = new StackPane();
        gridpane.add(row5Col1, 5, 1);
        gridpane.add(row5Col2, 5, 2);
        gridpane.add(row5Col3, 5, 3);
        gridpane.add(row5Col6, 5, 6);
        gridpane.add(row5Col7, 5, 7);
        gridpane.add(row5Col8, 5, 8);
        addDropHandling(row5Col1);
        addDropHandling(row5Col2);
        addDropHandling(row5Col3);
        addDropHandling(row5Col6);
        addDropHandling(row5Col7);
        addDropHandling(row5Col8);
        //row6
        StackPane row6Col1 = new StackPane();
        StackPane row6Col2 = new StackPane();
        StackPane row6Col3 = new StackPane();
        StackPane row6Col4 = new StackPane();
        StackPane row6Col5 = new StackPane();
        StackPane row6Col6 = new StackPane();
        StackPane row6Col7 = new StackPane();
        StackPane row6Col8 = new StackPane();
        gridpane.add(row6Col1, 6, 1);
        gridpane.add(row6Col2, 6, 2);
        gridpane.add(row6Col3, 6, 3);
        gridpane.add(row6Col4, 6, 4);
        gridpane.add(row6Col5, 6, 5);
        gridpane.add(row6Col6, 6, 6);
        gridpane.add(row6Col7, 6, 7);
        gridpane.add(row6Col8, 6, 8);
        addDropHandling(row6Col1);
        addDropHandling(row6Col2);
        addDropHandling(row6Col3);
        addDropHandling(row6Col4);
        addDropHandling(row6Col5);
        addDropHandling(row6Col6);
        addDropHandling(row6Col7);
        addDropHandling(row6Col8);
        //row7
        StackPane row7Col1 = new StackPane();
        StackPane row7Col2 = new StackPane();
        StackPane row7Col3 = new StackPane();
        StackPane row7Col4 = new StackPane();
        StackPane row7Col5 = new StackPane();
        StackPane row7Col6 = new StackPane();
        StackPane row7Col7 = new StackPane();
        StackPane row7Col8 = new StackPane();
        gridpane.add(row7Col1, 7, 1);
        gridpane.add(row7Col2, 7, 2);
        gridpane.add(row7Col3, 7, 3);
        gridpane.add(row7Col4, 7, 4);
        gridpane.add(row7Col5, 7, 5);
        gridpane.add(row7Col6, 7, 6);
        gridpane.add(row7Col7, 7, 7);
        gridpane.add(row7Col8, 7, 8);
        addDropHandling(row7Col1);
        addDropHandling(row7Col2);
        addDropHandling(row7Col3);
        addDropHandling(row7Col4);
        addDropHandling(row7Col5);
        addDropHandling(row7Col6);
        addDropHandling(row7Col7);
        addDropHandling(row7Col8);
        //row8
        StackPane row8Col1 = new StackPane();
        StackPane row8Col2 = new StackPane();
        StackPane row8Col3 = new StackPane();
        StackPane row8Col4 = new StackPane();
        StackPane row8Col5 = new StackPane();
        StackPane row8Col6 = new StackPane();
        StackPane row8Col7 = new StackPane();
        StackPane row8Col8 = new StackPane();
        gridpane.add(row8Col1, 8, 1);
        gridpane.add(row8Col2, 8, 2);
        gridpane.add(row8Col3, 8, 3);
        gridpane.add(row8Col4, 8, 4);
        gridpane.add(row8Col5, 8, 5);
        gridpane.add(row8Col6, 8, 6);
        gridpane.add(row8Col7, 8, 7);
        gridpane.add(row8Col8, 8, 8);
        addDropHandling(row8Col1);
        addDropHandling(row8Col2);
        addDropHandling(row8Col3);
        addDropHandling(row8Col4);
        addDropHandling(row8Col5);
        addDropHandling(row8Col6);
        addDropHandling(row8Col7);
        addDropHandling(row8Col8);
    }


    private void makeTiles() {
        //Create Edge 1: (Top of the board) (Stations 1-8)
        edge1.getChildren().clear();
        for (int i = 7; i > (-1); i--) {
            int stationNumber = i + 1;
            new DisplayTiles(("station" + stationNumber), (8 - i), 0, 180);
        }
        edge2.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            int stationNumber = i + 1;
            new DisplayTiles(("station" + (stationNumber + 8)), 0, stationNumber, 90);
        }
        edge3.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            int stationNumber = i + 1;
            new DisplayTiles(("station" + (stationNumber + 16)), stationNumber, 9, 0);
        }
        edge4.getChildren().clear();
        for (int i = 7; i > (-1); i--) {
            int stationNumber = i + 1;
            new DisplayTiles(("station" + (stationNumber + 24)), 9, (8 - i), 270);
        }
        middleStations.getChildren().clear();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                new DisplayTiles("centre_station", 4, 4, 270);
            } else if (i == 1) {
                new DisplayTiles("centre_station", 4, 5, 180);
            } else if (i == 2) {
                new DisplayTiles("centre_station", 5, 4, 0);
            } else {
                new DisplayTiles("centre_station", 5, 5, 90);
            }
        }
    }


    private void dragImage(ImageView card) {
        card.setOnDragDetected(e -> {
            //Drag was detected, start drap-and-drop gesture
            //Allow any transfer node

            Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(card.snapshot(null, null));
            ClipboardContent content = new ClipboardContent();
            content.put(imageFormat, " ");
            db.setContent(content);
            draggingImage = card;
            e.consume();
        });
        card.setOnDragDone(e -> {
            //removeDragHandling(card);
            draggingImage = null;
            showHand();
        });
    }

    private void removeDragHandling(ImageView card) {
        card.setOnDragDetected(e -> {
        });
    }

    private void removeDropHandling(StackPane pane) {
        pane.setOnDragOver(e -> {
        });
    }
    public boolean ans;
    private void addDropHandling(StackPane pane) {
        pane.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(imageFormat) && draggingImage != null) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        pane.setOnDragDropped(e -> {
            //If there is an image on the dragboard, read it and use it
            Dragboard db = e.getDragboard();
            boolean success = false;
            Node clickedNode = e.getPickResult().getIntersectedNode();

            rowIndex = GridPane.getRowIndex(clickedNode);
            colIndex = GridPane.getColumnIndex(clickedNode);

            String newestTilePlaced = (playerHand.get(indexOfPlayerHand) + (rowIndex - 1) + (colIndex - 1));
            // A confirm dialog for user to check whether the have place tile in their ideal location.
            //int confirm = JOptionPane.showConfirmDialog(null, "Is this the right place? " +
            //        "(" + (rowIndex - 1) + " , " + (colIndex - 1) + ")", "placement", JOptionPane.YES_NO_OPTION);
            boolean check = confirmBox(rowIndex - 1 ,colIndex - 1);
            if (db.hasContent(imageFormat)) {
                // Update placementSequenceString by add the new tile and the location which we put.
                placementSequenceString = placementSequenceString.concat(newestTilePlaced);
                // Determine whether the place is valid.
                if (isPlacementSequenceValid(placementSequenceString)) {
                    // If user choose Yes, drag the image to the right spot. And finished the dragging action.
                    if (check == true) {
                        ((Pane) draggingImage.getParent()).getChildren().remove(draggingImage);
                        pane.getChildren().add(draggingImage);
                        GridPane.setColumnIndex(draggingImage, colIndex);
                        GridPane.setRowIndex(draggingImage, rowIndex);
                        success = true;
                        draggingImage = null;
                        totalHands.remove(playerHand.get(indexOfPlayerHand));
                        playerHand.remove(indexOfPlayerHand);
                        showHand();
                        //remove drop handling for that area
                        removeDropHandling(pane);
                        // Updated the score for each round.
                        setScore(numOfPlayer);

                        //go into the next players move
                        changePlayer(nextPlayer(1, numOfPlayer));
                        makeCompletion(stage, startScene);

                    } else {
                        //If user click No, remove the tile and it's location from the placementSequenceString.
                        placementSequenceString = placementSequenceString.substring(0, placementSequenceString.length() - 6);
                    }
                } else {
                    // If the spot isn't valid. Remove the tile and it's location from the placementSequenceString.
                    placementSequenceString = placementSequenceString.substring(0, placementSequenceString.length() - 6);
                    //Display the invalid location message .
                    if (check == true) {
                        //JOptionPane.showMessageDialog(null, "Here isn't a valid place!! ",
                        //        "Invalid Placement", JOptionPane.WARNING_MESSAGE);
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);

                        stage.setTitle("Warring!");
                        stage.setMinWidth(300);
                        Label label = new Label();
                        System.out.println(placementSequenceString);
                        label.setText("Here isn't valid!");

                        Button OK = new Button("OK");

                        //Clicking OK and close window.
                        OK.setOnAction(e1 -> {
                            stage.close();
                        });
                        VBox confirm = new VBox(10);
                        //Add buttons to vbox.
                        confirm.getChildren().addAll(label, OK);
                        confirm.setAlignment(Pos.CENTER);
                        Scene scene = new Scene(confirm);
                        scene.getStylesheets().add(getClass().getResource("assets/Color.css").toExternalForm());
                        stage.setScene(scene);
                        stage.showAndWait();
                    }
                }
            }
            // Display who's the winner.
            //makeCompletion(stage, startScene);
            //let the source know whether the image was successfully transferred and used
            e.setDropCompleted(success);
            e.consume();
        });
    }

    // Confirm the placement location.
    public boolean confirmBox(int x, int y) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("Confirm");
        stage.setMinWidth(300);
        Label label = new Label();
        System.out.println(placementSequenceString);
        label.setText("Is ( "  + x + " , " + y + " ) "  +"the right place?");

        Button yes = new Button("Yes");
        Button no = new Button("No");

        //Clicking yes will set answer to true and close window.
        yes.setOnAction(e -> {
            ans = true;
            stage.close();
        });
        //Clicking no will set answer to false and close window.
        no.setOnAction(e -> {
            ans = false;
            stage.close();
        });

        VBox confirm = new VBox(10);
        //Add buttons to vbox.
        confirm.getChildren().addAll(label, yes, no);
        confirm.setAlignment(Pos.CENTER);
        Scene scene = new Scene(confirm);
        scene.getStylesheets().add(getClass().getResource("assets/Color.css").toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
        return ans;
    }

    public void changePlayer(int currentPlayer) {
        if (currentPlayer == 1) {
            showHand();
        } else {
            //draw card from deck and add it to the totalHands array
            totalHands.add(drawFromDeck(placementSequenceString, totalHandsString));
            lastElemIndex = totalHands.size();
            String tileType = totalHands.get(lastElemIndex - 1);
            String makeMove = generateMove(placementSequenceString.substring(0, placementSequenceString.length()), tileType, numOfPlayer);
            // Update placementSequenceString by add the new tile and the location which we put.
            placementSequenceString = placementSequenceString.concat(makeMove);


            int x = Character.getNumericValue(makeMove.charAt(4));
            int y = Character.getNumericValue(makeMove.charAt(5));
            displayTiles.getChildren().add(new DisplayTiles(tileType, y + 1, x + 1, 0));
            //add it the board placement sequence but remove it from the total hands
            placementSequence.add(makeMove);

            totalHands.remove(lastElemIndex - 1);
            //move to next player
            changePlayer( nextPlayer(currentPlayer, numOfPlayer));
        }
    }






    private void showHand() {

        if (placementSequenceString.length() < 360) {
            //add a label for the player's hand
            Label PlayerHand = new Label();
            PlayerHand.setText("Your Hand: ");
            PlayerHand.setFont(Font.font(20));
            playersHand.getChildren().add(PlayerHand);
            playersHand.setRowIndex(PlayerHand, 1);
            playersHand.setColumnIndex(PlayerHand, 1);

            //if the playerhand is empty, add a card to it
            if (playerHand.isEmpty()) {
                playerHand.add(drawFromDeck(placementSequenceString, totalHandsString));
                totalHands.add(playerHand.get(0));
            }

            //show the tile at the appropriate x and y value that is just to the right of the board.
            Image playersCard = new Image(Viewer.class.getResource(URI_BASE + (playerHand.get(0)) + ".jpg").toString());
            ImageView playerCard = new ImageView();
            playerCard.setFitHeight(SQUARE_SIZE);
            playerCard.setFitWidth(SQUARE_SIZE);
            playerCard.setImage(playersCard);
            playersHand.getChildren().add(playerCard);
            playersHand.setRowIndex(playerCard, 2);
            playersHand.setColumnIndex(playerCard, 1);
            //only allow the first card to drag if it is the only card in hand
            if (playerHand.size() == 1) {
                indexOfPlayerHand = 0;
                dragImage(playerCard);
            }


            //add a button to draw a card from deck
            Button addCardButton = new Button();
            addCardButton.setText("Draw New Card");
            addCardButton.setFont(Font.font(16));
            addCardButton.setStyle("-fx-text-fill: white;");
            addCardButton.setMaxSize(100, 200);
            playersHand.getChildren().add(addCardButton);
            playersHand.setRowIndex(addCardButton, 3);
            playersHand.setColumnIndex(addCardButton, 1);

            // The label for new tile.
            Label newTile = new Label();
            newTile.setText("New Tile: ");
            newTile.setFont(Font.font(20));
            playersHand.getChildren().add(newTile);
            playersHand.setRowIndex(newTile, 4);
            playersHand.setColumnIndex(newTile, 1);


            //add the mouse handling for this button
            addCardButton.setOnMouseClicked(e -> {
                if (playerHand.size() == 1) {
                    //make the other card undraggable
                    removeDragHandling(playerCard);


                    //add a new card at (0,3) for the playersHand gridpane
                    playerHand.add(drawFromDeck(placementSequenceString, totalHandsString));
                    totalHands.add(playerHand.get(1));
                    if (playerHand.size() == 2) {
                        //show the tile at the appropriate x and y value that is just to the right of the board.
                        Image playersSecondCard = new Image(Viewer.class.getResource(URI_BASE + (playerHand.get(1)) + ".jpg").toString());
                        ImageView playerSecondCard = new ImageView();
                        playerSecondCard.setFitHeight(SQUARE_SIZE);
                        playerSecondCard.setFitWidth(SQUARE_SIZE);
                        playerSecondCard.setImage(playersSecondCard);
                        playersHand.getChildren().add(playerSecondCard);
                        playersHand.setRowIndex(playerSecondCard, 5);
                        playersHand.setColumnIndex(playerSecondCard, 1);
                        indexOfPlayerHand = 1;
                        dragImage(playerSecondCard);
                    }
                }
            });
        }
    }

    public int highestPlayer() {
        int[] scoreList = Metro.getScore(placementSequenceString, numOfPlayer);
        int player = 0;
        int highest = scoreList[0];
        for (int i = 1; i < scoreList.length; i++) {
            if (scoreList[i] > highest) {
                highest = scoreList[i];
                player = i;
            }
        }
        return player + 1;
    }

    /**
     * Create the message to be displayed when all 60 tiles are placed on the board
     */
    private void makeCompletion(Stage stage, Scene scene) {
        //Create the new stage and display the winner's message.
        if (placementSequenceString.length() == 360) {
            highestPlayer();
            // New stage to display message.
            Stage sc = new Stage();
            // Message of who's the winner.
            Label label = new Label();
            label.setText("Player " + highestPlayer() + " is the winner!!");
            // Quit button
            Button quit = new Button("QUIT GAME");
            // Quit button will close both message stage and game stage.
            quit.setOnAction(e -> {
                sc.close();
                stage.close();
            });
            // Restart the game and back to StartMenu, but it's not functional correctly.
            Button restart = new Button("Restart");
            restart.setOnAction(e -> {
                placementSequenceString = "";
                gridpane = gridpane2;
                stage.setScene(scene);
                sc.close();

            });

            HBox h = new HBox(30);
            h.getChildren().addAll(quit);
            h.setAlignment(Pos.CENTER);
            VBox v = new VBox(30);
            v.getChildren().addAll(label, h);
            v.setAlignment(Pos.CENTER);

            Scene sc1 = new Scene(v, 300, 150);
            sc1.getStylesheets().add(getClass().getResource("assets/Color.css").toExternalForm());
            sc.setTitle("GAME END!");
            sc.setScene(sc1);
            sc.showAndWait();
        }
    }
    /**
     * Restarts the game and resets everything as necessary
     */
    public void reStart() {

    }

    private void newGame(String placement) {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Metro Game");
        // The start menu scene.
        Scene start = new Scene(StartMenu.root, VIEWER_WIDTH, VIEWER_HEIGHT);
        startScene = start;
        startScene.getStylesheets().add(getClass().getResource("assets/Color.css").toExternalForm());
        // The main game scene.
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("assets/Color.css").toExternalForm());
        scene.setFill(Color.PAPAYAWHIP);
        // Switching scene from start menu to main game with the start button and startMenu function in class StartMenu.
        s.startMenu(primaryStage, scene);

        setScore(numOfPlayer);
        VBox v = new VBox(200);
        v.getChildren().addAll(score, controls);
        v.setAlignment(Pos.TOP_LEFT);


        HBox hBox = new HBox(25);

        hBox.setLayoutY(30);
        hBox.getChildren().addAll(playersHand, gridpane, v);
        root.getChildren().add(hBox);

        makeControls();
        makeBoard();
        makePlacement(placementSequenceString);
        makeTiles();
        showHand();

        newGame(placementSequenceString);

        primaryStage.setScene(start);
        primaryStage.show();
    }
}
