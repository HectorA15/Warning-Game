package dev.hectora15.warning.core;

import dev.hectora15.warning.enums.PlayerState;
import dev.hectora15.warning.enums.PosPlayer;

public class Player {

    private static final double GRAVITY = 0.5;
    private static final double HITBOX_WIDTH = 20;
    private static final double HITBOX_HEIGHT = 20;
    private double x;
    private double y;
    private double oldX;
    private double oldY;
    private double velocityX = 0;
    private double velocityY = 0;
    private PosPlayer posPlayer = PosPlayer.AIR;
    private PlayerState playerState = PlayerState.ALIVE;


    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void applyImpulse(double forceX, double forceY) {

        this.velocityX += forceX;
        this.velocityY += forceY;

        this.posPlayer = PosPlayer.AIR;

    }


    public void updatePhysics() {
        this.oldX = this.x;
        this.oldY = this.y;

        if(this.posPlayer != PosPlayer.TOP){
            this.velocityY += GRAVITY;
        }


        this.x += velocityX;
        this.y += velocityY;
    }

    public void checkCollision(GameBounds gameBounds) {
        this.posPlayer = PosPlayer.AIR;

        if (this.x <= gameBounds.getLeftEdge()) {
            this.velocityX = 0;
            this.velocityY = 0;
            this.x = gameBounds.getLeftEdge();
            this.posPlayer = PosPlayer.LEFT;

        }

        if (this.x + HITBOX_WIDTH >= gameBounds.getRightEdge()) {
            this.velocityX = 0;
            this.velocityY = 0;
            this.x = gameBounds.getRightEdge() - HITBOX_WIDTH;
            this.posPlayer = PosPlayer.RIGHT;
        }

        if (this.y <= gameBounds.getTopEdge()) {
            this.velocityX = 0;
            this.velocityY = 0;
            this.y = gameBounds.getTopEdge();
            this.posPlayer = PosPlayer.TOP;
        }

        if (this.y + HITBOX_HEIGHT >= gameBounds.getBottomEdge()) {
            this.velocityX = 0;
            this.velocityY = 0;
            this.y = gameBounds.getBottomEdge() - HITBOX_HEIGHT;
            this.posPlayer = PosPlayer.BOTTOM;
        }
    }


    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }


    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getOldX() {
        return oldX;
    }
    public double getOldY() {
        return oldY;
    }

    public PosPlayer getPosPlayer() {
        return posPlayer;
    }

    public void setPosPlayer(PosPlayer posPlayer) {
        this.posPlayer = posPlayer;
    }

    public double getWidth() {
        return HITBOX_WIDTH;
    }

    public double getHeight() {
        return HITBOX_HEIGHT;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public double getGravity(){
        return GRAVITY;
    }


}
