package dev.hectora15.warning.gui.screens;

import dev.hectora15.warning.gui.window.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class MenuScreen {

    public static Scene create(SceneManager sceneManager, double width, double height) {

        Text title = new Text("WARNING");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        title.setFill(Color.WHITE);

        Button startBtn = new Button("PLAY");
        startBtn.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        startBtn.setStyle("-fx-background-color: #eebb00; -fx-text-fill: black; -fx-padding: 10 40; -fx-background-radius: 10; -fx-cursor: hand;");


        startBtn.setOnAction(e -> {
            sceneManager.setScene(GameScreen.create(sceneManager));
        });

        VBox root = new VBox(50, title, startBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a1c;");

        return new Scene(root, width, height);
    }
}