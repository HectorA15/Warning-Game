package dev.hectora15.warning.gui.screens;

import dev.hectora15.warning.core.FileManager;
import dev.hectora15.warning.core.GameCore;
import dev.hectora15.warning.gui.input.InputHandler;
import dev.hectora15.warning.gui.loop.GameLoop;
import dev.hectora15.warning.gui.rendering.GameRenderer;
import dev.hectora15.warning.gui.rendering.UIRenderer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameScreen {
    private GameScreen() {
        /* This utility class should not be instantiated */
    }


    public static Scene create(SceneManager sceneManager) {
        GameCore gameCore = new GameCore();

        FileManager.initialize();

        GameCanvas gameCanvas = new GameCanvas(
                gameCore.getBoundWidth(),
                gameCore.getBoundHeight()
        );

        UIRenderer uiRenderer = new UIRenderer(
                gameCore.getBoundWidth(),
                gameCore.getBoundHeight()
        );

        GameRenderer gameRenderer = new GameRenderer(
                gameCore,
                gameCanvas.getGraphicsContext2D(),
                uiRenderer
        );

        InputHandler inputHandler = new InputHandler(gameCore, gameRenderer);
        GameLoop gameLoop = new GameLoop(gameCore, gameRenderer, inputHandler);
        inputHandler.setGameLoop(gameLoop);

        VBox gameOverOverlay = new VBox(20);
        gameOverOverlay.setAlignment(Pos.CENTER);
        gameOverOverlay.setPrefSize(gameCore.getBoundWidth(), gameCore.getBoundHeight());
        gameOverOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);"); // Fondo oscuro transparente
        gameOverOverlay.setVisible(false); // Invisible al inicio

        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        gameOverText.setFill(Color.WHITE);

        Text scoreText = new Text("Score: 0");
        scoreText.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        scoreText.setFill(Color.WHITE);

        Text highScoreText = new Text("High Score: "+ FileManager.loadHighScore());
        highScoreText.setFont(Font.font("Arial",  FontWeight.NORMAL, 30));
        highScoreText.setFill(Color.YELLOW);

        Button restartBtn = new Button("RESTART");
        restartBtn.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        restartBtn.setStyle("-fx-background-color: #eebb00; -fx-text-fill: black; -fx-padding: 10 30; -fx-cursor: hand; -fx-background-radius: 10;");

        restartBtn.setOnAction(e -> {
            sceneManager.setScene(GameScreen.create(sceneManager));
        });

        gameOverOverlay.getChildren().addAll(gameOverText, scoreText, highScoreText, restartBtn);

        gameLoop.setOnGameOverEvent(() -> {
            int finalScore = gameCore.getScore();


            scoreText.setText("Score:  " + finalScore );

            int currentHighScore = FileManager.loadHighScore();

            if (finalScore > currentHighScore) {
                FileManager.saveHighScore(finalScore);
                highScoreText.setText("New High Score: " + finalScore + " seconds");
            } else {
                highScoreText.setText("High Score: " + currentHighScore + " seconds");
            }

            gameOverOverlay.setVisible(true);


        });

        Pane root = new Pane(gameCanvas, gameOverOverlay);
        inputHandler.setupMouseEvents(root);

        gameLoop.start();

        return new Scene(root, gameCore.getBoundWidth(), gameCore.getBoundHeight());
    }
}