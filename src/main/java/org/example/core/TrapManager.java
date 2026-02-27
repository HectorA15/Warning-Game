package org.example.core;

import org.example.enums.PlayerState;
import org.example.enums.PosTrap;
import org.example.enums.TrapState;
import org.example.traps.SpikeTrap;
import org.example.traps.Trap;

import java.util.ArrayList;
import java.util.Iterator;

public class TrapManager {

    private ArrayList<Trap> activeTraps = new ArrayList<>();

    public void update(Player player) {
        Iterator<Trap> iterator = activeTraps.iterator();
        while (iterator.hasNext()) {
            Trap trap = iterator.next();
            if (trap.getState() == TrapState.DESTROYED) {
                iterator.remove();
            } else {
                trap.updateLogic();
                if (trap.checkPlayerCollision(player)) {
                    player.setPlayerState(PlayerState.DEAD);
                }
            }
        }
    }

    public void spawnRandomTrap(GameBounds bounds) {
        PosTrap posTrap;
        boolean occupied;

        do {
            posTrap = PosTrap.getRandomPos();
            occupied = false;

            for (Trap trap : activeTraps) {
                if (trap.getState() != TrapState.DESTROYED && trap.getPosition() == posTrap) {
                    occupied = true;
                    break;
                }
            }
        } while (occupied);

        activeTraps.add(new SpikeTrap(posTrap, bounds));
    }

    public void forceSpawn(PosTrap pos, GameBounds bounds) {
        boolean occupied = activeTraps.stream()
                .anyMatch(t -> t.getPosition() == pos && t.getState() != TrapState.DESTROYED);

        if (!occupied) {
            activeTraps.add(new SpikeTrap(pos, bounds));
        }
    }

    public ArrayList<Trap> getActiveTraps() {
        return activeTraps;
    }
}
