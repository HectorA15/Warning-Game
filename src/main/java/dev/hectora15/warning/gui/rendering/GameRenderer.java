package dev.hectora15.warning.gui.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import dev.hectora15.warning.core.GameCore;
import dev.hectora15.warning.core.Player;
import dev.hectora15.warning.core.TrapManager;
import dev.hectora15.warning.enums.TrapState;

public class GameRenderer {

    private static final String CREDITS = "@HectorA ";

    private final GameCore gameCore;
    private final GraphicsContext gc;
    private final UIRenderer uiRenderer;

    private final double width;
    private final double height;

    public GameRenderer(GameCore gameCore, GraphicsContext gc, UIRenderer uiRenderer) {
        this.gameCore = gameCore;
        this.gc = gc;
        this.uiRenderer = uiRenderer;
        this.width = gameCore.getBoundWidth();
        this.height = gameCore.getBoundHeight();
    }

    public void render(double alpha, DragState dragState, LaunchConfig launchConfig) {
        gc.clearRect(0, 0, width, height);

        // Background
        gc.setFill(Color.rgb(26, 26, 28));
        gc.fillRect(0, 0, width, height);

        // Score
        gc.setFill(Color.rgb(40, 42, 45));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 250));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.valueOf(gameCore.getScore()), width / 2, height / 2 + 80);

        // Credits
        gc.setFill(Color.rgb(40, 42, 45));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(CREDITS, 10, height - 10);

        // Player
        Player player = gameCore.getPlayer();
        double drawX = player.getOldX() + (player.getX() - player.getOldX()) * alpha;
        double drawY = player.getOldY() + (player.getY() - player.getOldY()) * alpha;

        gc.setFill(Color.BLUE);
        gc.fillRect(drawX, drawY, player.getWidth(), player.getHeight());

        // Trajectory
        if (dragState.isDragging()) {
            drawTrajectory(gc, player, dragState, launchConfig);
        }

        // Traps
        TrapManager trapManager = gameCore.getTrapManager();
        trapManager.getActiveTraps().forEach(trap -> {
            if (trap.getState() == TrapState.WARNING) {
                gc.setFill(Color.YELLOW);
            } else if (trap.getState() == TrapState.ACTIVE) {
                gc.setFill(Color.RED);
            } else {
                gc.setFill(Color.GRAY);
            }
            gc.fillRect(trap.getX(), trap.getY(), trap.getWidth(), trap.getHeight());
        });
    }

    private void drawTrajectory(GraphicsContext gc, Player player, DragState dragState,
                                LaunchConfig launchConfig) {
        double rawDeltaX = dragState.getStartX() - dragState.getCurrentX();
        double rawDeltaY = dragState.getStartY() - dragState.getCurrentY();
        double magnitude = Math.sqrt((rawDeltaX * rawDeltaX) + (rawDeltaY * rawDeltaY));

        double simVelX;
        double simVelY;
        if (magnitude > launchConfig.getMaxDragDistance()) {
            simVelX = (rawDeltaX / magnitude) * launchConfig.getMaxDragDistance()
                    * launchConfig.getPowerMultiplier();
            simVelY = (rawDeltaY / magnitude) * launchConfig.getMaxDragDistance()
                    * launchConfig.getPowerMultiplier();
        } else {
            simVelX = rawDeltaX * launchConfig.getPowerMultiplier();
            simVelY = rawDeltaY * launchConfig.getPowerMultiplier();
        }

        double simX = player.getX() + player.getWidth() / 2;
        double simY = player.getY() + player.getHeight() / 2;
        double simulatedGravity = 0.5;

        gc.setFill(Color.GRAY.deriveColor(0, 1, 1, 0.5));
        double dotSize = 5;

        for (int i = 0; i < 30; i++) {
            simVelY += simulatedGravity;
            simX += simVelX;
            simY += simVelY;

            gc.fillOval(simX - dotSize / 2, simY - dotSize / 2, dotSize, dotSize);

            if (simY > gameCore.getBoundHeight()) break;
        }
    }

    public void renderGameOver() {
        uiRenderer.drawGameOver(gc);
    }

    public boolean isRestartButtonClicked(double mouseX, double mouseY) {
        return uiRenderer.isRestartButtonClicked(mouseX, mouseY);
    }
}