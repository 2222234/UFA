package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class Rain extends RandomEvent {
    private ArrayList<UFAPlayer> killed;

    public Rain(UFAMap map) {
        super(map);
        name = "rain";
    }


    @Override
    public void eventStart() {
        super.eventStart();
        killed = new ArrayList<>();
        map.getUfaController().setWeather(WeatherType.DOWNFALL);
    }

    @Override
    public void event(Object... objects) {
        if (objects[0] instanceof PlayerDeathEvent && objects[1] instanceof UFAPlayer) {
            UFAPlayer ufaPlayer = (UFAPlayer) objects[1];
            if (killed.contains(ufaPlayer)) {
                return;
            }
            map.getUfaController().sendMessage(map.getMessage().getGame("events." + name + ".event", map)
                    .replace("{vic}", ufaPlayer.getPlayer().getName()));
            killed.add(ufaPlayer);
        }
    }


    @Override
    public void timePerSecond(Object... objects) {
        super.timePerSecond(objects);
        map.getUfaController().playSound(Sound.WEATHER_RAIN);
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            if (!checkHeadHasBlock(ufaPlayer)) {
                ufaPlayer.getPlayer().damage(6.0d);
            }
            if (ufaPlayer.getPlayer().getHealth() > 0.0d) {
                killed.remove(ufaPlayer);
            }
        }
    }

    private boolean checkHeadHasBlock(UFAPlayer ufaPlayer) {
        Location location = ufaPlayer.getPlayer().getLocation().clone();
        for (int y = ufaPlayer.getPlayer().getLocation().getBlockY(); y <= map.getGameWorld().getMaxHeight(); y++) {
            location.setY(y);
            if (!location.getBlock().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void eventStop() {
        super.eventStop();
        map.getUfaController().resetWeather();
    }
}
