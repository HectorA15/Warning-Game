package dev.hectora15.warning.gui;

import dev.hectora15.warning.core.GameCore;
import dev.hectora15.warning.gui.input.InputHandler;
import dev.hectora15.warning.gui.loop.GameLoop;
import dev.hectora15.warning.gui.rendering.GameRenderer;
import dev.hectora15.warning.gui.rendering.UIRenderer;
import dev.hectora15.warning.gui.window.GameCanvas;
import dev.hectora15.warning.gui.window.GameWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        GameCore gameCore;
        GameLoop gameLoop;
        InputHandler inputHandler;
        gameCore = new GameCore();

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
                gameCanvas.getGraphicsContext(),
                uiRenderer
        );

        inputHandler = new InputHandler(gameCore, gameRenderer);

        gameLoop = new GameLoop(gameCore, gameRenderer, inputHandler);

        inputHandler.setGameLoop(gameLoop);

        GameWindow gameWindow = new GameWindow(
                primaryStage,
                gameCanvas,
                inputHandler
        );

        // start game
        gameWindow.show();
        gameLoop.start();
    }
}