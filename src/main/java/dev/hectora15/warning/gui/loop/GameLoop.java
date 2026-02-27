package dev.hectora15.warning.gui.loop;

import javafx.animation.AnimationTimer;
import dev.hectora15.warning.core.GameCore;
import dev.hectora15.warning.core.Player;
import dev.hectora15.warning.enums.PlayerState;
import dev.hectora15.warning.gui.input.InputHandler;
import dev.hectora15.warning.gui.rendering.GameRenderer;

public class GameLoop {

    private final GameCore gameCore;
    private final GameRenderer renderer;
    private final InputHandler inputHandler;
    private final AnimationTimer timer;

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

        double timeStep = 1.0 / 60.0;
        while (accumulator >= timeStep) {
            gameCore.update();
            accumulator -= timeStep;
        }

        Player player = gameCore.getPlayer();

        if (player.getPlayerState() == PlayerState.DEAD) {
            double alpha = 1.0;
            renderer.render(
                    alpha,
                    inputHandler.getDragState(),
                    inputHandler.getLaunchConfig()
            );
            renderer.renderGameOver();
            this.stop();
            return;
        }

        double alpha = accumulator / timeStep;
        renderer.render(
                alpha,
                inputHandler.getDragState(),
                inputHandler.getLaunchConfig()
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

}
