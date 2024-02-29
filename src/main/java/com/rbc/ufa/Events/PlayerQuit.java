package com.rbc.ufa.Events;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.UFA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerQuit implements Listener {
    private final ModeManager modeManager;

    public PlayerQuit(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @EventHandler
    public void pj(PlayerQuitEvent pqe) {
        ArrayList<ModeMap> maps = modeManager.getMapManager().getMaps(modeManager.getMode(UFA.class));
        UFAMap map = UFAMap.getMapByPlayer(maps, pqe.getPlayer());
        if (map == null) {
            return;
        }
        if (map.getUfaController().getStage() != UFAController.GAMING) {
            return;
        }
        UFAPlayer player = map.getUfaController().getPlayerInGame(pqe.getPlayer());
        if (player == null) {
            return;
        }
        quit(map, pqe, player);
    }

    private void quit(UFAMap map, PlayerQuitEvent pqe, UFAPlayer player) {
        if (!map.getUfaController().quitsContainsPlayer(pqe.getPlayer().getName())) {
            player.quit();
            map.getUfaController().addQuitPlayer(pqe.getPlayer().getName());
        } else {
            map.getUfaController().removeQuitPlayer(pqe.getPlayer().getName());
        }
    }
}
