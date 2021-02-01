package comp1110.ass2.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


// StartMenu class create by Yen Kuo.
public class StartMenu {
    public static Group root = new Group();
    public static Group background = new Group();

    public static ChoiceBox choiceBox = new ChoiceBox();


    /**
     * Making the start menu.
     * start button can change the scene.
     * quit button can quit the game.
     * rule button can display the new scene contain rule image.
     * And a drop-down menu which can select number of players.
     */
    public void startMenu(Stage p, Scene n) {
        menuBackground();
        root.getChildren().add(background);

        //start button
        Button start = new Button();
        startButton(start, p, n);

        //rule button
        Button rule = new Button();
        ruleButton(rule);

        //quit button
        Button quit = new Button();
        quitButton(quit, p);

        // Select players drop-down menu
        Label numOfPlayer = new Label();
        numOfPlayer(numOfPlayer, choiceBox);

        HBox h = new HBox(10);
        h.getChildren().addAll(numOfPlayer, choiceBox);

        // StartMenu vBox
        VBox vBox = new VBox(30);
        //vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutX(Game.VIEWER_WIDTH / 2);
        vBox.setLayoutY(Game.VIEWER_HEIGHT / 2);
        vBox.getChildren().addAll(h, start, rule, quit);
        root.getChildren().add(vBox);

    }

    /**
     * Start button setting
     *
     * @param start A button which can start the game and change to the main scene.
     * @param p     The current stage.
     * @param n     The new scene which are going to change.
     */
    //
    public void startButton(Button start, Stage p, Scene n) {
        start.setText("START GAME");


        // Change to another scene.
        start.setOnAction(e -> checkStart(p, n));

    }

    /**
     * Start confirm dialog reaction setting.
     */
    public boolean start;

    public boolean startOrNot() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Start Game?");
        stage.setMinWidth(300);
        Label label = new Label();
        int x = setNumOfPlayers(choiceBox);
        label.setText("Start the game for " + x + " players?");

        Button yes = new Button("Yes");
        Button no = new Button("No");

        //Clicking yes will set answer to true and close window
        yes.setOnAction(e -> {
            start = true;
            stage.close();
        });
        //Clicking no will set answer to false and close window
        no.setOnAction(e -> {
            start = false;
            stage.close();
        });
        VBox confirm = new VBox(10);
        //Add buttons
        confirm.getChildren().addAll(label, yes, no);
        confirm.setAlignment(Pos.CENTER);
        Scene scene = new Scene(confirm);
        scene.getStylesheets().add(getClass().getResource("assets/Color.css").toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
        return start;
    }

    /**
     * Check whether to start or not.
     */
    public void checkStart(Stage stage, Scene s) {
        // If the boolean is true mean that yes button been clicked, change the scene to main game.
        boolean check = startOrNot();
        if (check) {
            stage.setScene(s);
        }
    }

    /**
     * Rule button setting.
     */
    public void ruleButton(Button rule) {
        rule.setText("GAME RULES");
        // Display the rule image. And display new window of rule.
        rule.setOnAction(e -> ruleImage());
    }

    /**
     * New stage for rule image display.
     */
    public void ruleImage() {
        Group ruleScene = new Group();
        Stage stage = new Stage();
        stage.initModality((Modality.APPLICATION_MODAL));
        Button closeButton = new Button("Back to Game");
        // closeButton will close the current window.
        closeButton.setOnAction(e -> stage.close());
        closeButton.setLayoutX(600);
        closeButton.setLayoutY(750);
        stage.setTitle("Game Rules");
        // Insert new image - rule.jpg.
        ImageView background = new ImageView();
        background.setImage(new Image(String.valueOf(this.getClass().getResource("assets/rule.jpg"))));
        background.setFitWidth(1200);
        background.setFitHeight(800);
        ruleScene.getChildren().addAll(background, closeButton);

        Scene scene = new Scene(ruleScene, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("assets/Color.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Quit button setting.
     */
    public void quitButton(Button quit, Stage p) {
        quit.setText("QUIT");
        // Pop up confirm dialog to check whether to close stage or not for the quit button.
        quit.setOnAction(e -> checkClose(p));
        // Pop up confirm dialog to check whether to close stage or not for the crossing button.
        p.setOnCloseRequest(e -> {
            e.consume();
            checkClose(p);
        });
    }

    /**
     * Check whether to close or not.
     */
    public void checkClose(Stage stage) {
        // check will be true if user click yes in confirmBox().
        // check will be false if user click no in confirmBox().
        boolean check = confirmBox();
        // If user click yes. Close the stage.
        if (check) {
            stage.close();
        }
    }

    /**
     * Quit confirm dialog reaction setting.
     */
    public boolean ans;

    public boolean confirmBox() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Quit Game?");
        stage.setMinWidth(300);
        Label label = new Label();
        label.setText("Are you sure you want to quit the game?");

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

    /**
     * Select players drop-down menu setting.
     */
    public void numOfPlayer(Label numOfPlayer, ChoiceBox choiceBox) {
        numOfPlayer.setText("Number of players :");
        numOfPlayer.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058); -fx-text-fill: white; -fx-border-color: black;");
        // Adding the choice box options. Only 2 - 6 players are allowed.
        choiceBox.getItems().addAll(2, 3, 4, 5, 6);

        // Default value was 2.
        choiceBox.setValue(2);

    }
    /**
     * Setting background image of start menu.
     */
    public void menuBackground() {
        ImageView background = new ImageView();
        background.setImage(new Image(String.valueOf(this.getClass().getResource("assets/tile_back_cover.jpg"))));
        background.setFitHeight(Game.VIEWER_HEIGHT);
        background.setFitWidth(Game.VIEWER_WIDTH);
        background.setOpacity(0.7);
        StartMenu.background.getChildren().add(background);
    }

    /**
     * Reading the value of what user have select on the number of players.
     */
    public static int setNumOfPlayers(ChoiceBox<Integer> Num) {
        // num equal the option which choice box been selected.
        int num = Num.getValue();
        // Pass the value to the main Game class.
        Game.numOfPlayer = num;
        return num;
    }

}
