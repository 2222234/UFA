package com.rbc.ufa.Game;

import com.rbc.ufa.Tools.ReadConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class Config {
    private final ReadConfig readConfig;
    private int minPlayer;
    private boolean balPlayer;
    private int countDown;
    private int prepareTime;
    private int gameTime;
    private int freeTime;
    private int eventTime;
    private int settlementTime;
    private int rewardMultiple;
    private ArrayList<Double> rewardMultipleList;
    private float volume;
    private float pitch;

    public Config(ReadConfig readConfig) {
        this.readConfig = readConfig;
    }

    public void reload() {
        FileConfiguration config = readConfig.readConfig();
        minPlayer = config.getInt("minplayer");
        balPlayer = config.getBoolean("balplayer");
        countDown = config.getInt("countdown");
        prepareTime = config.getInt("preparetime");
        gameTime = config.getInt("gametime");
        freeTime = config.getInt("freetime");
        eventTime = config.getInt("eventtime");
        settlementTime = config.getInt("settlementtime");
        rewardMultiple = config.getInt("rewardmultiple");
        rewardMultipleList = (ArrayList<Double>) config.getList("rewardmultiplelist");
        volume = (float) config.getDouble("volume");
        pitch = (float) config.getDouble("pitch");
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public boolean getBalPlayer() {
        return balPlayer;
    }

    public int getCountDown() {
        return countDown;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public int getEventTime() {
        return eventTime;
    }

    public int getSettlementTime() {
        return settlementTime;
    }

    public int getRewardMultiple() {
        return rewardMultiple;
    }

    public ArrayList<Double> getRewardMultipleList() {
        return rewardMultipleList;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
