package dev.HectorA15.warning.gui.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.InputStream;

public class UIRenderer {

    private final Image restartIcon;
    private double restartBtnX;
    private double restartBtnY;
    private final double restartBtnSize = 50;

    private final double width;
    private final double height;

    public UIRenderer(double width, double height) {
        this.width = width;
        this.height = height;
        this.restartIcon = loadRestartIcon();

        // Calcular posiciones del botón desde el inicio
        this.restartBtnX = width / 2 - restartBtnSize / 2;
        this.restartBtnY = height / 2 + 80;
    }

    private Image loadRestartIcon() {
        InputStream in = getClass().getResourceAsStream("/restart.png");
        if (in == null) {
            throw new IllegalStateException("/restart.png not found in classpath");
        }
        return new Image(in);
    }

    public void drawGameOver(GraphicsContext gc) {
        // Overlay oscuro
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(0, 0, width, height);

        // Texto "GAME OVER"
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", width / 2, height / 2 - 40);

        // Botón de restart
        restartBtnX = width / 2 - restartBtnSize / 2;
        restartBtnY = height / 2 + 80;
        gc.drawImage(restartIcon, restartBtnX, restartBtnY, restartBtnSize, restartBtnSize);

        // Texto "Restart"
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Restart", width / 2, restartBtnY + restartBtnSize + 30);
    }

    public boolean isRestartButtonClicked(double mouseX, double mouseY) {
        return mouseX >= restartBtnX && mouseX <= restartBtnX + restartBtnSize &&
                mouseY >= restartBtnY && mouseY <= restartBtnY + restartBtnSize;
    }

    public double getRestartBtnX() {
        return restartBtnX;
    }

    public double getRestartBtnY() {
        return restartBtnY;
    }
}