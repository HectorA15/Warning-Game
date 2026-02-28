package dev.hectora15.warning.traps;

import dev.hectora15.warning.core.GameBounds;
import dev.hectora15.warning.core.Player;
import dev.hectora15.warning.enums.PosTrap;
import dev.hectora15.warning.enums.TrapState;

public abstract class Trap {
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected PosTrap position;
    protected TrapState currentState;
    protected int warningDuration;
    protected int activeDuration;

    protected Trap(PosTrap position) {
        this.position = position;
        this.currentState = TrapState.WARNING;
    }

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
    public int getWarningDuration(){return this.warningDuration;}
    public int getActiveDuration(){return this.activeDuration;}
    public void setWarningDuration(int duration){this.warningDuration = duration;}
    public void setActiveDuration(int duration){this.activeDuration = duration;}
}
