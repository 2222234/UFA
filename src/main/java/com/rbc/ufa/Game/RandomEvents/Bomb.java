package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Bomb extends RandomEvent {
    public Bomb(UFAMap map) {
        super(map);
        name = "bomb";
    }

    @Override
    public void event(Object... objects) {
        if (objects[0] instanceof PlayerDeathEvent && objects[1] instanceof UFAPlayer) {
            PlayerDeathEvent pde = (PlayerDeathEvent) objects[0];
            UFAPlayer boom = (UFAPlayer) objects[1];
            map.getUfaController().sendMessage(map.getMessage().getGame("events." + name + ".event", map));
            map.getUfaController().playSound(boom.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE);

            for (UFAPlayer vic : map.getUfaController().getGamePlayers()) {
                if (!(boom.getPlayer().getLocation().distance(vic.getPlayer().getLocation()) <= 10d)) {
                    continue;
                }
                vic.getPlayer().damage(40.0d);
                if (vic.getPlayer().getHealth() <= 0 && vic.getPlayer().getKiller() == null) {
                    if (!boom.getTeam().equals(vic.getTeam())) {
                        score(map, pde, boom, vic);
                    } else {
                        if (map.getSets().getKillMatePunish() == 0) {
                            killMateNotPunish(map, pde, boom, vic);
                        } else {
                            killMatePunish(map, boom, vic);
                        }
                    }
                }
            }
        }
    }

    private void score(UFAMap map, PlayerDeathEvent pde, UFAPlayer killer, UFAPlayer vic) {
        killer.addScore(map.getSets().getKillScore());
        map.getUfaController().sendMessage(
                map.getMessage().getGame("message.game_getscore_kill", map)
                        .replace("{killteam}", killer.getTeam().getName())
                        .replace("{killplayer}", killer.getPlayer().getName())
                        .replace("{victeam}", vic.getTeam().getName())
                        .replace("{vicplayer}", vic.getPlayer().getName())
                        .replace("{score}", String.valueOf(map.getSets().getKillScore())));
        killer.getPlayer().playSound(pde.getEntity().getLocation(),
                Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,
                map.getConfig().getVolume(),
                map.getConfig().getPitch());
        if (map.getSets().isRandomEvents()) {
            Events events = map.getUfaController().getEvents();
            if (events.getEvent() == Events.LUCK) {
                events.getRandomEvent().event(killer, map.getSets().getKillScore());
                if (events.getRandomEvent().isReplaceFunction()) {
                    return;
                }
            }
        }
    }

    private void killMateNotPunish(UFAMap map, PlayerDeathEvent pde, UFAPlayer killer, UFAPlayer vic) {
        map.getUfaController().sendMessage(
                map.getMessage().getGame("message.game_killmate", map)
                        .replace("{team}", killer.getTeam().getName())
                        .replace("{killer}", killer.getPlayer().getName())
                        .replace("{vic}", vic.getPlayer().getName()));
        killer.getPlayer().playSound(pde.getEntity().getLocation(),
                Sound.ENTITY_VILLAGER_DEATH,
                map.getConfig().getVolume(),
                map.getConfig().getPitch());
    }

    private void killMatePunish(UFAMap map, UFAPlayer killer, UFAPlayer vic) {
        killer.addScore(-map.getSets().getKillMatePunish());
        map.getUfaController().sendMessage(
                map.getMessage().getGame("message.game_killmatepunish", map)
                        .replace("{team}", killer.getTeam().getName())
                        .replace("{killer}", killer.getPlayer().getName())
                        .replace("{vic}", vic.getPlayer().getName())
                        .replace("{killmatepunish}", String.valueOf(map.getSets().getKillMatePunish())));
    }
}
