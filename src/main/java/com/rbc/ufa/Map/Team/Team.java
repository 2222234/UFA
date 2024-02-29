package com.rbc.ufa.Map.Team;

import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class Team {
    private final UFAMap map;
    private String name;
    private Location prepare1;
    private Location prepare2;
    private Location spawn1;
    private Location spawn2;
    private Location initbag;
    private int score;

    public Team(UFAMap map) {
        this.map = map;
    }

    public Location getSpawnCenter() {
        Vector vector = spawn1.toVector();
        vector.subtract(spawn2.toVector());
        vector.multiply(-0.5f);
        return spawn1.clone().add(vector);
    }

    public void addScore(int score) {
        this.score = this.score + score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getPrepare1() {
        return prepare1;
    }

    public void setPrepare1(Location prepare1) {
        this.prepare1 = prepare1;
    }

    public Location getPrepare2() {
        return prepare2;
    }

    public void setPrepare2(Location prepare2) {
        this.prepare2 = prepare2;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public void setSpawn1(Location spawn1) {
        this.spawn1 = spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public void setSpawn2(Location spawn2) {
        this.spawn2 = spawn2;
    }

    public Location getInitbag() {
        return initbag;
    }

    public void setInitbag(Location initbag) {
        this.initbag = initbag;
    }

    public String check() {
        String error = "";
        if (name == null) {
            error = error.concat(map.getMessage().getMap("error.team_name"))
                    .replace("{num}", String.valueOf(map.getTeams().getNum(this)));
        }
        if (!(Tool.check(map.getGameLoc1(), map.getGameLoc2(), prepare1) && Tool.check(map.getGameLoc1(), map.getGameLoc2(), prepare2))) {
            error = error.concat(map.getMessage().getMap("error.team_prepare")
                    .replace("{num}", String.valueOf(map.getTeams().getNum(this)))
                    .replace("{world1}", prepare1 != null && prepare1.getWorld() != null ? prepare1.getWorld().getName() : map.getMessage().getNotFind())
                    .replace("{location1}", prepare1 != null ? prepare1.toVector().toString() : map.getMessage().getNotFind())
                    .replace("{world2}", prepare2 != null && prepare2.getWorld() != null ? prepare2.getWorld().getName() : map.getMessage().getNotFind())
                    .replace("{location2}", prepare2 != null ? prepare2.toVector().toString() : map.getMessage().getNotFind()));

        }
        if (!(Tool.check(map.getGameLoc1(), map.getGameLoc2(), spawn1) && Tool.check(map.getGameLoc1(), map.getGameLoc2(), spawn2))) {
            error = error.concat(map.getMessage().getMap("error.team_spawn")
                    .replace("{num}", String.valueOf(map.getTeams().getNum(this)))
                    .replace("{world1}", spawn1 != null && spawn1.getWorld() != null ? spawn1.getWorld().getName() : map.getMessage().getNotFind())
                    .replace("{location1}", spawn1 != null ? spawn1.toVector().toString() : map.getMessage().getNotFind())
                    .replace("{world2}", spawn2 != null && spawn2.getWorld() != null ? spawn2.getWorld().getName() : map.getMessage().getNotFind())
                    .replace("{location2}", spawn2 != null ? spawn2.toVector().toString() : map.getMessage().getNotFind())
            );
        }
        if (initbag == null || initbag.getWorld() == null) {
            error = error.concat(map.getMessage().getMap("error.team_initbag_null")
                    .replace("{num}", String.valueOf(map.getTeams().getNum(this))));
        } else if (!initbag.getBlock().getType().equals(Material.CHEST)) {
            error = error.concat(map.getMessage().getMap("error.team_initbag")
                            .replace("{num}", String.valueOf(map.getTeams().getNum(this))))
                    .replace("{world}", initbag != null && initbag.getWorld() != null ? initbag.getWorld().getName() : map.getMessage().getNotFind())
                    .replace("{location}", initbag != null ? initbag.toVector().toString() : map.getMessage().getNotFind());
        }
        return error;
    }


    public String info() {
        String mates = "";
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            if (ufaPlayer.getTeam().equals(this)) {
                if (mates.isEmpty()) {
                    mates = mates.concat(ufaPlayer.getPlayer().getName());
                } else {
                    mates = mates.concat(" ").concat(ufaPlayer.getPlayer().getName());
                }
            }
        }
        String info = map.getMessage().getMap("info.team")
                .replace("{num}", String.valueOf(map.getTeams().getNum(this)))
                .replace("{name}", name != null ? name : map.getMessage().getNotFind())
                .replace("{mates}", mates)
                .replace("{score}", String.valueOf(score))
                .replace("{prepareworld1}", prepare1 != null && prepare1.getWorld() != null ? prepare1.getWorld().getName() : map.getMessage().getNotFind())
                .replace("{preparelocation1}", prepare1 != null ? prepare1.toVector().toString() : map.getMessage().getNotFind())
                .replace("{prepareworld2}", prepare2 != null && prepare2.getWorld() != null ? prepare2.getWorld().getName() : map.getMessage().getNotFind())
                .replace("{preparelocation2}", prepare2 != null ? prepare2.toVector().toString() : map.getMessage().getNotFind())
                .replace("{spawnworld1}", spawn1 != null && spawn1.getWorld() != null ? spawn1.getWorld().getName() : map.getMessage().getNotFind())
                .replace("{spawnlocation1}", spawn1 != null ? spawn1.toVector().toString() : map.getMessage().getNotFind())
                .replace("{spawnworld2}", spawn2 != null && spawn2.getWorld() != null ? spawn2.getWorld().getName() : map.getMessage().getNotFind())
                .replace("{spawnlocation2}", spawn2 != null ? spawn2.toVector().toString() : map.getMessage().getNotFind())
                .replace("{initbagworld}", initbag != null && initbag.getWorld() != null ? initbag.getWorld().getName() : map.getMessage().getNotFind())
                .replace("{initbaglocation}", initbag != null ? initbag.toVector().toString() : map.getMessage().getNotFind());
        return info;
    }
}
