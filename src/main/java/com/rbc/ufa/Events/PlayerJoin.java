package com.rbc.ufa.Events;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.UFA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerJoin implements Listener {
    private final ModeManager modeManager;

    public PlayerJoin(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @EventHandler
    public void pj(PlayerJoinEvent pje) {
        ArrayList<ModeMap> maps = modeManager.getMapManager().getMaps(modeManager.getMode(UFA.class));
        UFAMap map = UFAMap.getMapByPlayer(maps, pje.getPlayer());
        if (map == null) {
            return;
        }
        if (map.getUfaController().getStage() != UFAController.GAMING) {
            return;
        }
        join(map, pje);
    }

    private void join(UFAMap map, PlayerJoinEvent pje) {
        if (map.getUfaController().quitsContainsPlayer(pje.getPlayer().getName())) {
            pje.getPlayer().sendMessage(
                    map.getMessage().getGame("message.game_qj", map));
        }
    }
}
