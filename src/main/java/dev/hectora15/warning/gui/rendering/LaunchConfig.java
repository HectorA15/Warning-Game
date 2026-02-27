package dev.hectora15.warning.gui.rendering;

public class LaunchConfig {
    private final double maxDragDistance;
    private final double powerMultiplier;

    public LaunchConfig(double maxDragDistance, double powerMultiplier) {
        this.maxDragDistance = maxDragDistance;
        this.powerMultiplier = powerMultiplier;
    }

    public double getMaxDragDistance() { return maxDragDistance; }
    public double getPowerMultiplier() { return powerMultiplier; }
}