package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.Team;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Fire extends RandomEvent {
    private final HashMap<Team, UFAPlayer> bag;
    private final int score = 6;

    public Fire(UFAMap map) {
        super(map);
        name = "fire";
        bag = new HashMap<>();
    }

    @Override
    public void eventStart() {
        super.eventStart();
        for (Team team : map.getTeams().getTeams()) {
            bag.put(team, null);
        }
    }

    @Override
    public void event(Object... objects) {
        String en = map.getMessage().getGame("events." + name + ".event", map);
        String ab = en.substring(0, en.lastIndexOf("|"));
        String getB = ab.substring(0, ab.lastIndexOf("|"));
        String reB = ab.substring(ab.lastIndexOf("|") + 1);
        String getS = en.substring(en.lastIndexOf("|") + 1);
        if (objects[0] instanceof PlayerMoveEvent && objects[1] instanceof UFAPlayer) {
            UFAPlayer ufaPlayer = (UFAPlayer) objects[1];
            Team team = getTeamByTeamSpawn(ufaPlayer.getPlayer().getLocation());
            if (team == null) {
                return;
            }
            if (team.equals(ufaPlayer.getTeam())) {
                if (!bag.containsValue(ufaPlayer)) {
                    return;
                }
                Team bagTeam = getTeamByBagPlayer(ufaPlayer);
                if (bagTeam == null) {
                    return;
                }
                bag.replace(bagTeam, null);
                ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
                ufaPlayer.addScore(score);
                map.getUfaController().playSound(Sound.BLOCK_CHEST_CLOSE);
                map.getUfaController().sendMessage(getS
                        .replace("{playerteam}", team.getName())
                        .replace("{player}", ufaPlayer.getPlayer().getName())
                        .replace("{bagteam}", bagTeam.getName())
                        .replace("{score}", String.valueOf(score)));
            } else {
                if (bag.get(team) != null) {
                    return;
                }
                bag.replace(team, ufaPlayer);
                ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, allTime * 20, 0));
                map.getUfaController().playSound(Sound.BLOCK_CHEST_OPEN);
                map.getUfaController().sendMessage(getB
                        .replace("{playerteam}", ufaPlayer.getTeam().getName())
                        .replace("{player}", ufaPlayer.getPlayer().getName())
                        .replace("{bagteam}", team.getName()));
            }
        }
        if (objects[0] instanceof PlayerDeathEvent && objects[1] instanceof UFAPlayer) {
            UFAPlayer ufaPlayer = (UFAPlayer) objects[1];
            for (Team team : bag.keySet()) {
                if (ufaPlayer.equals(bag.get(team))) {
                    bag.replace(team, null);
                    map.getUfaController().playSound(Sound.BLOCK_CHEST_LOCKED);
                    map.getUfaController().sendMessage(reB
                            .replace("{bagteam}", team.getName()));
                    return;
                }
            }
        }
    }

    private Team getTeamByTeamSpawn(Location location) {
        for (Team team : bag.keySet()) {
            if (Tool.check(team.getSpawn1(), team.getSpawn2(), location)) {
                return team;
            }
        }
        return null;
    }

    private Team getTeamByBagPlayer(UFAPlayer ufaPlayer) {
        for (Team team : bag.keySet()) {
            UFAPlayer bager = bag.get(team);
            if (bager == null) {
                continue;
            }
            if (bag.get(team).equals(ufaPlayer)) {
                return team;
            }
        }
        return null;
    }

    @Override
    public void eventStop() {
        super.eventStop();
        for (Team team : bag.keySet()) {
            UFAPlayer ufaPlayer = bag.get(team);
            if (ufaPlayer == null) {
                continue;
            }
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
        }
    }
}
