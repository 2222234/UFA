package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Random;

public class Break extends RandomEvent {
    private int time = 1;

    public Break(UFAMap map) {
        super(map);
        name = "break";
    }

    @Override
    public void timePerSecond(Object... objects) {
        super.timePerSecond(objects);
        if (time != 20) {
            time++;
            return;
        }
        ArrayList<UFAPlayer> ufaPlayers = map.getUfaController().getGamePlayers();
        if (ufaPlayers.size() <= 1) {
            return;
        }
        int randoma = new Random().nextInt(ufaPlayers.size());
        int randomb = new Random().nextInt(ufaPlayers.size());
        boolean ct = ufaPlayers.get(randoma).getTeam().equals(ufaPlayers.get(randomb).getTeam());
        while (ct) {
            randomb = new Random().nextInt(ufaPlayers.size());
            ct = ufaPlayers.get(randoma).getTeam().equals(ufaPlayers.get(randomb).getTeam());
        }
        UFAPlayer ufaPlayera = ufaPlayers.get(randoma);
        UFAPlayer ufaPlayerb = ufaPlayers.get(randomb);
        ufaPlayera.getPlayer().teleport(ufaPlayerb.getPlayer().getLocation());
        map.getUfaController().sendMessage(
                map.getMessage().getGame("events." + name + ".event", map)
                        .replace("{playera}", ufaPlayera.getPlayer().getName())
                        .replace("{playerb}", ufaPlayerb.getPlayer().getName())
        );
        ufaPlayera.getPlayer().playSound(ufaPlayera.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, map.getConfig().getVolume(), map.getConfig().getPitch());
        ufaPlayerb.getPlayer().playSound(ufaPlayerb.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, map.getConfig().getVolume(), map.getConfig().getPitch());
        time = 1;
    }
}
