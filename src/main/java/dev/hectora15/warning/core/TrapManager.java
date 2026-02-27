package dev.hectora15.warning.core;

import dev.hectora15.warning.enums.PlayerState;
import dev.hectora15.warning.enums.PosTrap;
import dev.hectora15.warning.enums.TrapState;
import dev.hectora15.warning.traps.SpikeTrap;
import dev.hectora15.warning.traps.Trap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrapManager {

    private final ArrayList<Trap> activeTraps = new ArrayList<>();

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

    public List<Trap> getActiveTraps() {
        return activeTraps;
    }
}
