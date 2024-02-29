package com.rbc.ufa.Game;

import com.rbc.gunfight.GunFight;
import com.rbc.gunfight.Mode.ModeController;
import com.rbc.ufa.Game.RandomEvents.Events;
import com.rbc.ufa.Game.Stages.Gaming;
import com.rbc.ufa.Game.Stages.Prepare;
import com.rbc.ufa.Game.Stages.Settlement;
import com.rbc.ufa.Game.Stages.StageRunnable;
import com.rbc.ufa.Map.Team.Team;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import com.rbc.ufa.Tools.TpTeam;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class UFAController extends ModeController {
    public static final int PREPARE = 1;
    public static final int GAMING = 2;
    public static final int SETTLEMENT = 3;
    private final UFAMap map;
    private final HashMap<Integer, StageRunnable> stageMap;
    private final ArrayList<UFAPlayer> gamePlayers;
    private final ArrayList<UFAPlayer> otherPlayers;
    private final ArrayList<String> votes;
    private final ArrayList<String> quits;
    private final Events events;
    private BukkitRunnable control;
    private int time;
    private StageRunnable stageRunnable;

    public UFAController(UFAMap map) {
        this.map = map;
        events = new Events(map);
        stageMap = new HashMap<>();
        gamePlayers = new ArrayList<>();
        otherPlayers = new ArrayList<>();
        votes = new ArrayList<>();
        quits = new ArrayList<>();
    }

    public void init() {
        gamePlayers.clear();
        otherPlayers.clear();
        votes.clear();
        quits.clear();
        events.init();
        for (Team team : map.getTeams().getTeams()) {
            team.setScore(0);
        }
        stage = STOP;
        stageMap.clear();
        stageMap.put(STOP, null);
        stageMap.put(PREPARE, new Prepare(map));
        stageMap.put(GAMING, new Gaming(map));
        stageMap.put(SETTLEMENT, new Settlement(map));
    }

    public void initPlayer(UFAPlayer ufaPlayer) {
        ufaPlayer.setMaxBlood(map.getSets().getMaxBlood());
        Block init = ufaPlayer.getTeam().getInitbag().getBlock();
        Chest chest = (Chest) init.getState();
        ufaPlayer.getPlayer().getInventory().setContents(chest.getInventory().getContents());

        events.initPlayer(ufaPlayer);
    }

    @Override
    public boolean start() {
        if (stage != STOP) {
            return false;
        }
        init();
        stage = PREPARE;
        stageRunnable = stageMap.get(stage);
        stageRunnable.start();
        control = new BukkitRunnable() {
            @Override
            public void run() {
                time = stageRunnable.getTime();
                switch (stage) {
                    case PREPARE:
                        if (stageRunnable.getTime() == 0) {
                            stageRunnable.cancel();
                            stage = GAMING;
                            stageRunnable = stageMap.get(stage);
                            stageRunnable.start();
                        }
                        break;
                    case GAMING:
                        if (stageRunnable.getTime() == 0) {
                            stageRunnable.cancel();
                            stage = SETTLEMENT;
                            stageRunnable = stageMap.get(stage);
                            stageRunnable.start();
                        }
                        break;
                    case SETTLEMENT:
                        if (stageRunnable.getTime() == 0) {
                            stop();
                        }
                        break;
                }
            }
        };
        control.runTaskTimer(GunFight.getPlugin(GunFight.class), 0L, 0L);
        return true;
    }

    @Override
    public boolean stop() {
        if (stage == STOP) {
            return false;
        }
        sendMessage(map.getMessage().getGame("message.stop", map));
        map.getBlocks().stop();
        map.getAreas().stop();
        events.stopEvent();
        for (UFAPlayer ufaPlayer : getGamePlayers()) {
            if (!Tool.check(ufaPlayer.getTeam().getPrepare1(), ufaPlayer.getTeam().getPrepare2(), ufaPlayer.getPlayer().getLocation())) {
                TpTeam.randomPrepare(ufaPlayer);
            }
            ufaPlayer.resetPlayer();
        }
        for (UFAPlayer ufaPlayer : getOtherPlayers()) {
            ufaPlayer.resetPlayer();
        }
        init();
        stageRunnable.cancel();
        control.cancel();
        stage = STOP;
        return true;
    }

    public boolean reJoinGame(Player player) {
        if (stage != GAMING) {
            return false;
        }
        if (!quits.contains(player.getName())) {
            return false;
        }
        for (UFAPlayer ufaPlayer : gamePlayers) {
            if (ufaPlayer.getPlayer().getName().equals(player.getName())) {
                ufaPlayer.join(player);
                ufaPlayer.getPlayer().teleport(ufaPlayer.getTeam().getSpawnCenter());
                quits.remove(player.getName());
                sendMessage(map.getMessage().getGame("message.game_joinsuccess", map)
                        .replace("{player}", ufaPlayer.getPlayer().getName()));
                initPlayer(ufaPlayer);
                return true;
            }
        }
        return false;
    }

    public boolean jump(int num) {
        if (jumpTo(num)) {
            sendMessage(
                    map.getMessage().getGame("message.jump", map)
                            .replace("{num}", String.valueOf(num)));
            return true;
        }
        return false;
    }

    public int getTime() {
        return time;
    }

    public int getGameTime() {
        return map.getConfig().getGameTime();
    }

    public int getStage() {
        return stage;
    }


    public boolean jumpTo(int stageNum) {
        if (stageNum <= 0 || stageNum >= stageMap.size() || stageNum == stage) {
            return false;
        }
        stageRunnable.cancel();
        stage = stageNum;
        stageRunnable = stageMap.get(stage);
        stageRunnable.start();
        return true;
    }

    public ArrayList<UFAPlayer> getOtherPlayers() {
        return otherPlayers;
    }

    public ArrayList<UFAPlayer> getGamePlayers() {
        return gamePlayers;
    }

    public boolean addOtherPlayers(UFAPlayer ufaPlayer) {
        return otherPlayers.add(ufaPlayer);
    }

    public boolean removeOtherPlayers(UFAPlayer ufaPlayer) {
        return otherPlayers.remove(ufaPlayer);
    }

    public boolean removeVote(String name) {
        return votes.remove(name);
    }

    public boolean quitsContainsPlayer(String name) {
        return quits.contains(name);
    }

    public boolean addQuitPlayer(String name) {
        return quits.add(name);
    }

    public boolean removeQuitPlayer(String name) {
        return quits.remove(name);
    }

    public UFAPlayer getPlayerInGame(Player player) {
        for (UFAPlayer ufaPlayer : gamePlayers) {
            if (ufaPlayer.getPlayer().equals(player)) {
                return ufaPlayer;
            }
        }
        return null;
    }

    public UFAPlayer getPlayerInOther(Player player) {
        for (UFAPlayer ufaPlayer : otherPlayers) {
            if (ufaPlayer.getPlayer().equals(player)) {
                return ufaPlayer;
            }
        }
        return null;
    }

    public boolean movePlayerFromOtherToGame(ArrayList<UFAPlayer> ufaPlayer) {
        if (otherPlayers.removeAll(ufaPlayer)) {
            return gamePlayers.addAll(ufaPlayer);
        }
        return false;
    }

    public void setWeather(WeatherType weather) {
        for (UFAPlayer ufaPlayer : otherPlayers) {
            ufaPlayer.getPlayer().setPlayerWeather(weather);
        }
        if (stage == UFAController.GAMING || stage == UFAController.SETTLEMENT) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                ufaPlayer.getPlayer().setPlayerWeather(weather);
            }
        }
    }

    public void resetWeather() {
        for (UFAPlayer ufaPlayer : otherPlayers) {
            ufaPlayer.getPlayer().resetPlayerWeather();
        }
        if (stage == UFAController.GAMING || stage == UFAController.SETTLEMENT) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                ufaPlayer.getPlayer().resetPlayerWeather();
            }
        }
    }

    public void sendMessage(String message) {
        for (UFAPlayer ufaPlayer : otherPlayers) {
            ufaPlayer.getPlayer().sendMessage(message);
        }
        if (stage == UFAController.GAMING || stage == UFAController.SETTLEMENT) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                ufaPlayer.getPlayer().sendMessage(message);
            }
        }
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (UFAPlayer ufaPlayer : otherPlayers) {
            ufaPlayer.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
        if (stage == UFAController.GAMING || stage == UFAController.SETTLEMENT) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                ufaPlayer.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        }
    }

    public void playSound(Sound sound) {
        for (UFAPlayer ufaPlayer : otherPlayers) {
            ufaPlayer.getPlayer().playSound(ufaPlayer.getPlayer().getLocation(), sound, map.getConfig().getVolume(), map.getConfig().getPitch());
        }
        if (stage == UFAController.GAMING || stage == UFAController.SETTLEMENT) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                ufaPlayer.getPlayer().playSound(ufaPlayer.getPlayer().getLocation(), sound, map.getConfig().getVolume(), map.getConfig().getPitch());
            }
        }
    }

    public void playSound(Location location, Sound sound) {
        for (UFAPlayer ufaPlayer : otherPlayers) {
            ufaPlayer.getPlayer().playSound(location, sound, map.getConfig().getVolume(), map.getConfig().getPitch());
        }
        if (stage == UFAController.GAMING || stage == UFAController.SETTLEMENT) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                ufaPlayer.getPlayer().playSound(location, sound, map.getConfig().getVolume(), map.getConfig().getPitch());
            }
        }
    }

    public boolean voteToJumpPrepare(Player player) {
        if (stage == UFAController.PREPARE) {
            if (getPlayerInOther(player) != null) {
                if (!votes.contains(player.getName())) {
                    votes.add(player.getName());
                    sendMessage(
                            map.getMessage().getGame("message.prepare_vote_to_game", map)
                                    .replace("{player}", player.getName())
                                    .replace("{vote}", String.valueOf(votes.size()))
                                    .replace("{all}", String.valueOf(otherPlayers.size())));
                    checkVote();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkVote() {
        if (votes.size() == otherPlayers.size()) {
            sendMessage(map.getMessage().getGame("message.prepare_votesuccess", map));
            jumpTo(UFAController.GAMING);
            return true;
        }
        return false;
    }

    public ArrayList<String> playersInfo() {
        ArrayList<String> ps = new ArrayList<>();
        for (UFAPlayer ufaPlayer : otherPlayers) {
            ps.add(map.getMessage().getGame("info.map_other", map)
                    .replace("{player}", ufaPlayer.getPlayer().getName()));
        }
        if (stage == UFAController.GAMING) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                ps.add(map.getMessage().getGame("info.map_gamer", map)
                        .replace("{player}", ufaPlayer.getPlayer().getName())
                        .replace("{team}", ufaPlayer.getTeam().getName())
                        .replace("{score}", String.valueOf(ufaPlayer.getScore()))
                        .replace("{blood}", String.valueOf(ufaPlayer.getPlayer().getHealth()))
                        .replace("{kill}", String.valueOf(ufaPlayer.getKill()))
                        .replace("{die}", String.valueOf(ufaPlayer.getDie()))
                        .replace("{kd}", ufaPlayer.getKD()));
            }
        }
        return ps;
    }

    public String playerInfoName(String name) {
        for (UFAPlayer ufaPlayer : otherPlayers) {
            if (ufaPlayer.getPlayer().getName().equals(name)) {
                return map.getMessage().getGame("info.map_player", map)
                        .replace("{player}", ufaPlayer.getPlayer().getName());
            }
        }
        if (stage == UFAController.GAMING) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                if (ufaPlayer.getPlayer().getName().equals(name)) {
                    return map.getMessage().getGame("info.map_gamer", map)
                            .replace("{player}", ufaPlayer.getPlayer().getName())
                            .replace("{team}", ufaPlayer.getTeam().getName())
                            .replace("{score}", String.valueOf(ufaPlayer.getScore()))
                            .replace("{blood}", String.valueOf(ufaPlayer.getPlayer().getHealth()))
                            .replace("{kill}", String.valueOf(ufaPlayer.getKill()))
                            .replace("{die}", String.valueOf(ufaPlayer.getDie()))
                            .replace("{kd}", ufaPlayer.getKD());
                }
            }
        }
        return map.getMessage().getGame("erroe.notfoundplayer", map);
    }

    public ArrayList<String> getPlayerNames() {
        ArrayList<String> names = new ArrayList<>();
        for (UFAPlayer ufaPlayer : otherPlayers) {
            names.add(ufaPlayer.getPlayer().getName());
        }
        if (stage == UFAController.GAMING) {
            for (UFAPlayer ufaPlayer : gamePlayers) {
                names.add(ufaPlayer.getPlayer().getName());
            }
        }
        return names;
    }

    public Events getEvents() {
        return events;
    }
}
