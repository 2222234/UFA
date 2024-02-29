package com.rbc.ufa.Tools;

import com.rbc.ufa.Map.Team.Team;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Location;

import java.util.*;

public class TpTeam {

    public static void randomTeam(UFAMap map) {
        ArrayList<UFAPlayer> gp = new ArrayList<>();
        ArrayList<Team> teams = map.getTeams().getTeams();
        HashSet<Integer> done = new HashSet<>();
        for (UFAPlayer ufaPlayer : map.getUfaController().getOtherPlayers()) {
            int num = new Random().nextInt(teams.size());
            boolean d = done.contains(num);
            while (d && done.size() != teams.size()) {
                num = new Random().nextInt(teams.size());
                d = done.contains(num);
            }
            if (done.size() == teams.size()) {
                done.clear();
            }
            done.add(num);
            ufaPlayer.setTeam(teams.get(num));
            gp.add(ufaPlayer);
        }
        map.getUfaController().movePlayerFromOtherToGame(gp);
    }

    public static void pickToTeam(UFAMap map) {
        ArrayList<UFAPlayer> gp = new ArrayList<>();
        for (Team team : map.getTeams().getTeams()) {
            for (UFAPlayer ufaPlayer : map.getUfaController().getOtherPlayers()) {
                if (Tool.check(team.getPrepare1(), team.getPrepare2(), ufaPlayer.getPlayer().getLocation())) {
                    ufaPlayer.setTeam(team);
                    gp.add(ufaPlayer);
                }
            }
        }
        map.getUfaController().movePlayerFromOtherToGame(gp);
    }

    public static void pickToBalTeam(UFAMap map) {
        Integer[] membersnum = new Integer[map.getTeams().getTeams().size()];
        ArrayList<Team> teams = map.getTeams().getTeams();
        for (Team team : teams) {
            membersnum[teams.indexOf(team)] = 0;
            Team gameTeam = map.getTeams().getTeams().get(teams.indexOf(team));
            for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
                if (ufaPlayer.getTeam().equals(gameTeam)) {
                    membersnum[teams.indexOf(team)]++;
                }
            }
        }
        int min = Collections.min(Arrays.asList(membersnum));
        int max = Collections.max(Arrays.asList(membersnum));
        if (max - min > 1) {
            Team minTeam = map.getTeams().getTeams().get(Tool.getSubscript(Arrays.asList(membersnum), min));
            Team maxTeam = map.getTeams().getTeams().get(Tool.getSubscript(Arrays.asList(membersnum), max));
            ArrayList<UFAPlayer> maxPlayers = new ArrayList<>();
            for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
                if (ufaPlayer.getTeam().equals(maxTeam)) {
                    maxPlayers.add(ufaPlayer);
                }
            }
            int lucky = new Random().nextInt(maxPlayers.size());
            maxPlayers.get(lucky).setTeam(minTeam);
            maxPlayers.get(lucky).getPlayer().sendMessage(
                    map.getMessage().getGame("message.game_balmate", map)
                            .replace("{team}", minTeam.getName()));
            maxPlayers.remove(lucky);
            pickToBalTeam(map);
        }
    }


    public static void randomPrepare(UFAPlayer ufaPlayer) {
        if (ufaPlayer.getTeam() != null) {
            Team team = ufaPlayer.getTeam();
            ufaPlayer.getPlayer().teleport(randomArea(team.getPrepare1(), team.getPrepare2()));
        }
    }

    public static void randomSpawn(UFAMap map, UFAPlayer ufaPlayer) {
        if (ufaPlayer.getTeam() != null) {
            Team team;
            if (!map.getSets().isRandomSpawn()) {
                team = ufaPlayer.getTeam();
            } else {
                ArrayList<Team> teams = map.getTeams().getTeams();
                team = teams.get(new Random().nextInt(teams.size()));
            }
            ufaPlayer.getPlayer().teleport(randomArea(team.getSpawn1(), team.getSpawn2()));
        }
    }

    private static Location randomArea(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return null;
        }
        int x = location1.getBlockX() - location2.getBlockX();
        int x1 = x != 0 ? x / Math.abs(x) : 0;
        int y = Math.min(location1.getBlockY(), location2.getBlockY());
        int z = location1.getBlockZ() - location2.getBlockZ();
        int z1 = z != 0 ? z / Math.abs(z) : 0;
        Location spawn = location1.clone().subtract(
                new Random().nextInt(Math.abs(x) + 1) * x1,
                0,
                new Random().nextInt(Math.abs(z) + 1) * z1
        );
        spawn.setY(y);
        spawn.add(0.5, 0, 0.5);
        return spawn;
    }
}
