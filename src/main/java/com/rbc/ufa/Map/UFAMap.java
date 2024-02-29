package com.rbc.ufa.Map;

import com.rbc.gunfight.Mode.ModeMain;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.Config;
import com.rbc.ufa.Game.RandomEvents.RandomEvent;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.Area.Areas;
import com.rbc.ufa.Map.Block.Blocks;
import com.rbc.ufa.Map.Set.Sets;
import com.rbc.ufa.Map.Team.Teams;
import com.rbc.ufa.Tools.Message;
import com.rbc.ufa.Tools.Tool;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UFAMap extends ModeMap {
    private final Blocks blocks;
    private final Areas areas;
    private final Teams teams;
    private final Sets sets;
    private final Config config;
    private final Message message;

    public UFAMap(ModeMain modeMain, File mapFile, String name, Config config, Message message) {
        super(modeMain, mapFile, name);
        this.config = config;
        this.message = message;
        blocks = new Blocks(this);
        areas = new Areas(this);
        teams = new Teams(this);
        sets = new Sets(this);
        modeController = new UFAController(this);
    }

    public static UFAMap getMapByPlayer(ArrayList<ModeMap> maps, Player player) {
        for (ModeMap map : maps) {
            if (!(map instanceof UFAMap)) {
                continue;
            }
            UFAMap ufaMap = (UFAMap) map;
            if (ufaMap.getUfaController().getPlayerInGame(player) != null) {
                return ufaMap;
            }
            if (ufaMap.getUfaController().getPlayerInOther(player) != null) {
                return ufaMap;
            }
            if (ufaMap.getUfaController().quitsContainsPlayer(player.getName())) {
                return ufaMap;
            }
            if (Tool.check(ufaMap.getGameLoc1(), ufaMap.getGameLoc2(), player.getLocation()) && ufaMap.getUfaController().getStage() != UFAController.STOP) {
                return ufaMap;
            }
        }
        return null;
    }

    @Override
    public boolean startCheck() {
        return check().isEmpty();
    }

    @Override
    public boolean saveMap() {
        FileConfiguration save = YamlConfiguration.loadConfiguration(getMapFile());
        if (check().isEmpty()) {
            save.set("world", gameWorld.getName());
            save.set("pos1", gameLoc1.toVector());
            save.set("pos2", gameLoc2.toVector());
            blocks.save(save);
            areas.save(save);
            teams.save(save);
            sets.save(save);
            try {
                save.save(getMapFile());
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public Blocks getBlocks() {
        return blocks;
    }

    public Areas getAreas() {
        return areas;
    }

    public Teams getTeams() {
        return teams;
    }

    public Sets getSets() {
        return sets;
    }

    public UFAController getUfaController() {
        return (UFAController) modeController;
    }

    public Config getConfig() {
        return config;
    }

    public Message getMessage() {
        return message;
    }

    public ArrayList<String> check() {
        ArrayList<String> error = new ArrayList<>();
        if (!(Tool.checkWorld(gameLoc1, gameWorld) && Tool.checkWorld(gameLoc2, gameWorld))) {
            error.add(message.getMap("error.world_and_area")
                    .replace("{world1}", gameLoc1 != null && gameLoc1.getWorld() != null ? gameLoc1.getWorld().getName() : message.getNotFind())
                    .replace("{location1}", gameLoc1 != null ? gameLoc1.toVector().toString() : message.getNotFind())
                    .replace("{world2}", gameLoc2 != null && gameLoc2.getWorld() != null ? gameLoc2.getWorld().getName() : message.getNotFind())
                    .replace("{location2}", gameLoc2 != null ? gameLoc2.toVector().toString() : message.getNotFind()));
        }
        ArrayList<String> sErrors = sets.check();
        if (!sErrors.isEmpty()) {
            error.addAll(sErrors);
        }
        ArrayList<String> bErrors = blocks.check();
        if (!bErrors.isEmpty()) {
            error.addAll(bErrors);
        }
        ArrayList<String> aErrors = areas.check();
        if (!aErrors.isEmpty()) {
            error.addAll(aErrors);
        }
        ArrayList<String> tErrors = teams.check();
        if (!tErrors.isEmpty()) {
            error.addAll(tErrors);
        }
        if (user != null) {
            Player player = Bukkit.getPlayer(user);
            if (player != null) {
                if (error.isEmpty()) {
                    player.sendMessage(message.getMap("message.check"));
                } else {
                    player.sendMessage(message.getMap("error.check"));
                    for (String e : error) {
                        player.sendMessage(e);
                    }
                }

            }
        }
        return error;
    }

    public String editInfo() {
        return message.getMap("info.map")
                .replace("{name}", name != null ? name : message.getNotFind())
                .replace("{world}", gameWorld != null ? gameWorld.getName() : message.getNotFind())
                .replace("{world1}", gameLoc1 != null && gameLoc1.getWorld() != null ? gameLoc1.getWorld().getName() : message.getNotFind())
                .replace("{location1}", gameLoc1 != null ? gameLoc1.toVector().toString() : message.getNotFind())
                .replace("{world2}", gameLoc2 != null && gameLoc2.getWorld() != null ? gameLoc2.getWorld().getName() : message.getNotFind())
                .replace("{location2}", gameLoc2 != null ? gameLoc2.toVector().toString() : message.getNotFind());
    }

    public String gameInfo() {
        if (getUfaController().getStage() == UFAController.STOP) {
            return message.getMap("info.map")
                    .replace("{name}", name != null ? name : message.getNotFind())
                    .replace("{world}", gameWorld != null ? gameWorld.getName() : message.getNotFind())
                    .replace("{world1}", gameLoc1 != null && gameLoc1.getWorld() != null ? gameLoc1.getWorld().getName() : message.getNotFind())
                    .replace("{location1}", gameLoc1 != null ? gameLoc1.toVector().toString() : message.getNotFind())
                    .replace("{world2}", gameLoc2 != null && gameLoc2.getWorld() != null ? gameLoc2.getWorld().getName() : message.getNotFind())
                    .replace("{location2}", gameLoc2 != null ? gameLoc2.toVector().toString() : message.getNotFind());
        } else {
            RandomEvent randomEvent = ((UFAController) modeController).getEvents().getRandomEvent();
            return message.getGame("info.map", this)
                    .replace("{stage}", String.valueOf(((UFAController) modeController).getStage()))
                    .replace("{time}", String.valueOf(((UFAController) modeController).getTime()))
                    .replace("{gametime}", String.valueOf(((UFAController) modeController).getGameTime()))
                    .replace("{event}", String.valueOf(randomEvent != null ? randomEvent.getName() : "null"))
                    .replace("{eventtime}", String.valueOf(randomEvent != null ? randomEvent.getAllTime() : "null"))
                    .replace("{etime}", String.valueOf(randomEvent != null ? config.getEventTime() : "null"));
        }
    }

    public boolean load() {
        FileConfiguration save = YamlConfiguration.loadConfiguration(getMapFile());
        String world = save.getString("world");
        gameWorld = (Bukkit.getWorld(world != null ? world : ""));
        Vector vector1 = save.getVector("pos1");
        gameLoc1 = new Location(gameWorld, vector1.getX(), vector1.getY(), vector1.getZ());
        Vector vector2 = save.getVector("pos2");
        gameLoc2 = new Location(gameWorld, vector2.getX(), vector2.getY(), vector2.getZ());
        blocks.load(save);
        areas.load(save);
        teams.load(save);
        sets.load(save);
        config.reload();
        message.reload();
        return true;
    }
}
