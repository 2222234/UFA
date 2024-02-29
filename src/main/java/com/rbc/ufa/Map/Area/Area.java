package com.rbc.ufa.Map.Area;

import com.rbc.gunfight.GunFight;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Area {
    private final UFAMap map;
    private BukkitRunnable rep;
    private Location location1;
    private Location location2;
    private int repTime;
    private int countTime;

    public Area(UFAMap map, Location location1, Location location2, int repTime) {
        this.location1 = location1;
        this.location2 = location2;
        this.repTime = repTime;
        this.map = map;
    }

    public boolean start() {
        repArea();
        rep = new BukkitRunnable() {
            int count = repTime;

            @Override
            public void run() {
                countTime = count;
                if (count == 0) {
                    repArea();
                    count = repTime;
                }
                if (count > 0) {
                    count--;
                }
            }
        };
        rep.runTaskTimer(GunFight.getPlugin(GunFight.class), 20L, 20L);
        return true;
    }

    public boolean stop() {
        if (rep.isCancelled()) {
            return false;
        }
        rep.cancel();
        return true;
    }

    public void repArea() {
        Vector distanceVec = location1.clone().subtract(location2).toVector();
        Vector addVec = new Vector();
        addVec.setX(distanceVec.getBlockX() == 0 ? distanceVec.getBlockX() : distanceVec.getBlockX() / Math.abs(distanceVec.getBlockX()));
        addVec.setY(distanceVec.getBlockY() == 0 ? distanceVec.getBlockY() : distanceVec.getBlockY() / Math.abs(distanceVec.getBlockY()));
        addVec.setZ(distanceVec.getBlockZ() == 0 ? distanceVec.getBlockZ() : distanceVec.getBlockZ() / Math.abs(distanceVec.getBlockZ()));
        int x = 0;
        do {
            int y = 0;
            do {
                int z = 0;
                do {
                    Block block = location1.clone().add(-x, -y, -z).getBlock();
                    if (block.getType().isAir()) {
                        block.setType(Material.GLASS);
                    }
                    z += addVec.getBlockZ();
                } while (Math.abs(z) <= Math.abs(distanceVec.getBlockZ()) && z != 0);
                y += addVec.getBlockY();
            } while (Math.abs(y) <= Math.abs(distanceVec.getBlockY()) && y != 0);
            x += addVec.getBlockX();
        } while (Math.abs(x) <= Math.abs(distanceVec.getBlockX()) && x != 0);
    }

    public Location getLocation1() {
        return location1;
    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    public int getRepTime() {
        return repTime;
    }

    public void setRepTime(int repTime) {
        this.repTime = repTime;
    }

    public String check() {
        String error = "";
        if (!(Tool.check(map.getGameLoc1(), map.getGameLoc2(), location1) && Tool.check(map.getGameLoc1(), map.getGameLoc2(), location2))) {
            error = error.concat(map.getMessage().getMap("error.area")
                    .replace("{num}", String.valueOf(map.getAreas().getNum(this)))
                    .replace("{world1}", location1 != null && location1.getWorld() != null ? location1.getWorld().getName() : map.getMessage().getNotFind())
                    .replace("{location1}", location1 != null ? location1.toVector().toString() : map.getMessage().getNotFind())
                    .replace("{world2}", location2 != null && location2.getWorld() != null ? location2.getWorld().getName() : map.getMessage().getNotFind())
                    .replace("{location2}", location2 != null ? location2.toVector().toString() : map.getMessage().getNotFind()));
        }
        return error;
    }

    public String info() {
        String info = map.getMessage().getMap("info.area")
                .replace("{num}", String.valueOf(map.getAreas().getNum(this)))
                .replace("{world1}", location1 != null && location1.getWorld() != null ? location1.getWorld().getName() : map.getMessage().getNotFind())
                .replace("{location1}", location1 != null ? location1.toVector().toString() : map.getMessage().getNotFind())
                .replace("{world2}", location2 != null && location2.getWorld() != null ? location2.getWorld().getName() : map.getMessage().getNotFind())
                .replace("{location2}", location2 != null ? location2.toVector().toString() : map.getMessage().getNotFind())
                .replace("{counttime}", String.valueOf(countTime))
                .replace("{reptime}", String.valueOf(repTime));
        return info;
    }
}
