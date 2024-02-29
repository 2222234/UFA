package com.rbc.ufa.Events;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.RandomEvents.Events;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.UFA;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class GetScoreFromKill implements Listener {
    private final ModeManager modeManager;

    public GetScoreFromKill(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @EventHandler
    public void gsfk(PlayerDeathEvent pde) {
        ArrayList<ModeMap> maps = modeManager.getMapManager().getMaps(modeManager.getMode(UFA.class));
        UFAMap map = UFAMap.getMapByPlayer(maps, pde.getEntity());
        if (map == null) {
            return;
        }
        if (map.getUfaController().getStage() != UFAController.GAMING) {
            return;
        }
        UFAPlayer killer = map.getUfaController().getPlayerInGame(pde.getEntity().getKiller());
        UFAPlayer vic = map.getUfaController().getPlayerInGame(pde.getEntity());
        Events events = map.getUfaController().getEvents();
        if (killer != null || vic != null) {
            pde.setDeathMessage(null);
            if (killer == null) {
                events.event(Events.HIGH, pde, vic);
                events.event(Events.RAIN, pde, vic);
            }
            if (vic != null) {
                events.event(Events.FIRE, pde, vic);
                events.event(Events.LIFE, pde, vic);
                vic.addDie();
            }
        }
        if (killer == null || vic == null) {
            return;
        }

        if (events.event(Events.BOMB, pde, vic)) {
            return;
        }
        if (events.event(Events.KILL, pde)) {
            return;
        }
        kill(map, pde, killer, vic);
    }

    private void kill(UFAMap map, PlayerDeathEvent pde, UFAPlayer killer, UFAPlayer vic) {
        if (killer.equals(vic)) {
            suicide(map, killer);
            return;
        }
        if (!killer.getTeam().equals(vic.getTeam())) {
            score(map, pde, killer, vic);
        } else {
            if (map.getSets().getKillMatePunish() == 0) {
                killMateNotPunish(map, pde, killer, vic);
            } else {
                killMatePunish(map, killer, vic);
            }
        }
    }

    private void suicide(UFAMap map, UFAPlayer killer) {
        map.getUfaController().sendMessage(
                map.getMessage().getGame("message.game_suicide", map)
                        .replace("{team}", killer.getTeam().getName())
                        .replace("{player}", killer.getPlayer().getName()));
    }

    private void score(UFAMap map, PlayerDeathEvent pde, UFAPlayer killer, UFAPlayer vic) {
        killer.addScore(map.getSets().getKillScore());
        killer.addKill();
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

        map.getUfaController().getEvents().event(Events.LUCK, killer, map.getSets().getKillScore());
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
