package dev.hectora15.warning.gui.screens;

import dev.hectora15.warning.core.GameCore;
import dev.hectora15.warning.gui.input.InputHandler;
import dev.hectora15.warning.gui.loop.GameLoop;
import dev.hectora15.warning.gui.rendering.GameRenderer;
import dev.hectora15.warning.gui.rendering.UIRenderer;
import dev.hectora15.warning.gui.window.GameCanvas;
import dev.hectora15.warning.gui.window.SceneManager;
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

        GameRenderer gameRenderer = new GameRenderer(
                gameCore,
                gameCanvas.getGraphicsContext(),
                uiRenderer
        );

        InputHandler inputHandler = new InputHandler(gameCore, gameRenderer);
        GameLoop gameLoop = new GameLoop(gameCore, gameRenderer, inputHandler);
        inputHandler.setGameLoop(gameLoop);

        // Preparamos la raíz y los eventos
        Pane root = new Pane(gameCanvas.getCanvas());
        inputHandler.setupMouseEvents(root);

        // Arrancamos el motor de físicas y dibujo
        gameLoop.start();

        return new Scene(root);
    }
}