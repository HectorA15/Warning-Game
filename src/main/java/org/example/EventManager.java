package org.example;

import org.example.core.GameBounds;
import org.example.core.Player;
import org.example.core.TrapManager;
import org.example.enums.PosPlayer;
import org.example.enums.PosTrap;

public class EventManager {
    private double difficultyTimer = 0;
    private int framesSinceLastSpawn = 0;
    private int spawnCooldown = 120;

    public void update(TrapManager tm, GameBounds bounds, Player player) {
        difficultyTimer += (1.0 / 60.0);

        if (difficultyTimer >= 10.0) {
            if (spawnCooldown > 30) spawnCooldown -= 15;
            difficultyTimer = 0;
        }

        if (player.getPosPlayer() != PosPlayer.AIR) {
            framesSinceLastSpawn++;
        }

        if (framesSinceLastSpawn >= spawnCooldown) {
            triggerEvent(tm, bounds);
            framesSinceLastSpawn = 0;
        }
    }

    private void triggerEvent(TrapManager tm, GameBounds bounds) {
        double chance = Math.random();

        if (spawnCooldown <= 60 && chance < 0.10) {
            spawnWall(tm, bounds);
            spawnWall(tm, bounds);
        } else if (chance < 0.20) {
            spawnWall(tm, bounds);
        } else {
            tm.spawnRandomTrap(bounds);
        }
    }

    private void spawnWall(TrapManager tm, GameBounds bounds) {
        int wallSide = (int) (Math.random() * 4);

        for (PosTrap pos : PosTrap.values()) {
            if (wallSide == 0 && pos.name().startsWith("TOP")) tm.forceSpawn(pos, bounds);
            if (wallSide == 1 && pos.name().startsWith("BOTTOM")) tm.forceSpawn(pos, bounds);
            if (wallSide == 2 && pos.name().startsWith("LEFT")) tm.forceSpawn(pos, bounds);
            if (wallSide == 3 && pos.name().startsWith("RIGHT")) tm.forceSpawn(pos, bounds);
        }
    }
}