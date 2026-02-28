package dev.hectora15.warning.gui.rendering;

import dev.hectora15.warning.traps.SpikeTrap;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import dev.hectora15.warning.core.GameCore;
import dev.hectora15.warning.core.Player;
import dev.hectora15.warning.core.TrapManager;
import dev.hectora15.warning.enums.TrapState;

import java.util.Objects;

public class GameRenderer {

    private static final String CREDITS = "@HectorA ";

    private final GameCore gameCore;
    private final GraphicsContext gc;
    private final UIRenderer uiRenderer;

    private final double width;
    private final double height;

    private ImagePattern spikeUpPattern;
    private ImagePattern spikeDownPattern;
    private ImagePattern spikeLeftPattern;
    private ImagePattern spikeRightPattern;

    private Image spikeWarningTop;
    private Image spikeWarningBottom;
    private Image spikeWarningLeft;
    private Image spikeWarningRight;


    public GameRenderer(GameCore gameCore, GraphicsContext gc, UIRenderer uiRenderer) {
        this.gameCore = gameCore;
        this.gc = gc;
        this.uiRenderer = uiRenderer;
        this.width = gameCore.getBoundWidth();
        this.height = gameCore.getBoundHeight();

        Image spikeUpImg    = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/traps/spikes/SpikeUp.png")));
        Image spikeDownImg  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/traps/spikes/SpikeDown.png")));
        Image spikeLeftImg  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/traps/spikes/SpikeLeft.png")));
        Image spikeRightImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/traps/spikes/SpikeRight.png")));

        // Cargar imágenes de Warnings
        this.spikeWarningTop    = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/traps/spikes/SpikeWarningTop.png")));
        this.spikeWarningBottom = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/traps/spikes/SpikeWarningBottom.png")));
        this.spikeWarningLeft   = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/traps/spikes/SpikeWarningLeft.png")));
        this.spikeWarningRight  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/traps/spikes/SpikeWarningRight.png")));

        // Convertir a mosaico SOLO los pinchos
        this.spikeUpPattern    = new ImagePattern(spikeUpImg, 0, 0, 30, 30, false);
        this.spikeDownPattern  = new ImagePattern(spikeDownImg, 0, 0, 30, 30, false);
        this.spikeLeftPattern  = new ImagePattern(spikeLeftImg, 0, 0, 30, 30, false);
        this.spikeRightPattern = new ImagePattern(spikeRightImg, 0, 0, 30, 30, false);
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

            if (trap.getState() == TrapState.DESTROYED) {
                return;
            }
            String posName = trap.getPosition().name();

            if (trap.getState() == TrapState.WARNING) {

                if (trap instanceof SpikeTrap spikeTrap) {
                    int currentFrame = spikeTrap.getFramesAlive();
                    int totalDuration = spikeTrap.getWarningDuration();
                    int phaseLength = totalDuration / 6;
                    int currentPhase = currentFrame / phaseLength;

                    if (currentPhase % 2 == 0) {
                        Image warningImg = null;

                        if (posName.startsWith("BOTTOM")) {
                            warningImg = spikeWarningBottom;
                        } else if (posName.startsWith("TOP")) {
                            warningImg = spikeWarningTop;

                        } else if (posName.startsWith("LEFT")) {
                            warningImg = spikeWarningLeft;
                        } else if (posName.startsWith("RIGHT")) {
                            warningImg = spikeWarningRight;
                        }

                        if (warningImg != null) {
                            double iconSize = 30;
                            double centerX = trap.getX() + (trap.getWidth() / 2) - (iconSize / 2);
                            double centerY = trap.getY() + (trap.getHeight() / 2) - (iconSize / 2);

                            if (posName.startsWith("BOTTOM")) {
                                centerY -= ((SpikeTrap) trap).getTHICKNESS();
                            } else if (posName.startsWith("TOP")) {
                                centerY += ((SpikeTrap) trap).getTHICKNESS();

                            } else if (posName.startsWith("LEFT")) {
                                centerX += ((SpikeTrap) trap).getTHICKNESS();
                            } else if (posName.startsWith("RIGHT")) {
                                centerX -= ((SpikeTrap) trap).getTHICKNESS();
                            }

                            gc.drawImage(warningImg, centerX, centerY, iconSize, iconSize);
                        }
                    }
                }
            } else if (trap.getState() == TrapState.ACTIVE) {


                if (posName.startsWith("BOTTOM")) {
                    gc.setFill(spikeUpPattern);
                } else if (posName.startsWith("TOP")) {
                    gc.setFill(spikeDownPattern);
                } else if (posName.startsWith("LEFT")) {
                    gc.setFill(spikeRightPattern);
                } else if (posName.startsWith("RIGHT")) {
                    gc.setFill(spikeLeftPattern);
                }
                gc.fillRect(trap.getX(), trap.getY(), trap.getWidth(), trap.getHeight());
            }


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