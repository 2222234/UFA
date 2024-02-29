package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Events {
    public static final int BOMB = 0;
    public static final int GRAVITY = 1;
    public static final int RAIN = 2;
    public static final int SPEED = 3;
    public static final int LUCK = 4;
    public static final int KILL = 5;
    public static final int SAND = 6;
    public static final int BREAK = 7;
    public static final int HIGH = 8;
    public static final int LIFE = 9;
    public static final int FIRE = 10;
    public static final int ONLY = 11;
    public static final int SEE = 12;
    private final UFAMap map;
    private final HashMap<Integer, RandomEvent> eventsMap;
    private final HashSet<Integer> done;
    private int event = -1;

    public Events(UFAMap map) {
        this.map = map;
        eventsMap = new HashMap<>();
        eventsMap.put(BOMB, new Bomb(map));
        eventsMap.put(GRAVITY, new Gravity(map));
        eventsMap.put(RAIN, new Rain(map));
        eventsMap.put(SPEED, new Speed(map));
        eventsMap.put(LUCK, new Luck(map));
        eventsMap.put(KILL, new Kill(map));
        eventsMap.put(SAND, new Sand(map));
        eventsMap.put(BREAK, new Break(map));
        eventsMap.put(HIGH, new High(map));
        eventsMap.put(LIFE, new Life(map));
        eventsMap.put(FIRE, new Fire(map));
        eventsMap.put(ONLY, new Only(map));
        eventsMap.put(SEE, new See(map));
        done = new HashSet<>();
    }

    public int getEvent() {
        return event;
    }

    public void startEvent() {
        if (!map.getSets().isRandomEvents()) {
            return;
        }
        stopEvent();
        event = new Random().nextInt(eventsMap.size());
        boolean d = done.contains(event);
        while (d && done.size() != eventsMap.size()) {
            event = new Random().nextInt(eventsMap.size());
            d = done.contains(event);
        }
        if (done.size() == eventsMap.size()) {
            done.clear();
        }
        done.add(event);
        getRandomEvent().eventStart();
    }

    public boolean startEvent(String name) {
        for (int event : eventsMap.keySet()) {
            if (eventsMap.get(event).name.equals(name)) {
                stopEvent();
                this.event = event;
                eventsMap.get(event).eventStart();
                return true;
            }
        }
        return false;
    }

    public void timePerSecond() {
        if (!map.getSets().isRandomEvents()) {
            return;
        }
        RandomEvent randomEvent = getRandomEvent();
        if (randomEvent != null) {
            randomEvent.timePerSecond();
            if (randomEvent.getAllTime() == 0) {
                stopEvent();
            }
        }
    }

    public void stopEvent() {
        if (!map.getSets().isRandomEvents()) {
            return;
        }
        RandomEvent randomEvent = getRandomEvent();
        if (randomEvent != null) {
            getRandomEvent().eventStop();
        }
        event = -1;
    }

    public RandomEvent getRandomEvent() {
        if (event < 0 || event > eventsMap.size() - 1) {
            return null;
        }
        return eventsMap.get(event);
    }

    public ArrayList<String> getEventNames() {
        ArrayList<String> eventNames = new ArrayList<>();
        for (RandomEvent randomEvent : eventsMap.values()) {
            eventNames.add(randomEvent.name);
        }
        return eventNames;
    }

    public void init() {
        event = -1;
        done.clear();
    }

    public boolean event(int event, Object... objects) {
        if (!map.getSets().isRandomEvents()) {
            return false;
        }
        RandomEvent randomEvent = getRandomEvent();
        if (randomEvent == null || this.event != event) {
            return false;
        }
        randomEvent.event(objects);
        return randomEvent.isReplaceFunction();
    }

    public void initPlayer(UFAPlayer player) {
        if (!map.getSets().isRandomEvents()) {
            return;
        }
        RandomEvent randomEvent = getRandomEvent();
        if (randomEvent == null) {
            return;
        }
        randomEvent.initPlayer(player);
    }
}