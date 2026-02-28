package dev.hectora15.warning.gui.screens;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Warning Game");
        this.stage.setResizable(true);
    }

    public void setScreen(Parent newRoot) {
        if (stage.getScene() == null) {
            Scene masterScene = new Scene(newRoot, 630, 720);
            stage.setScene(masterScene);
            stage.show();
        } else {
            stage.getScene().setRoot(newRoot);
        }
    }
}