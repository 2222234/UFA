package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;

public class RandomEvent {

    protected UFAMap map;
    protected String name;
    protected int allTime;
    protected boolean replaceFunction = false;

    public RandomEvent(UFAMap map) {
        this.map = map;
    }

    public void eventStart() {
        allTime = map.getConfig().getEventTime();
        map.getUfaController().sendMessage(map.getMessage().getGame("events.start", map)
                .replace("{event}", map.getMessage().getGame("events." + name + ".name", map)));
        map.getUfaController().sendTitle(map.getMessage().getGame("events." + name + ".name", map), null, 20, 160, 20);
        map.getUfaController().getGamePlayers().forEach(this::initPlayer);
    }

    public void eventStop() {
        map.getUfaController().sendMessage(map.getMessage().getGame("events.stop", map)
                .replace("{event}", map.getMessage().getGame("events." + name + ".name", map)));
    }

    public void event(Object... objects) {
    }

    public void timePerSecond(Object... objects) {
        allTime--;
        if (map.getConfig().getEventTime() - allTime <= 10) {
            map.getUfaController().playSound(Sound.AMBIENT_CAVE);
        }
        if (allTime <= 5) {
            map.getUfaController().playSound(Sound.BLOCK_NOTE_BLOCK_BELL);
        }
    }

    public void initPlayer(UFAPlayer ufaPlayer) {
    }

    public boolean isReplaceFunction() {
        return replaceFunction;
    }

    public int getAllTime() {
        return allTime;
    }

    public String getName() {
        return name;
    }
}