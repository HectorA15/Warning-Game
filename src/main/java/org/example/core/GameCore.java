package org.example.core;

public class GameCore {


    private int framesSurvived = 0;
    double boundWidth = 650;
    double boundHeight = 750;
    GameBounds gameBounds = new GameBounds(0, 0, boundWidth, boundHeight);
    Player player = new Player(boundWidth / 2, boundHeight / 2);
    TrapManager trapManager = new TrapManager();
    EventManager eventManager = new EventManager();

    public void update() {
        player.updatePhysics();
        player.checkCollision(gameBounds);

        eventManager.update(trapManager, gameBounds, player);
        trapManager.update(player);
        if (player.getPlayerState() != org.example.enums.PlayerState.DEAD) {
            framesSurvived++;
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
