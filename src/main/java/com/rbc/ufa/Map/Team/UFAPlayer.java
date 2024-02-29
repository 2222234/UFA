package com.rbc.ufa.Map.Team;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class UFAPlayer {
    private Player player;
    private Team team;
    private int score;
    private GameMode befGM;
    private GameMode quitGM;
    private ItemStack[] befBag;
    private ItemStack[] quitBag;

    private double befMaxBlood;

    private double quitMaxBlood;
    private int kill;
    private int die;


    public UFAPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void addScore(int score) {
        this.score = this.score + score;
        team.addScore(score);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setBag(ItemStack[] IS) {
        befBag = player.getInventory().getContents().clone();
        player.getInventory().setContents(IS);
    }

    public void setBefBag() {
        if (befBag != null) {
            player.getInventory().setContents(befBag);
        }
    }

    public void setGM(GameMode GM) {
        befGM = player.getGameMode();
        player.setGameMode(GM);
    }

    public void setBefGM() {
        if (befGM != null) {
            player.setGameMode(befGM);
        }
    }

    public void setMaxBlood(double MB) {
        befMaxBlood = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(MB);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    public void setBefMaxBlood() {
        if (befMaxBlood != 0) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(befMaxBlood);
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        }
    }

    public void quit() {
        quitBag = player.getInventory().getContents().clone();
        quitGM = player.getGameMode();
        quitMaxBlood = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        setBefBag();
        setBefGM();
        setBefMaxBlood();
    }

    public void join(Player player) {
        this.player = player;
        if (quitBag != null) {
            setBag(quitBag);
        }
        if (quitGM != null) {
            setGM(quitGM);
        }
        if (quitMaxBlood != 0) {
            setMaxBlood(quitMaxBlood);
        }
    }

    public void addKill() {
        kill++;
    }

    public void addDie() {
        die++;
    }

    public int getKill() {
        return kill;
    }

    public int getDie() {
        return die;
    }

    public String getKD() {
        int d = die == 0 ? 1 : die;
        double kd = (double) kill / d;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(kd);
    }

    public void resetPlayer() {
        setBefGM();
        setBefBag();
        setBefMaxBlood();
        kill = 0;
        die = 0;
    }
}
