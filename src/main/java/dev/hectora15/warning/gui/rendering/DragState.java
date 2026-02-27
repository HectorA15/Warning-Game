package dev.hectora15.warning.gui.rendering;

public class DragState {
    private final boolean isDragging;
    private final double startX;
    private final double startY;
    private final double currentX;
    private final double currentY;

    public DragState(boolean isDragging, double startX, double startY,
                     double currentX, double currentY) {
        this.isDragging = isDragging;
        this.startX = startX;
        this.startY = startY;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    public boolean isDragging() { return isDragging; }
    public double getStartX() { return startX; }
    public double getStartY() { return startY; }
    public double getCurrentX() { return currentX; }
    public double getCurrentY() { return currentY; }
}