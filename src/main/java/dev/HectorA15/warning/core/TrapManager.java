package dev.HectorA15.warning.core;

import dev.HectorA15.warning.enums.PlayerState;
import dev.HectorA15.warning.enums.PosTrap;
import dev.HectorA15.warning.enums.TrapState;
import dev.HectorA15.warning.traps.SpikeTrap;
import dev.HectorA15.warning.traps.Trap;

import java.util.ArrayList;
import java.util.Iterator;

public class TrapManager {

    private ArrayList<Trap> activeTraps = new ArrayList<>();

    public boolean update(Player player) { // Cambia void por boolean
        boolean playerDiedThisFrame = false;
        Iterator<Trap> iterator = activeTraps.iterator();
        while (iterator.hasNext()) {
            Trap trap = iterator.next();
            if (trap.getState() == TrapState.DESTROYED) {
                iterator.remove();
            } else {
                trap.updateLogic();
                if (trap.checkPlayerCollision(player)) {
                    player.setPlayerState(PlayerState.DEAD);
                    playerDiedThisFrame = true;
                }
            }
        }
        return playerDiedThisFrame;
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
