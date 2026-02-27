package org.example.gui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.example.core.GameCore;
import org.example.core.Player;
import org.example.core.TrapManager;
import org.example.enums.PlayerState;
import org.example.enums.PosPlayer;
import org.example.enums.TrapState;

import java.io.InputStream;

public class Main extends Application {

    private final GameCore gameCore = new GameCore();
    private final double width = gameCore.getBoundWidth();
    private final double height = gameCore.getBoundHeight();
    private final double MAX_DRAG_DISTANCE = 200.0;
    private final double POWER_MULTIPLIER = 0.15;
    TrapManager trapManager = gameCore.getTrapManager();
    private GraphicsContext gc;
    // ANTES: private final Player player = gameCore.getPlayer();
    private Player player = gameCore.getPlayer();
    private boolean isDragging = false;
    private double startDragX, startDragY;
    private double currentMouseX, currentMouseY;
    private Image restartIcon;
    private double restartBtnX;
    private double restartBtnY;
    private final double restartBtnSize = 50;

    private static final String CREDITS = "@HectorA ";

    private Image loadRestartIconOrNull() {
        InputStream in = getClass().getResourceAsStream("/restart.png");
        if (in == null) return null;
        return new Image(in);
    }

    @Override
    public void start(Stage primaryStage) {

        restartIcon = loadRestartIconOrNull();
        if (restartIcon == null) {
            throw new IllegalStateException(
                    " /restart.png not found in classpath"
            );
        }

        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();



        AnimationTimer timer = new AnimationTimer() {
            private final double TIME_STEP = 1.0 / 60.0;
            private long lastTime = 0;
            private double accumulator = 0;

            private void resetClock() {
                lastTime = 0;
                accumulator = 0;
            }

            @Override
            public void handle(long now) {
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

                if (player.getPlayerState() == PlayerState.DEAD) {
                    double alpha = 1.0; // dibuja la posición actual, no la anterior
                    gc.clearRect(0, 0, width, height);
                    renderGraphics(gc, alpha);
                    drawGameOver(gc);
                    this.stop();
                    return;
                }

                double alpha = accumulator / TIME_STEP;
                gc.clearRect(0, 0, width, height);
                renderGraphics(gc, alpha);
            }
        };
        timer.start();

        root.setOnMouseClicked(event -> {
            if (player.getPlayerState() == PlayerState.DEAD) {
                double mouseX = event.getX();
                double mouseY = event.getY();

                if (mouseX >= restartBtnX && mouseX <= restartBtnX + restartBtnSize &&
                        mouseY >= restartBtnY && mouseY <= restartBtnY + restartBtnSize) {

                    gameCore.restart();

                    this.trapManager = gameCore.getTrapManager();
                    this.player = gameCore.getPlayer();

                    this.isDragging = false;
                    timer.start();
                }
            }
        });

        root.setOnMousePressed(event -> {
            isDragging = true;
            startDragX = event.getX();
            startDragY = event.getY();
            currentMouseX = event.getX();
            currentMouseY = event.getY();
        });

        root.setOnMouseDragged(event -> {
            currentMouseX = event.getX();
            currentMouseY = event.getY();
        });

        root.setOnMouseReleased(event -> {
            isDragging = false;

            double rawDeltaX = startDragX - event.getX();
            double rawDeltaY = startDragY - event.getY();

            double currentDragMagnitude = Math.sqrt((rawDeltaX * rawDeltaX) + (rawDeltaY * rawDeltaY));

            double finalForceX, finalForceY;

            if (currentDragMagnitude > MAX_DRAG_DISTANCE) {
                finalForceX = (rawDeltaX / currentDragMagnitude) * MAX_DRAG_DISTANCE;
                finalForceY = (rawDeltaY / currentDragMagnitude) * MAX_DRAG_DISTANCE;
            } else {
                finalForceX = rawDeltaX;
                finalForceY = rawDeltaY;
            }

            if (player.getPosPlayer() != PosPlayer.AIR) {
                player.applyImpulse(finalForceX * POWER_MULTIPLIER, finalForceY * POWER_MULTIPLIER);
            }

            player.setPosPlayer(PosPlayer.AIR);
        });
    }

    private void renderGraphics(GraphicsContext gc, double alpha) {
        //BACKGROUND COLOR
        gc.setFill(Color.rgb(26, 26, 28));
        gc.fillRect(0, 0, width, height);

        // SCORE
        gc.setFill(Color.rgb(40, 42, 45));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 250));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.valueOf(gameCore.getScore()), width / 2, height / 2 + 80);

        double drawX = player.getOldX() + (player.getX() - player.getOldX()) * alpha;
        double drawY = player.getOldY() + (player.getY() - player.getOldY()) * alpha;

        //CREDITS
        gc.setFill(Color.rgb(40, 42, 45));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(CREDITS, 10, height - 10);

        //PLAYER COLOR
        gc.setFill(Color.BLUE);
        gc.fillRect(drawX, drawY, player.getWidth(), player.getHeight());

        drawTrajectory(gc);

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

    private void drawTrajectory(GraphicsContext gc) {
        if (!isDragging) return;

        double rawDeltaX = startDragX - currentMouseX;
        double rawDeltaY = startDragY - currentMouseY;
        double magnitude = Math.sqrt((rawDeltaX * rawDeltaX) + (rawDeltaY * rawDeltaY));

        double simVelX, simVelY;
        if (magnitude > MAX_DRAG_DISTANCE) {
            simVelX = (rawDeltaX / magnitude) * MAX_DRAG_DISTANCE * POWER_MULTIPLIER;
            simVelY = (rawDeltaY / magnitude) * MAX_DRAG_DISTANCE * POWER_MULTIPLIER;
        } else {
            simVelX = rawDeltaX * POWER_MULTIPLIER;
            simVelY = rawDeltaY * POWER_MULTIPLIER;
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
        //gola
    private void drawGameOver(GraphicsContext gc) {
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", width / 2, height / 2 - 40);

        restartBtnX = width / 2 - restartBtnSize / 2;
        restartBtnY = height / 2 + 80;
        gc.drawImage(restartIcon, restartBtnX, restartBtnY, restartBtnSize, restartBtnSize);

        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Restart", width / 2, restartBtnY + restartBtnSize + 30);
    }
}