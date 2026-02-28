package dev.hectora15.warning.gui.screens;

import dev.hectora15.warning.core.GameCore;
import dev.hectora15.warning.gui.input.InputHandler;
import dev.hectora15.warning.gui.loop.GameLoop;
import dev.hectora15.warning.gui.rendering.GameRenderer;
import dev.hectora15.warning.gui.rendering.UIRenderer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameScreen {

    public static Scene create(SceneManager sceneManager) {
        GameCore gameCore = new GameCore();

        GameCanvas gameCanvas = new GameCanvas(
                gameCore.getBoundWidth(),
                gameCore.getBoundHeight()
        );

        UIRenderer uiRenderer = new UIRenderer(
                gameCore.getBoundWidth(),
                gameCore.getBoundHeight()
        );

        // CORRECCIÓN 1: gameCanvas.getGraphicsContext2D() en minúscula
        GameRenderer gameRenderer = new GameRenderer(
                gameCore,
                gameCanvas.getGraphicsContext2D(),
                uiRenderer
        );

        InputHandler inputHandler = new InputHandler(gameCore, gameRenderer);
        GameLoop gameLoop = new GameLoop(gameCore, gameRenderer, inputHandler);
        inputHandler.setGameLoop(gameLoop);

        // CORRECCIÓN 2: Le pasamos gameCanvas directamente al Pane
        Pane root = new Pane(gameCanvas);
        inputHandler.setupMouseEvents(root);

        gameLoop.start();

        // CORRECCIÓN 3: Forzamos el tamaño de la escena para que no se deforme
        return new Scene(root, gameCore.getBoundWidth(), gameCore.getBoundHeight());
    }
}