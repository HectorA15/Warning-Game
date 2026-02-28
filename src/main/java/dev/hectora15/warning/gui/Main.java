package dev.hectora15.warning.gui;

import dev.hectora15.warning.gui.screens.MenuScreen;
import dev.hectora15.warning.gui.screens.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage);

        double width = 650;
        double height = 750;

        sceneManager.setScene(MenuScreen.create(sceneManager, width, height));
    }
}