package com.rbc.ufa.Map.Block;

import com.rbc.gunfight.GunFight;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class Block {
    private final UFAMap map;
    private BukkitRunnable rep;
    private Location location;
    private int score;
    private int repTime;
    private int countTime;
    private boolean r = true;

    public Block(UFAMap map, Location location, int score, int repTime) {
        this.location = location;
        this.score = score;
        this.repTime = repTime;
        this.map = map;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean start() {
        repBlock(false);
        rep = new BukkitRunnable() {
            int count = repTime;

            @Override
            public void run() {
                if (!r) {
                    countTime = count;
                    if (count == 0) {
                        repBlock(true);
                        count = repTime;
                        r = true;
                    }
                    if (count > 0) {
                        count--;
                    }
                }
            }
        };
        rep.runTaskTimer(GunFight.getPlugin(GunFight.class), 20L, 20L);
        return true;
    }

    public void restart() {
        r = false;
    }

    public boolean stop() {
        if (rep.isCancelled()) {
            return false;
        }
        rep.cancel();
        return true;
    }

    public void repBlock(boolean sendMessage) {
        if (!location.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {
            location.getBlock().setType(Material.DIAMOND_BLOCK);
            if (sendMessage) {
                map.getUfaController().sendMessage(map.getMessage().getGame("message.game_repblock", map));
            }
        }
    }

    public int getRepTime() {
        return repTime;
    }

    public void setRepTime(int repTime) {
        this.repTime = repTime;
    }

    public String check() {
        String error = "";
        if (!(Tool.check(map.getGameLoc1(), map.getGameLoc2(), location))) {
            error = error.concat(map.getMessage().getMap("error.block")
                    .replace("{num}", String.valueOf(map.getBlocks().getNum(this)))
                    .replace("{world}", location != null && location.getWorld() != null ? location.getWorld().getName() : map.getMessage().getNotFind())
                    .replace("{location}", location != null ? location.toVector().toString() : map.getMessage().getNotFind()));
        }
        return error;
    }


    public String info() {
        String info = map.getMessage().getMap("info.block")
                .replace("{num}", String.valueOf(map.getBlocks().getNum(this)))
                .replace("{world}", location != null && location.getWorld() != null ? location.getWorld().getName() : map.getMessage().getNotFind())
                .replace("{location}", location != null ? location.toVector().toString() : map.getMessage().getNotFind())
                .replace("{score}", String.valueOf(score))
                .replace("{counttime}", String.valueOf(countTime))
                .replace("{reptime}", String.valueOf(repTime));
        return info;
    }
}
