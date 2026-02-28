package dev.hectora15.warning.gui.window;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Warning Game");
        this.stage.setResizable(false);
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }
}