package dev.HectorA15.warning.gui;

import dev.HectorA15.warning.gui.window.GameCanvas;
import javafx.application.Application;
import javafx.stage.Stage;
import dev.HectorA15.warning.core.GameCore;
import dev.HectorA15.warning.gui.input.InputHandler;
import dev.HectorA15.warning.gui.loop.GameLoop;
import dev.HectorA15.warning.gui.rendering.GameRenderer;
import dev.HectorA15.warning.gui.rendering.UIRenderer;
import dev.HectorA15.warning.gui.window.GameWindow;

public class Main extends Application {

    private GameCore gameCore;
    private GameLoop gameLoop;
    private InputHandler inputHandler;

    @Override
    public void start(Stage primaryStage) {
        // Inicializar componentes
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

        // Conectar el gameLoop al inputHandler para que pueda reiniciarlo
        inputHandler.setGameLoop(gameLoop);

        GameWindow gameWindow = new GameWindow(
                primaryStage,
                gameCanvas,
                inputHandler,
                gameCore.getBoundWidth(),
                gameCore.getBoundHeight()
        );

        // start game
        gameWindow.show();
        gameLoop.start();
    }
}