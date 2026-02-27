package dev.HectorA15.warning.traps;

import dev.HectorA15.warning.core.GameBounds;
import dev.HectorA15.warning.core.Player;
import dev.HectorA15.warning.enums.PosTrap;
import dev.HectorA15.warning.enums.TrapState;

public abstract class Trap {
    protected double x, y, width, height;
    protected PosTrap position;
    protected TrapState currentState;

    // El constructor ahora solo pide la posición
    public Trap(PosTrap position) {
        this.position = position;
        this.currentState = TrapState.WARNING;
    }

    // Obligamos a los hijos a crear este método
    protected abstract void calculateHitbox(GameBounds bounds);

    public abstract void updateLogic();
    public abstract boolean checkPlayerCollision(Player player);

    public TrapState getState(){
        return this.currentState;
    }

    public PosTrap getPosition() {
        return position;
    }

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public double getWidth(){
        return this.width;
    }
    public double getHeight(){
        return this.height;
    }
}
