package com.rbc.ufa.Events;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.RandomEvents.Events;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.UFA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.ArrayList;

public class DropKill implements Listener {
    private final ModeManager modeManager;

    public DropKill(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @EventHandler
    public void dk(PlayerDropItemEvent pdie) {
        ArrayList<ModeMap> maps = modeManager.getMapManager().getMaps(modeManager.getMode(UFA.class));
        UFAMap map = UFAMap.getMapByPlayer(maps, pdie.getPlayer());
        if (map == null) {
            return;
        }
        if (map.getUfaController().getStage() != UFAController.GAMING) {
            return;
        }
        drop(map, pdie);
    }

    private void drop(UFAMap map, PlayerDropItemEvent pdie) {
        map.getUfaController().getEvents().event(Events.KILL, pdie);
    }
}
