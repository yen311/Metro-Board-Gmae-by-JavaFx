package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A very simple viewer for piece placements in the Metro game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */

// This class are made by Brienna Heisner.
public class Viewer extends Application {
    /* board layout */
    private static final int SQUARE_SIZE = 70;
    private static final int VIEWER_WIDTH = 1024;
    private static final int VIEWER_HEIGHT = 768;
    private static final ArrayList<String> validTiles;

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

    private TextField textField;

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
                        displayTiles.getChildren().add(new DisplayTiles(tileType, x, y));
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

    static class DisplayTiles extends ImageView {
        /**
         * Display images of pieces in the window (anywhere)
         *
         * @param tile the tile code of length 4
         */
        public DisplayTiles(String tile, int xValue, int yValue) {
            setImage(new Image(Viewer.class.getResource(URI_BASE + tile + ".jpg").toString()));
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            int locX = (SQUARE_SIZE * yValue) + (SQUARE_SIZE);
            int locY = (SQUARE_SIZE * xValue) + (SQUARE_SIZE);
            setLayoutX(locX);
            setLayoutY(locY);
        }
    }

    static class Edge1 extends ImageView {
        public Edge1(int stationNumber) {
            //Create Edge 1: (Top of the board) (Stations 1-8)
            setImage(new Image(Viewer.class.getResource(URI_BASE + "station" + stationNumber + ".jpg").toString()));
            setRotate(180);
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            int locX = (SQUARE_SIZE * (8 - (stationNumber))) + SQUARE_SIZE;
            int locY = 0;
            setLayoutX(locX);
            setLayoutY(locY);
        }
    }

    static class Edge2 extends ImageView {
        public Edge2(int stationNumber) {
            //Create Edge 2: (Left of the board) (Stations 9-16)
            setImage(new Image(Viewer.class.getResource(URI_BASE + "station" + (stationNumber + 8) + ".jpg").toString()));
            setRotate(90);
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            int locX = 0;
            int locY = (SQUARE_SIZE * stationNumber);
            setLayoutX(locX);
            setLayoutY(locY);
        }
    }

    static class Edge3 extends ImageView {
        public Edge3(int stationNumber) {
            //Create Edge 3: (Bottom of the board) (Stations 17-24)
            setImage(new Image(Viewer.class.getResource(URI_BASE + "station" + (stationNumber + 16) + ".jpg").toString()));
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            //the column is what differs from each piece
            int locX = (SQUARE_SIZE * stationNumber);
            //the row is on the bottom of the board, or the 10th row
            int locY = SQUARE_SIZE * 9;
            setLayoutX(locX);
            setLayoutY(locY);
        }
    }

    static class Edge4 extends ImageView {
        public Edge4(int stationNumber) {
            //Create Edge 4: (Right of the board) (Stations 25-32)
            setImage(new Image(Viewer.class.getResource(URI_BASE + "station" + (stationNumber + 24) + ".jpg").toString()));
            setRotate(270);
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            int locX = SQUARE_SIZE * 9;
            int locY = (SQUARE_SIZE * (8 - stationNumber)) + SQUARE_SIZE;
            setLayoutX(locX);
            setLayoutY(locY);
        }
    }

    static class MiddleStations extends ImageView {
        public MiddleStations(int station) {
            setImage(new Image(Viewer.class.getResource(URI_BASE + "centre_station.jpg").toString()));
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
            int locX;
            int locY;
            if (station == 0) {
                locX = (4 * SQUARE_SIZE);
                locY = (4 * SQUARE_SIZE);
                setRotate(270);
                setLayoutX(locX);
                setLayoutY(locY);
            } else if (station == 1) {
                locX = (4 * SQUARE_SIZE);
                locY = (5 * SQUARE_SIZE);
                setRotate(180);
                setLayoutX(locX);
                setLayoutY(locY);
            } else if (station == 2) {
                locX = (5 * SQUARE_SIZE);
                locY = (4 * SQUARE_SIZE);
                setLayoutX(locX);
                setLayoutY(locY);
            } else if (station == 3) {
                locX = (5 * SQUARE_SIZE);
                locY = (5 * SQUARE_SIZE);
                setRotate(90);
                setLayoutX(locX);
                setLayoutY(locY);
            }
        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button placebutton = new Button("Place");
        placebutton.setOnAction(e -> {
            makePlacement(textField.getText());
            textField.clear();
        });
        Button refreshbutton = new Button("Refresh");
        refreshbutton.setOnAction(e -> {
            textField.clear();
            displayTiles.getChildren().clear();
        });
        // This is a Horizontal Box that contains the 'Placement' label, textfield and the refresh button
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, placebutton, refreshbutton);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);


    }

    /**
     * Set up the group that represents the graphical elements that make up the game board such as the surrounding
     * stations and the four stations in the middle of the board
     */
    private void makeBoard() {
        board.getChildren().clear();

        Rectangle baseboard = new Rectangle();
        baseboard.setWidth(VIEWER_WIDTH);
        baseboard.setHeight(VIEWER_HEIGHT);
        baseboard.setLayoutX(0);
        baseboard.setLayoutY(0);
        baseboard.setFill(Color.BROWN);
        //Set up the base metro board with brown squares


        board.getChildren().add(baseboard);
        board.getChildren().add(edge1);
        board.getChildren().add(edge2);
        board.getChildren().add(edge3);
        board.getChildren().add(edge4);
        board.getChildren().add(middleStations);
        board.getChildren().add(displayTiles);
    }

    private void makeTiles() {
        //Create Edge 1: (Top of the board) (Stations 1-8)
        edge1.getChildren().clear();
        for (int i = 7; i > (-1); i--) {
            int stationNumber = i + 1;
            edge1.getChildren().add(new Edge1(stationNumber));
        }
        edge2.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            int stationNumber = i + 1;
            edge2.getChildren().add(new Edge2(stationNumber));
        }
        edge3.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            int stationNumber = i + 1;
            edge3.getChildren().add(new Edge3(stationNumber));
        }
        edge4.getChildren().clear();
        for (int i = 7; i > (-1); i--) {
            int stationNumber = i + 1;
            edge4.getChildren().add(new Edge4(stationNumber));
        }
        middleStations.getChildren().clear();
        for (int i = 0; i < 4; i++) {
            middleStations.getChildren().add(new MiddleStations(i));
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FocusGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);


        root.getChildren().add(board);
        root.getChildren().add(controls);
        root.getChildren().add(displayTiles);

        makeBoard();
        makeControls();
        makeTiles();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
