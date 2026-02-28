package dev.hectora15.warning.traps;

import dev.hectora15.warning.core.GameBounds;
import dev.hectora15.warning.core.Player;
import dev.hectora15.warning.enums.PosTrap;
import dev.hectora15.warning.enums.TrapState;

public class SpikeTrap extends Trap {

    private int framesAlive = 0;
    private static int warningDuration = 100;
    private static int activeDuration = 240;

    public SpikeTrap(PosTrap position, GameBounds bounds) {
        super(position);
        calculateHitbox(bounds);

    }

    @Override
    protected void calculateHitbox(GameBounds bounds) {
        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;

        // Trap dimensions
        final double THICKNESS = 30.0;
        double thirdHeight = bounds.getHeight() / 3.0;
        double thirdWidth = bounds.getWidth() / 3.0;

        switch (this.position) {

            // ================== LEFT POSITIONS =====================
            case LEFT_TOP:
                x = bounds.getLeftEdge();
                y = bounds.getTopEdge();
                width = THICKNESS;
                height = thirdHeight;
                break;
            case LEFT_CENTER:
                x = bounds.getLeftEdge();
                y = bounds.getTopEdge() + thirdHeight;
                width = THICKNESS;
                height = thirdHeight;
                break;
            case LEFT_BOTTOM:
                x = bounds.getLeftEdge();
                y = bounds.getBottomEdge() - thirdHeight;
                width = THICKNESS;
                height = thirdHeight;
                break;

            // ================== RIGHT POSITIONS =====================
            case RIGHT_TOP:
                x = bounds.getRightEdge() - THICKNESS;
                y = bounds.getTopEdge();
                width = THICKNESS;
                height = thirdHeight;
                break;
            case RIGHT_CENTER:
                x = bounds.getRightEdge() - THICKNESS;
                y = bounds.getTopEdge() + thirdHeight;
                width = THICKNESS;
                height = thirdHeight;
                break;
            case RIGHT_BOTTOM:
                x = bounds.getRightEdge() - THICKNESS;
                y = bounds.getBottomEdge() - thirdHeight;
                width = THICKNESS;
                height = thirdHeight;
                break;

            // ================== TOP POSITIONS =====================
            case TOP_LEFT:
                x = bounds.getLeftEdge();
                y = bounds.getTopEdge();
                width = thirdWidth;
                height = THICKNESS;
                break;
            case TOP_CENTER:
                x = bounds.getLeftEdge() + thirdWidth;
                y = bounds.getTopEdge();
                width = thirdWidth;
                height = THICKNESS;
                break;
            case TOP_RIGHT:
                x = bounds.getRightEdge() - thirdWidth;
                y = bounds.getTopEdge();
                width = thirdWidth;
                height = THICKNESS;
                break;

            // ================== BOTTOM POSITIONS =====================
            case BOTTOM_LEFT:
                x = bounds.getLeftEdge();
                y = bounds.getBottomEdge() - THICKNESS;
                width = thirdWidth;
                height = THICKNESS;
                break;
            case BOTTOM_CENTER:
                x = bounds.getLeftEdge() + thirdWidth;
                y = bounds.getBottomEdge() - THICKNESS;
                width = thirdWidth;
                height = THICKNESS;
                break;
            case BOTTOM_RIGHT:
                x = bounds.getRightEdge() - thirdWidth;
                y = bounds.getBottomEdge() - THICKNESS;
                width = thirdWidth;
                height = THICKNESS;
                break;
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }


    @Override
    public void updateLogic() {
        this.framesAlive++;
        if(this.currentState == TrapState.WARNING){

            if (this.framesAlive >= warningDuration) {
                this.currentState = TrapState.ACTIVE;
                this.framesAlive = 0;
            }

        }else if(this.currentState == TrapState.ACTIVE && this.framesAlive >= activeDuration){

                this.currentState = TrapState.DESTROYED;

        }
    }

    @Override
    public boolean checkPlayerCollision(Player player) {

        if(this.currentState != TrapState.ACTIVE) return false;

        return player.getX() < this.x + this.width &&
                player.getX() + player.getWidth() > this.x &&
                player.getY() < this.y + this.height &&
                player.getY() + player.getHeight() > this.y;
    }

}


