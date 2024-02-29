package com.rbc.ufa.Game.Stages;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class Prepare extends StageRunnable {

    public Prepare(UFAMap map) {
        super(map);
    }

    @Override
    public void start() {
        super.start();
        allTime = time = map.getConfig().getPrepareTime();
        bossBar = Bukkit.createBossBar(
                map.getMessage().getGame("message.prepare_bossbar", map)
                        .replace("{time}", String.valueOf(time)),
                BarColor.GREEN, BarStyle.SOLID);
        for (Player player : Bukkit.getOnlinePlayers()) {
            UFAPlayer ufaPlayer = map.getUfaController().getPlayerInOther(player);
            if (Tool.check(map.getGameLoc1(), map.getGameLoc2(), player.getLocation())) {
                if (ufaPlayer == null) {
                    ufaPlayer = new UFAPlayer(player);
                    map.getUfaController().addOtherPlayers(ufaPlayer);
                }
            }
        }
    }

    @Override
    public void run() {
        time--;
        if (time <= map.getConfig().getCountDown()) {
            countDown();
        }
        super.run();
    }

    @Override
    protected void setBossBar() {
        bossBar.setProgress(Math.min(1.0f, (allTime * 1.0f - time) / allTime));
        bossBar.setTitle(map.getMessage().getGame("message.prepare_bossbar", map)
                .replace("{time}", String.valueOf(time)));
    }

    private void countDown() {
        map.getUfaController().sendTitle(
                map.getMessage().getGame("message.prepare_countdown_title", map),
                map.getMessage().getGame("message.prepare_countdown_subtitle", map)
                        .replace("{time}", String.valueOf(time)),
                10, 0, 10);
        map.getUfaController().playSound(Sound.BLOCK_NOTE_BLOCK_BELL);
    }
}
