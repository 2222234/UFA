package com.rbc.ufa.Map.Block;

import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Blocks {
    private final ArrayList<Block> blocks;
    private final UFAMap map;

    public Blocks(UFAMap map) {
        this.map = map;
        blocks = new ArrayList<>();
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public int getNum(Block block) {
        return blocks.indexOf(block);
    }

    public ArrayList<String> check() {
        ArrayList<String> errors = new ArrayList<>();
        for (Block block : blocks) {
            String check = block.check();
            if (!check.isEmpty()) {
                errors.add(check);
            }
        }
        return errors;
    }

    public void save(FileConfiguration save) {
        String path = "scorepoint.point";
        int num = 0;
        for (Block block : blocks) {
            String subPath = path + num + ".";
            save.set(subPath + "pos", block.getLocation().toVector());
            save.set(subPath + "score", block.getScore());
            save.set(subPath + "reptime", block.getRepTime());
            num++;
        }
    }

    public void load(FileConfiguration save) {
        blocks.clear();
        String path = "scorepoint.point";
        int num = 0;
        while (save.get(path + num) != null) {
            String subPath = path + num + ".";
            Vector vector = save.getVector(subPath + "pos");
            Location location = new Location(map.getGameWorld(), vector.getX(), vector.getY(), vector.getZ());
            int score = save.getInt(subPath + "score");
            int repTime = save.getInt(subPath + "reptime");
            blocks.add(new Block(map, location, score, repTime));
            num++;
        }
    }

    public ArrayList<String> info() {
        ArrayList<String> info = new ArrayList<>();
        for (Block block : blocks) {
            info.add(block.info());
        }
        return info;
    }

    public String infoNum(int num) {
        if (0 <= num && num < blocks.size()) {
            return blocks.get(num).info();
        }
        return null;
    }

    public String infoLast() {
        if (!blocks.isEmpty()) {
            return blocks.get(blocks.size() - 1).info();
        }
        return null;
    }

    public ArrayList<String> getLoc(Location location) {
        ArrayList<String> info = new ArrayList<>();
        for (Block block : blocks) {
            if (block.getLocation().equals(location)) {
                info.add(block.info());
            }
        }
        return info;
    }

    public boolean createBlock(Location location, int score, int repTime) {
        if (location != null) {
            blocks.add(new Block(map, location, score, repTime));
            return true;
        }
        return false;
    }

    public boolean removeBlock(int num) {
        if (0 <= num && num < blocks.size()) {
            blocks.remove(num);
            return true;
        }
        return false;
    }

    public boolean setLoc(int num, Location location) {
        if (0 <= num && num < blocks.size()) {
            if (location != null) {
                blocks.get(num).setLocation(location);
                return true;
            }
        }
        return false;
    }

    public boolean setScore(int num, int score) {
        if (0 <= num && num < blocks.size()) {
            blocks.get(num).setScore(score);
        }
        return false;
    }

    public boolean setRepTime(int num, int repTime) {
        if (0 <= num && num < blocks.size()) {
            blocks.get(num).setRepTime(repTime);
        }
        return false;
    }

    public boolean start() {
        for (Block block : blocks) {
            if (!block.start()) {
                return false;
            }
        }
        return true;
    }

    public boolean stop() {
        for (Block block : blocks) {
            if (!block.stop()) {
                return false;
            }
        }
        return true;
    }

    public void rep(int num, boolean sendMessage) {
        if (0 <= num && num < blocks.size()) {
            blocks.get(num).repBlock(sendMessage);
        }
    }
}
