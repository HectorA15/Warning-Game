package dev.HectorA15.warning.gui.loop;

import javafx.animation.AnimationTimer;
import dev.HectorA15.warning.core.GameCore;
import dev.HectorA15.warning.core.Player;
import dev.HectorA15.warning.enums.PlayerState;
import dev.HectorA15.warning.gui.input.InputHandler;
import dev.HectorA15.warning.gui.rendering.GameRenderer;

public class GameLoop {

    private final GameCore gameCore;
    private final GameRenderer renderer;
    private final InputHandler inputHandler;
    private final AnimationTimer timer;

    private final double TIME_STEP = 1.0 / 60.0;
    private long lastTime = 0;
    private double accumulator = 0;

    public GameLoop(GameCore gameCore, GameRenderer renderer, InputHandler inputHandler) {
        this.gameCore = gameCore;
        this.renderer = renderer;
        this.inputHandler = inputHandler;

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(now);
            }
        };
    }

    private void update(long now) {
        if (lastTime == 0) {
            lastTime = now;
            return;
        }

        double deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;
        accumulator += deltaTime;

        while (accumulator >= TIME_STEP) {
            gameCore.update();
            accumulator -= TIME_STEP;
        }

        Player player = gameCore.getPlayer();

        if (player.getPlayerState() == PlayerState.DEAD) {
            double alpha = 1.0;
            renderer.render(
                    alpha,
                    inputHandler.isDragging(),
                    inputHandler.getStartDragX(),
                    inputHandler.getStartDragY(),
                    inputHandler.getCurrentMouseX(),
                    inputHandler.getCurrentMouseY(),
                    inputHandler.getMaxDragDistance(),
                    inputHandler.getPowerMultiplier()
            );
            renderer.renderGameOver();
            this.stop();
            return;
        }

        double alpha = accumulator / TIME_STEP;
        renderer.render(
                alpha,
                inputHandler.isDragging(),
                inputHandler.getStartDragX(),
                inputHandler.getStartDragY(),
                inputHandler.getCurrentMouseX(),
                inputHandler.getCurrentMouseY(),
                inputHandler.getMaxDragDistance(),
                inputHandler.getPowerMultiplier()
        );
    }

    public void start() {
        resetClock();
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    private void resetClock() {
        lastTime = 0;
        accumulator = 0;
    }

    public AnimationTimer getTimer() {
        return timer;
    }
}