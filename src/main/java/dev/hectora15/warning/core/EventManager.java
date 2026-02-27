package dev.hectora15.warning.core;

import dev.hectora15.warning.enums.PosPlayer;
import dev.hectora15.warning.enums.PosTrap;

import java.util.Random;

public class EventManager {
    private double difficultyTimer = 0;
    private int framesSinceLastSpawn = 0;
    private int spawnCooldown = 120;

    Random random = new Random();

    public void update(TrapManager tm, GameBounds bounds, Player player, Random random) {
        difficultyTimer += (1.0 / 60.0);

        if (difficultyTimer >= 10.0) {
            if (spawnCooldown > 30) spawnCooldown -= 15;
            difficultyTimer = 0;
        }

        if (player.getPosPlayer() != PosPlayer.AIR) {
            framesSinceLastSpawn++;
        }

        if (framesSinceLastSpawn >= spawnCooldown) {
            triggerEvent(tm, bounds, random);
            framesSinceLastSpawn = 0;
        }
    }

    private void triggerEvent(TrapManager tm, GameBounds bounds, Random random) {
        double chance = Math.random();

        if (spawnCooldown <= 60 && chance < 0.10) {
            spawnWall(tm, bounds, random);
            spawnWall(tm, bounds, random);
        } else if (chance < 0.20) {
            spawnWall(tm, bounds, random);
        } else {
            tm.spawnRandomTrap(bounds);
        }
    }

    private void spawnWall(TrapManager tm, GameBounds bounds, Random random) {
        int wallSide = random.nextInt(4);

        for (PosTrap pos : PosTrap.values()) {
            if (wallSide == 0 && pos.name().startsWith("TOP")) tm.forceSpawn(pos, bounds);
            if (wallSide == 1 && pos.name().startsWith("BOTTOM")) tm.forceSpawn(pos, bounds);
            if (wallSide == 2 && pos.name().startsWith("LEFT")) tm.forceSpawn(pos, bounds);
            if (wallSide == 3 && pos.name().startsWith("RIGHT")) tm.forceSpawn(pos, bounds);
        }
    }
}