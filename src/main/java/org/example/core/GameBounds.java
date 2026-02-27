package org.example.core;

public class GameBounds {

    private double width;
    private double height;
    private double initX;
    private double initY;

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
