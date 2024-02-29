package com.rbc.ufa.Map.Team;

import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Teams {
    private final ArrayList<Team> teams;
    private final UFAMap map;

    public Teams(UFAMap map) {
        this.map = map;
        teams = new ArrayList<>();
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public int getNum(Team team) {
        return teams.indexOf(team);
    }

    public ArrayList<String> check() {
        ArrayList<String> error = new ArrayList<>();
        for (Team team : teams) {
            if (!team.check().isEmpty()) {
                error.add(team.check());
            }
        }
        return error;
    }


    public void save(FileConfiguration save) {
        String path = "team";
        int num = 0;
        for (Team team : teams) {
            String subPath = path + num + ".";
            save.set(subPath + "name", team.getName());
            save.set(subPath + "prepare.pos1", team.getPrepare1().toVector());
            save.set(subPath + "prepare.pos2", team.getPrepare2().toVector());
            save.set(subPath + "spawn.pos1", team.getSpawn1().toVector());
            save.set(subPath + "spawn.pos2", team.getSpawn2().toVector());
            save.set(subPath + "initbag.pos", team.getInitbag().toVector());
            num++;
        }
    }

    public void load(FileConfiguration save) {
        teams.clear();
        String path = "team";
        int num = 0;
        while (save.get(path + num) != null) {
            String subPath = path + num + ".";
            Team team = new Team(map);
            team.setName(save.getString(subPath + "name"));
            Vector pVector1 = save.getVector(subPath + "prepare.pos1");
            Location prepare1 = new Location(map.getGameWorld(), pVector1.getX(), pVector1.getY(), pVector1.getZ());
            team.setPrepare1(prepare1);
            Vector pVector2 = save.getVector(subPath + "prepare.pos2");
            Location prepare2 = new Location(map.getGameWorld(), pVector2.getX(), pVector2.getY(), pVector2.getZ());
            team.setPrepare2(prepare2);
            Vector sVector1 = save.getVector(subPath + "spawn.pos1");
            Location spawn1 = new Location(map.getGameWorld(), sVector1.getX(), sVector1.getY(), sVector1.getZ());
            team.setSpawn1(spawn1);
            Vector sVector2 = save.getVector(subPath + "spawn.pos2");
            Location spawn2 = new Location(map.getGameWorld(), sVector2.getX(), sVector2.getY(), sVector2.getZ());
            team.setSpawn2(spawn2);
            Vector ibVector = save.getVector(subPath + "initbag.pos");
            Location initbag = new Location(map.getGameWorld(), ibVector.getX(), ibVector.getY(), ibVector.getZ());
            team.setInitbag(initbag);
            teams.add(team);
            num++;
        }
    }

    public ArrayList<String> info() {
        ArrayList<String> info = new ArrayList<>();
        for (Team team : teams) {
            info.add(team.info());
        }
        return info;
    }

    public String infoNum(int num) {
        if (0 <= num && num < teams.size()) {
            return teams.get(num).info();
        }
        return null;
    }

    public String infoLast() {
        if (!teams.isEmpty()) {
            return teams.get(teams.size() - 1).info();
        }
        return null;
    }

    public void createTeam() {
        teams.add(new Team(map));
    }

    public boolean removeTeam(int num) {
        if (0 <= num && num < teams.size()) {
            teams.remove(num);
            return true;
        }
        return false;
    }

    public boolean setName(int num, String name) {
        if (0 <= num && num < teams.size()) {
            teams.get(num).setName(name);
            return true;
        }
        return false;
    }

    public boolean setPrepareLoc(int num, Location location1, Location location2) {
        if (0 <= num && num < teams.size()) {
            if (location1 != null && location2 != null) {
                teams.get(num).setPrepare1(location1);
                teams.get(num).setPrepare2(location2);
                return true;
            }
        }
        return false;
    }

    public boolean setSpawnLoc(int num, Location location1, Location location2) {
        if (0 <= num && num < teams.size()) {
            if (location1 != null && location2 != null) {
                teams.get(num).setSpawn1(location1);
                teams.get(num).setSpawn2(location2);
                return true;
            }
        }
        return false;
    }

    public boolean setInitBag(int num, Location location1) {
        if (0 <= num && num < teams.size()) {
            if (location1 != null) {
                teams.get(num).setInitbag(location1);
                return true;
            }
        }
        return false;
    }
}
