package com.rbc.ufa.Map.Set;

import com.rbc.ufa.Map.UFAMap;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class Sets {
    private final UFAMap map;
    private int killScore;
    private boolean accidentalInjury;
    private int killMatePunish;
    private double maxBlood;
    private boolean randomEvents;
    private boolean randomTeam;
    private boolean randomSpawn;

    public Sets(UFAMap map) {
        this.map = map;
    }

    public int getKillScore() {
        return killScore;
    }

    public void setKillScore(int killScore) {
        this.killScore = killScore;
    }


    public boolean isAccidentalInjury() {
        return accidentalInjury;
    }

    public void setAccidentalInjury(boolean accidentalInjury) {
        this.accidentalInjury = accidentalInjury;
    }

    public int getKillMatePunish() {
        return killMatePunish;
    }

    public void setKillMatePunish(int killMatePunish) {
        this.killMatePunish = killMatePunish;
    }

    public double getMaxBlood() {
        return maxBlood;
    }

    public void setMaxBlood(double maxBlood) {
        this.maxBlood = maxBlood;
    }

    public boolean isRandomEvents() {
        return randomEvents;
    }

    public void setRandomEvents(boolean randomEvents) {
        this.randomEvents = randomEvents;
    }

    public boolean isRandomTeam() {
        return randomTeam;
    }

    public void setRandomTeam(boolean randomTeam) {
        this.randomTeam = randomTeam;
    }

    public boolean isRandomSpawn() {
        return randomSpawn;
    }

    public void setRandomSpawn(boolean randomSpawn) {
        this.randomSpawn = randomSpawn;
    }

    public ArrayList<String> check() {
        ArrayList<String> errors = new ArrayList<>();
        if (maxBlood <= 0) {
            errors.add(map.getMessage().getMap("error.maxblood"));
        }
        return errors;
    }

    public void save(FileConfiguration save) {
        String path = "set.";
        save.set(path + "killscore", killScore);
        save.set(path + "accidentalinjury", accidentalInjury);
        save.set(path + "killmatepunish", killMatePunish);
        save.set(path + "maxblood", maxBlood);
        save.set(path + "randomevents", randomEvents);
        save.set(path + "randomteam", randomTeam);
        save.set(path + "randomspawn", randomSpawn);
    }

    public void load(FileConfiguration save) {
        String path = "set.";
        killScore = save.getInt(path + "killscore");
        accidentalInjury = save.getBoolean(path + "accidentalinjury");
        killMatePunish = save.getInt(path + "killmatepunish");
        maxBlood = save.getDouble(path + "maxblood");
        randomEvents = save.getBoolean(path + "randomevents");
        randomTeam = save.getBoolean(path + "randomteam");
        randomSpawn = save.getBoolean(path + "randomspawn");
    }

    public String info() {
        return map.getMessage().getMap("info.set")
                .replace("{killscore}", String.valueOf(killScore))
                .replace("{accidentalinjury}", String.valueOf(accidentalInjury))
                .replace("{killmatepunish}", String.valueOf(killMatePunish))
                .replace("{maxblood}", String.valueOf(maxBlood))
                .replace("{randomevents}", String.valueOf(randomEvents))
                .replace("{randomteam}", String.valueOf(randomTeam))
                .replace("{randomspawn}", String.valueOf(randomSpawn));
    }

}
