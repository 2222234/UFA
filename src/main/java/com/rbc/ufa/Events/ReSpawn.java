package com.rbc.ufa.Events;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.Team.Team;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.UFA;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ReSpawn implements Listener {
    private final ModeManager modeManager;

    public ReSpawn(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

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

    @EventHandler
    public void rs(PlayerRespawnEvent pre) {
        ArrayList<ModeMap> maps = modeManager.getMapManager().getMaps(modeManager.getMode(UFA.class));
        UFAMap map = UFAMap.getMapByPlayer(maps, pre.getPlayer());
        if (map == null) {
            return;
        }
        if (map.getUfaController().getStage() != UFAController.GAMING) {
            return;
        }
        UFAPlayer ufaPlayer = map.getUfaController().getPlayerInGame(pre.getPlayer());
        if (ufaPlayer == null) {
            return;
        }
        spawn(map, pre, ufaPlayer);
    }

    private void spawn(UFAMap map, PlayerRespawnEvent pre, UFAPlayer ufaPlayer) {
        //new TpTeam(map).randomSpawn(ufaPlayer);
        //pre.setRespawnLocation(ufaPlayer.getTeam().getSpawnCenter()); // ufaPlayer.teleport() ?
//        ufaPlayer.getPlayer().teleport(ufaPlayer.getTeam().getSpawnCenter()); //yes
        //?
        if (!map.getSets().isRandomSpawn()) {
            pre.setRespawnLocation(ufaPlayer.getTeam().getSpawnCenter());
        } else {
            ArrayList<Team> teams = map.getTeams().getTeams();
            Location spawn = teams.get(new Random().nextInt(teams.size())).getSpawnCenter();
            pre.setRespawnLocation(spawn);
        }
        map.getUfaController().initPlayer(ufaPlayer);
    }
}
