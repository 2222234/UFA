package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class High extends RandomEvent {
    private int time = 1;

    public High(UFAMap map) {
        super(map);
        name = "high";
    }

    @Override
    public void event(Object... objects) {
        if (objects[0] instanceof PlayerDeathEvent && objects[1] instanceof UFAPlayer) {
            UFAPlayer ufaPlayer = (UFAPlayer) objects[1];
            String en = map.getMessage().getGame("events." + name + ".event", map);
            String thunder = en.substring(en.lastIndexOf("|") + 1);
            map.getUfaController().sendMessage(thunder
                    .replace("{vic}", ufaPlayer.getPlayer().getName()));
        }
    }

    @Override
    public void timePerSecond(Object... objects) {
        super.timePerSecond(objects);
        String en = map.getMessage().getGame("events." + name + ".event", map);
        String early = en.substring(0, en.lastIndexOf("|"));
        if (time >= 12 && time < 15) {
            for (UFAPlayer ufaPlayer : highest()) {
                ufaPlayer.getPlayer().playSound(ufaPlayer.getPlayer().getLocation(), Sound.ITEM_TRIDENT_RETURN, map.getConfig().getVolume(), map.getConfig().getPitch());
                map.getGameWorld().strikeLightningEffect(ufaPlayer.getPlayer().getLocation());
                map.getUfaController().sendMessage(early
                        .replace("{vic}", ufaPlayer.getPlayer().getName()));
            }
        }
        if (time != 15) {
            time++;
            return;
        }
        for (UFAPlayer ufaPlayer : highest()) {
            ufaPlayer.getPlayer().playSound(ufaPlayer.getPlayer().getLocation(), Sound.ITEM_TRIDENT_THUNDER, map.getConfig().getVolume(), map.getConfig().getPitch());
            map.getGameWorld().strikeLightningEffect(ufaPlayer.getPlayer().getLocation());
            ufaPlayer.getPlayer().damage(40.0d);
        }
        time = 1;
    }

    private ArrayList<UFAPlayer> highest() {
        ArrayList<UFAPlayer> highest = new ArrayList<>();
        double y = 0;
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            double py = ufaPlayer.getPlayer().getLocation().getY();
            if (py > y) {
                highest.clear();
                y = py;
                highest.add(ufaPlayer);
            } else if (py == y) {
                highest.add(ufaPlayer);
            }
        }
        return highest;
    }
}
