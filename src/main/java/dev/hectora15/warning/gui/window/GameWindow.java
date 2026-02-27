package dev.hectora15.warning.gui.window;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import dev.hectora15.warning.gui.input.InputHandler;

public class GameWindow {

    private final Stage stage;
    private final Pane root;

    public GameWindow(Stage stage, GameCanvas gameCanvas, InputHandler inputHandler) {
        this.stage = stage;
        this.root = new Pane(gameCanvas.getCanvas());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Warning Game");
        stage.setResizable(false);


        inputHandler.setupMouseEvents(root);
    }

    public void show() {
        stage.show();
    }

}