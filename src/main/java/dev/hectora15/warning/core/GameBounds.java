package dev.hectora15.warning.core;

public class GameBounds {

    private final double width;
    private final double height;
    private final double initX;
    private final double initY;

    public GameBounds(double initX, double initY, double width, double height) {
        this.initX = initX;
        this.initY = initY;
        this.width = width;
        this.height = height;
    }

    public double getLeftEdge() {
        return initX;
    }
    public double getRightEdge() {
        return initX + width;
    }
    public double getTopEdge() {
        return initY;
    }
    public double getBottomEdge() {
        return initY + height;
    }


    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

}
