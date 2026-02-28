package dev.hectora15.warning.core;

import dev.hectora15.warning.enums.PlayerState;

import java.util.Random;

public class GameCore {


    private int framesSurvived = 0;
    double boundWidth = 630;
    double boundHeight = 720;
    GameBounds gameBounds = new GameBounds(0, 0, boundWidth, boundHeight);
    Player player = new Player(boundWidth / 2, boundHeight / 2);
    TrapManager trapManager = new TrapManager();
    EventManager eventManager = new EventManager();
    Random random = new Random();
    FileManager fileManager = new FileManager();


    public void update() {
        player.updatePhysics();
        player.checkCollision(gameBounds);

        eventManager.update(trapManager, gameBounds, player, random);
        trapManager.update(player);
        if (player.getPlayerState() != PlayerState.DEAD) {
            framesSurvived++;
        }else{
            fileManager.saveHighScore(getScore());
        }
    }

    public double getBoundWidth() {
        return boundWidth;
    }

    public double getBoundHeight() {
        return boundHeight;
    }

    public GameBounds getGameBounds() {
        return gameBounds;
    }

    public Player getPlayer() {
        return player;
    }

    public TrapManager getTrapManager() {
        return trapManager;
    }

    public int getScore() {
        return framesSurvived / 60;
    }

    public void restart() {
        this.framesSurvived = 0;
        this.player = new Player(boundWidth / 2, boundHeight / 2);
        this.trapManager = new TrapManager();
        this.eventManager = new EventManager();
    }
}
