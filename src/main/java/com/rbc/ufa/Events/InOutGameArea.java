package com.rbc.ufa.Events;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.RandomEvents.Events;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import com.rbc.ufa.Tools.TpTeam;
import com.rbc.ufa.UFA;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class InOutGameArea implements Listener {
    private final ModeManager modeManager;

    public InOutGameArea(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @EventHandler
    public void ioga(PlayerMoveEvent pme) {
        ArrayList<ModeMap> maps = modeManager.getMapManager().getMaps(modeManager.getMode(UFA.class));
        UFAMap map = UFAMap.getMapByPlayer(maps, pme.getPlayer());
        if (map == null) {
            return;
        }
        if (map.getUfaController().getStage() == UFAController.PREPARE) {
            prepare(map, pme);
            return;
        }

        if (map.getUfaController().getStage() == UFAController.GAMING) {
            gaming(map, pme);
        }
    }

    private void prepare(UFAMap map, PlayerMoveEvent pme) {
        UFAPlayer player = map.getUfaController().getPlayerInOther(pme.getPlayer());
        if (Tool.check(map.getGameLoc1(), map.getGameLoc2(), pme.getPlayer().getLocation())) {
            if (player == null) {
                player = new UFAPlayer(pme.getPlayer());
                map.getUfaController().addOtherPlayers(player);
                map.getUfaController().checkVote();
            }
        } else if (player != null) {
            map.getUfaController().removeOtherPlayers(player);
            player.setBefGM();
            map.getUfaController().removeVote(pme.getPlayer().getName());
            map.getUfaController().checkVote();
        }
    }

    private void gaming(UFAMap map, PlayerMoveEvent pme) {
        UFAPlayer player = map.getUfaController().getPlayerInGame(pme.getPlayer());
        if (player != null) {
            if (!Tool.check(map.getGameLoc1(), map.getGameLoc2(), pme.getFrom())) {
                TpTeam.randomSpawn(map, player);
                return;
            }
            if (!Tool.check(map.getGameLoc1(), map.getGameLoc2(), pme.getTo())) {
                pme.setCancelled(true);
            }
            Events events = map.getUfaController().getEvents();
            events.event(Events.FIRE, pme, player);
            events.event(Events.LIFE, pme, player);
        } else {
            player = map.getUfaController().getPlayerInOther(pme.getPlayer());
            if (player == null) {
                if (Tool.check(map.getGameLoc1(), map.getGameLoc2(), pme.getTo())) {
                    if (map.getUfaController().quitsContainsPlayer(pme.getPlayer().getName())) {
                        return;
                    }
                    player = new UFAPlayer((pme.getPlayer()));
                    map.getUfaController().addOtherPlayers(player);
                    if (player.getPlayer().getGameMode() != GameMode.SPECTATOR) {
                        player.setGM(GameMode.SPECTATOR);
                    }
                }
            } else {
                if (!Tool.check(map.getGameLoc1(), map.getGameLoc2(), pme.getTo())) {
                    map.getUfaController().removeOtherPlayers(player);
                    player.setBefGM();
                }
            }
        }
    }
}
