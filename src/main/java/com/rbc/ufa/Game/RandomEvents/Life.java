package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.Team;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Life extends RandomEvent {
    private final HashMap<Team, ArrayList<UFAPlayer>> players;
    private final HashMap<Team, HashSet<UFAPlayer>> heros;
    private final HashMap<Team, UFAPlayer> hero;
    private final HashMap<Team, Integer> stop;

    public Life(UFAMap map) {
        super(map);
        name = "life";
        players = new HashMap<>();
        heros = new HashMap<>();
        hero = new HashMap<>();
        stop = new HashMap<>();
    }

    @Override
    public void eventStart() {
        super.eventStart();
        for (Team team : map.getTeams().getTeams()) {
            players.put(team, new ArrayList<>());
            heros.put(team, new HashSet<>());
            stop.put(team, 1);
        }
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            ArrayList<UFAPlayer> p = players.get(ufaPlayer.getTeam());
            p.add(ufaPlayer);
            players.replace(ufaPlayer.getTeam(), p);
        }
        for (Team team : map.getTeams().getTeams()) {
            hero.put(team, randomHero(team));
        }
    }

    private UFAPlayer randomHero(Team team) {
        ArrayList<UFAPlayer> teamMates = players.get(team);
        HashSet<UFAPlayer> teamHeros = heros.get(team);

        int rp = new Random().nextInt(teamMates.size());
        UFAPlayer ufaPlayer = teamMates.get(rp);
        boolean check = teamHeros.contains(ufaPlayer);

        while (check && teamHeros.size() != teamMates.size()) {
            rp = new Random().nextInt(teamMates.size());
            ufaPlayer = teamMates.get(rp);
            check = teamHeros.contains(ufaPlayer);
        }
        if (teamHeros.size() == teamMates.size()) {
            teamHeros.clear();
        }
        teamHeros.add(ufaPlayer);
        heros.replace(team, teamHeros);
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, allTime * 20, 0));
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, allTime * 20, 0));
        ufaPlayer.setMaxBlood(map.getSets().getMaxBlood() + 40.0d);
        map.getUfaController().playSound(ufaPlayer.getPlayer().getLocation(), Sound.ITEM_TOTEM_USE);
        String en = map.getMessage().getGame("events." + name + ".event", map);
        String gh = en.substring(0, en.lastIndexOf("|"));
        map.getUfaController().sendMessage(gh
                .replace("{team}", team.getName())
                .replace("{hero}", ufaPlayer.getPlayer().getName()));
        return ufaPlayer;
    }

    @Override
    public void event(Object... objects) {
        if (objects[0] instanceof PlayerDeathEvent && objects[1] instanceof UFAPlayer) {
            String en = map.getMessage().getGame("events." + name + ".event", map);
            String hd = en.substring(en.lastIndexOf("|") + 1);
            UFAPlayer ufaPlayer = (UFAPlayer) objects[1];
            UFAPlayer hp = hero.get(ufaPlayer.getTeam());
            if (hp == null) {
                return;
            }
            if (hero.get(ufaPlayer.getTeam()).equals(ufaPlayer)) {
                hero.put(ufaPlayer.getTeam(), null);
                stop.put(ufaPlayer.getTeam(), 10);
                map.getUfaController().playSound(ufaPlayer.getPlayer().getLocation(), Sound.EVENT_RAID_HORN);
                map.getUfaController().sendMessage(hd
                        .replace("{team}", ufaPlayer.getTeam().getName())
                        .replace("{hero}", ufaPlayer.getPlayer().getName()));
            }
            return;
        }
        if (objects[0] instanceof PlayerMoveEvent && objects[1] instanceof UFAPlayer) {
            PlayerMoveEvent pme = (PlayerMoveEvent) objects[0];
            UFAPlayer ufaPlayer = (UFAPlayer) objects[1];
            if (stop.get(ufaPlayer.getTeam()) > 1) {
                pme.setCancelled(true);
            }
        }
    }

    @Override
    public void timePerSecond(Object... objects) {
        super.timePerSecond(objects);
        for (Team team : stop.keySet()) {
            int time = stop.get(team);
            if (time > 1) {
                time--;
                stop.put(team, time);
            }
            if (time == 1) {
                if (hero.get(team) == null) {
                    hero.put(team, randomHero(team));
                }
            }
        }
    }

    @Override
    public void eventStop() {
        super.eventStop();
        for (Team team : hero.keySet()) {
            UFAPlayer ufaPlayer = hero.get(team);
            if (ufaPlayer == null) {
                continue;
            }
            ufaPlayer.setMaxBlood(map.getSets().getMaxBlood());
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
        }
        heros.clear();
        hero.clear();
        stop.clear();
    }
}
