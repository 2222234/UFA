package com.rbc.ufa.Game.Stages;

import com.rbc.gunfight.GunFight;
import com.rbc.ufa.Game.RandomEvents.Events;
import com.rbc.ufa.Game.RandomEvents.RandomEvent;
import com.rbc.ufa.Map.Team.Team;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.TpTeam;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Gaming extends StageRunnable {
    private BukkitRunnable score;

    public Gaming(UFAMap map) {
        super(map);
    }

    @Override
    public void start() {
        super.start();
        map.getBlocks().start();
        map.getAreas().start();
        allTime = time = map.getConfig().getGameTime();
        bossBar = Bukkit.createBossBar(
                map.getMessage().getGame("message.game_bossbar", map)
                        .replace("{time}", String.valueOf(time)),
                BarColor.YELLOW, BarStyle.SOLID);
        score();
        firstRun();
    }

    public void firstRun() {
        if (map.getSets().isRandomTeam()) {
            TpTeam.randomTeam(map);
        } else {
            TpTeam.pickToTeam(map);
        }
        if (map.getConfig().getBalPlayer() && map.getTeams().getTeams().size() <= 2) {
            TpTeam.pickToBalTeam(map);
        }
        HashMap<Team, String> teamMates = getMates();
        if (map.getUfaController().getGamePlayers().size() < map.getConfig().getMinPlayer()) {
            playerNotEnough(map.getUfaController().getGamePlayers().size());
            return;
        }

        gpjt(teamMates);
        opjg(teamMates);
    }

    private HashMap<Team, String> getMates() {
        HashMap<Team, String> teamMates = new HashMap<>();
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            Team team = ufaPlayer.getTeam();
            teamMates.putIfAbsent(team, "");
            String mates = teamMates.get(team);
            mates = mates != null ? mates.concat(" " + ufaPlayer.getPlayer().getName()) : " " + ufaPlayer.getPlayer().getName();
            teamMates.replace(team, mates);
        }
        return teamMates;
    }

    private void playerNotEnough(int count) {
        map.getUfaController().sendTitle(map.getMessage().getGame("message.game_lessplayer_title", map),
                map.getMessage().getGame("message.game_lessplayer_subtitle", map)
                        .replace("{players}", String.valueOf(count))
                        .replace("{minplayer}", String.valueOf(map.getConfig().getMinPlayer())),
                10, 40, 10
        );
        map.getUfaController().stop();
    }

    private void gpjt(HashMap<Team, String> teamMates) {
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            TpTeam.randomSpawn(map, ufaPlayer);
            ufaPlayer.getPlayer().sendTitle(
                    map.getMessage().getGame("message.game_jointeam_title", map),
                    map.getMessage().getGame("message.game_jointeam_subtitle", map)
                            .replace("{team}", ufaPlayer.getTeam().getName()),
                    10, 20, 10);
            for (Team team : teamMates.keySet()) {
                if (team.equals(ufaPlayer.getTeam())) {
                    ufaPlayer.getPlayer().sendMessage(
                            map.getMessage().getGame("message.game_yourmates", map)
                                    .replace("{team}", team.getName())
                                    .replace("{mates}", teamMates.get(team)));
                } else {
                    ufaPlayer.getPlayer().sendMessage(
                            map.getMessage().getGame("message.game_mates", map)
                                    .replace("{team}", team.getName())
                                    .replace("{mates}", teamMates.get(team)));
                }
            }

            ufaPlayer.setGM(GameMode.SURVIVAL);
            ufaPlayer.setMaxBlood(map.getSets().getMaxBlood());
            Block init = ufaPlayer.getTeam().getInitbag().getBlock();
            if (init.getType().equals(Material.CHEST)) {
                Chest chest = (Chest) init.getState();
                ufaPlayer.setBag(chest.getInventory().getContents());
            } else {
                ufaPlayer.getPlayer().sendMessage(map.getMessage().getGame("error.notfoundinitbag", map));
            }
        }
    }

    private void opjg(HashMap<Team, String> teamMates) {
        for (UFAPlayer ufaPlayer : map.getUfaController().getOtherPlayers()) {
            ufaPlayer.getPlayer().sendTitle(
                    map.getMessage().getGame("message.game_unjointeam_title", map),
                    map.getMessage().getGame("message.game_unjointeam_subtitle", map),
                    10, 20, 10);
            for (Team team : teamMates.keySet()) {
                ufaPlayer.getPlayer().sendMessage(
                        map.getMessage().getGame("message.game_mates", map)
                                .replace("{team}", team.getName())
                                .replace("{mates}", teamMates.get(team)));
            }
            ufaPlayer.setGM(GameMode.SPECTATOR);
        }
    }

    public void score() {
        score = new BukkitRunnable() {
            @Override
            public void run() {
                for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
                    String str = map.getMessage().getGame("message.game_actionbar", map)
                            .replace("{teamscore}", String.valueOf(ufaPlayer.getTeam().getScore()))
                            .replace("{playerscore}", String.valueOf(ufaPlayer.getScore()))
                            .replace("{kill}", String.valueOf(ufaPlayer.getKill()))
                            .replace("{die}", String.valueOf(ufaPlayer.getDie()))
                            .replace("{kd}", ufaPlayer.getKD());
                    ufaPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(str));
                }
            }
        };
        score.runTaskTimer(GunFight.getPlugin(GunFight.class), 0L, 0L);
    }

    @Override
    public void run() {
        time--;
        if (time <= map.getConfig().getCountDown()) {
            countDown();
        }
        Events events = map.getUfaController().getEvents();
        events.timePerSecond();
        if (allTime - time >= map.getConfig().getFreeTime() && time >= map.getConfig().getFreeTime() + map.getConfig().getEventTime()) {
            if (events.getRandomEvent() == null) {
                events.startEvent();
            }
        }
        super.run();
        if (time == 0) {
            map.getBlocks().stop();
            map.getAreas().stop();
            map.getUfaController().getEvents().stopEvent();
            map.getUfaController().sendMessage(
                    map.getMessage().getGame("message.game_stop", map));
        }
    }

    private void countDown() {
        map.getUfaController().sendMessage(
                map.getMessage().getGame("message.game_countdown", map)
                        .replace("{time}", String.valueOf(time)));
        map.getUfaController().playSound(Sound.BLOCK_ANVIL_PLACE);
    }

    @Override
    protected void setBossBar() {
        bossBar.setProgress(Math.min(1.0f, (allTime * 1.0f - time) / allTime));
        RandomEvent randomEvent = map.getUfaController().getEvents().getRandomEvent();
        if (randomEvent == null) {
            bossBar.setTitle(
                    map.getMessage().getGame("message.game_bossbar", map)
                            .replace("{time}", String.valueOf(time)));
        } else {
            bossBar.setTitle(
                    map.getMessage().getGame("message.game_bossbar_event", map)
                            .replace("{time}", String.valueOf(time))
                            .replace("{event}", map.getMessage().getGame("events." + randomEvent.getName() + ".name", map))
                            .replace("{eventtime}", String.valueOf(Math.min(time, randomEvent.getAllTime()))));
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (score != null) {
            score.cancel();
        }
        map.getBlocks().stop();
        map.getAreas().stop();
    }
}
