package com.rbc.ufa.Map.Area;

import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Areas {
    private final ArrayList<Area> areas;
    private final UFAMap map;

    public Areas(UFAMap map) {
        this.map = map;
        areas = new ArrayList<>();
    }

    public int getNum(Area area) {
        return areas.indexOf(area);
    }

    public ArrayList<String> check() {
        ArrayList<String> errors = new ArrayList<>();
        for (Area area : areas) {
            String check = area.check();
            if (!check.isEmpty()) {
                errors.add(check);
            }
        }
        return errors;
    }


    public void save(FileConfiguration save) {
        String path = "blockarea.area";
        int num = 0;
        for (Area area : areas) {
            String subPath = path + num + ".";
            save.set(subPath + "pos1", area.getLocation1().toVector());
            save.set(subPath + "pos2", area.getLocation2().toVector());
            save.set(subPath + "reptime", area.getRepTime());
            num++;
        }
    }

    public void load(FileConfiguration save) {
        areas.clear();
        String path = "blockarea.area";
        int num = 0;
        while (save.get(path + num) != null) {
            String subPath = path + num + ".";
            Vector vector1 = save.getVector(subPath + "pos1");
            Location location1 = new Location(map.getGameWorld(), vector1.getX(), vector1.getY(), vector1.getZ());
            Vector vector2 = save.getVector(subPath + "pos2");
            Location location2 = new Location(map.getGameWorld(), vector2.getX(), vector2.getY(), vector2.getZ());
            int repTime = save.getInt(subPath + "reptime");
            Area area = new Area(map, location1, location2, repTime);
            areas.add(area);
            num++;
        }
    }

    public ArrayList<String> info() {
        ArrayList<String> info = new ArrayList<>();
        for (Area area : areas) {
            info.add(area.info());
        }
        return info;
    }

    public String infoNum(int num) {
        if (0 <= num && num < areas.size()) {
            return areas.get(num).info();
        }
        return null;
    }

    public String infoLast() {
        if (!areas.isEmpty()) {
            return areas.get(areas.size() - 1).info();
        }
        return null;
    }

    public ArrayList<String> getLoc(Location location) {
        ArrayList<String> info = new ArrayList<>();
        for (Area area : areas) {
            if (Tool.check(area.getLocation1(), area.getLocation2(), location)) {
                info.add(area.info());
            }
        }
        return info;
    }

    public boolean createArea(Location location1, Location location2, int repTime) {
        if (location1 != null && location2 != null) {
            areas.add(new Area(map, location1, location2, repTime));
            return true;
        }
        return false;
    }

    public boolean removeArea(int num) {
        if (0 <= num && num < areas.size()) {
            areas.remove(num);
            return true;
        }
        return false;
    }

    public boolean setLoc(int num, Location location1, Location location2) {
        if (0 <= num && num < areas.size()) {
            if (location1 != null && location2 != null) {
                areas.get(num).setLocation1(location1);
                areas.get(num).setLocation2(location2);
                return true;
            }
        }
        return false;
    }

    public boolean setRepTime(int num, int repTime) {
        if (0 <= num && num < areas.size()) {
            areas.get(num).setRepTime(repTime);
        }
        return false;
    }

    public boolean start() {
        for (Area area : areas) {
            if (!area.start()) {
                return false;
            }
        }
        return true;
    }

    public boolean stop() {
        for (Area area : areas) {
            if (!area.stop()) {
                return false;
            }
        }
        return true;
    }

    public boolean rep(int num) {
        if (0 <= num && num < areas.size()) {
            areas.get(num).repArea();
            return true;
        }
        return false;
    }

}
