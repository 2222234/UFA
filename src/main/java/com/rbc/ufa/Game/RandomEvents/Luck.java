package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;

import java.util.Random;

public class Luck extends RandomEvent {
    public Luck(UFAMap map) {
        super(map);
        name = "luck";
    }

    @Override
    public void event(Object... objects) {
        if (objects[0] instanceof UFAPlayer && objects[1] instanceof Integer) {
            int score = new Random().nextInt((int) objects[1] * 2) - (int) objects[1];
            UFAPlayer ufaPlayer = (UFAPlayer) objects[0];
            ufaPlayer.addScore(score);
            ufaPlayer.getPlayer().playSound(ufaPlayer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, map.getConfig().getVolume(), map.getConfig().getPitch());
            map.getUfaController().sendMessage(map.getMessage().getGame("events." + name + ".event", map)
                    .replace("{score}", String.valueOf(score)));
        }
    }
}
