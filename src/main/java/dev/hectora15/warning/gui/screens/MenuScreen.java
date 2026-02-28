package dev.hectora15.warning.gui.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;

public class MenuScreen {
    private MenuScreen() {
        /* This utility class should not be instantiated */
    }


    public static Scene create(SceneManager sceneManager, double width, double height) {

        Text title = new Text("WARNING");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        title.setFill(Color.WHITE);

        Button startBtn = new Button("Play");
        startBtn.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        startBtn.setStyle("-fx-background-color: #eebb00; -fx-text-fill: black; -fx-padding: 10 40; -fx-background-radius: 10; -fx-cursor: hand;");

        Button settingsBtn = new Button();
        Image settingsIcon = new Image(MenuScreen.class.getResourceAsStream("/settings.png"));
        settingsBtn.setGraphic(new ImageView(settingsIcon));
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        Button exitBtn = new Button("EXIT");
        exitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        exitBtn.setStyle("-fx-background-color: #eebb00; -fx-text-fill: black; -fx-padding: 10 40; -fx-background-radius: 10; -fx-cursor: hand;");



        startBtn.setOnAction(e -> {
            sceneManager.setScene(GameScreen.create(sceneManager));
        });

        settingsBtn.setOnAction(e -> {

        });

       exitBtn.setOnAction(e -> {
            System.exit(0);
       });

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1c;");

        VBox vBox = new VBox(50, title, startBtn, exitBtn);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: #1a1a1c;");

        root.setCenter(vBox);
        root.setTop(settingsBtn);
        BorderPane.setAlignment(settingsBtn, Pos.TOP_RIGHT);

        return new Scene(root, width, height);
    }



}