package dev.hectora15.warning.gui.input;

import dev.hectora15.warning.gui.loop.GameLoop;
import dev.hectora15.warning.gui.rendering.DragState;
import dev.hectora15.warning.gui.rendering.LaunchConfig;
import javafx.scene.layout.Pane;
import dev.hectora15.warning.core.GameCore;
import dev.hectora15.warning.core.Player;
import dev.hectora15.warning.enums.PlayerState;
import dev.hectora15.warning.enums.PosPlayer;
import dev.hectora15.warning.gui.rendering.GameRenderer;

public class InputHandler {

    private final GameCore gameCore;
    private final GameRenderer gameRenderer;
    private GameLoop gameLoop;

    private Player player;
    private boolean isDragging = false;
    private double startDragX;
    private double startDragY;
    private double currentMouseX;
    private double currentMouseY;

    private static final double MAX_DRAG_DISTANCE = 200.0;
    private static final double POWER_MULTIPLIER = 0.15;

    public InputHandler(GameCore gameCore, GameRenderer gameRenderer) {
        this.gameCore = gameCore;
        this.gameRenderer = gameRenderer;
        this.player = gameCore.getPlayer();
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    public void setupMouseEvents(Pane root) {
        root.setOnMouseClicked(event -> handleMouseClick(event.getX(), event.getY()));
        root.setOnMousePressed(event -> handleMousePressed(event.getX(), event.getY()));
        root.setOnMouseDragged(event -> handleMouseDragged(event.getX(), event.getY()));
        root.setOnMouseReleased(event -> handleMouseReleased(event.getX(), event.getY()));
    }

    private void handleMouseClick(double mouseX, double mouseY) {
        if (player.getPlayerState() == PlayerState.DEAD && gameRenderer.isRestartButtonClicked(mouseX, mouseY)) {
                restart();
            }

    }

    private void handleMousePressed(double x, double y) {
        isDragging = true;
        startDragX = x;
        startDragY = y;
        currentMouseX = x;
        currentMouseY = y;
    }

    private void handleMouseDragged(double x, double y) {
        currentMouseX = x;
        currentMouseY = y;
    }

    private void handleMouseReleased(double x, double y) {
        isDragging = false;

        double rawDeltaX = startDragX - x;
        double rawDeltaY = startDragY - y;

        double currentDragMagnitude = Math.sqrt(
                (rawDeltaX * rawDeltaX) + (rawDeltaY * rawDeltaY)
        );

        double finalForceX;
        double finalForceY;

        if (currentDragMagnitude > MAX_DRAG_DISTANCE) {
            finalForceX = (rawDeltaX / currentDragMagnitude) * MAX_DRAG_DISTANCE;
            finalForceY = (rawDeltaY / currentDragMagnitude) * MAX_DRAG_DISTANCE;
        } else {
            finalForceX = rawDeltaX;
            finalForceY = rawDeltaY;
        }

        if (player.getPosPlayer() != PosPlayer.AIR) {
            player.applyImpulse(
                    finalForceX * POWER_MULTIPLIER,
                    finalForceY * POWER_MULTIPLIER
            );
        }

        player.setPosPlayer(PosPlayer.AIR);
    }

    public void restart() {
        gameCore.restart();
        this.player = gameCore.getPlayer();
        this.isDragging = false;

        // Reiniciar el game loop
        if (gameLoop != null) {
            gameLoop.start();
        }
    }

    public DragState getDragState() {
        return new DragState(
                isDragging,
                startDragX,
                startDragY,
                currentMouseX,
                currentMouseY
        );
    }

    public LaunchConfig getLaunchConfig() {
        return new LaunchConfig(
                MAX_DRAG_DISTANCE,
                POWER_MULTIPLIER
        );
    }

}