package com.rbc.ufa.Game.Stages;

import com.rbc.gunfight.GunFight;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StageRunnable extends BukkitRunnable {
    protected final UFAMap map;
    protected int allTime;
    protected int time;
    protected BossBar bossBar;

    public StageRunnable(UFAMap map) {
        this.map = map;
    }

    public void start() {
        refreshBossBar();
        runTaskTimer(GunFight.getPlugin(GunFight.class), 20L, 20L);
    }

    @Override
    public void run() {
        refreshBossBar();
    }

    protected void refreshBossBar() {
        if (bossBar == null) {
            return;
        }
        setBossBar();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UFAPlayer oPlayer = map.getUfaController().getPlayerInOther(player);
            UFAPlayer gPlayer = map.getUfaController().getPlayerInGame(player);
            if (oPlayer != null || gPlayer != null) {
                if (!bossBar.getPlayers().contains(player)) {
                    bossBar.addPlayer(player);
                }
            } else {
                if (bossBar.getPlayers().contains(player)) {
                    bossBar.removePlayer(player);
                }
            }
        }
    }

    protected void setBossBar() {
    }

    @Override
    public void cancel() {
        super.cancel();
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    public int getTime() {
        return time;
    }
}
