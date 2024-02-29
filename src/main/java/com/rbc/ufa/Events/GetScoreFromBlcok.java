package com.rbc.ufa.Events;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.RandomEvents.Events;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.Block.Block;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import com.rbc.ufa.UFA;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class GetScoreFromBlcok implements Listener {
    private final ModeManager modeManager;

    public GetScoreFromBlcok(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @EventHandler
    public void gsfk(BlockBreakEvent bbe) {
        ArrayList<ModeMap> maps = modeManager.getMapManager().getMaps(modeManager.getMode(UFA.class));
        UFAMap map = UFAMap.getMapByPlayer(maps, bbe.getPlayer());
        if (map == null) {
            return;
        }
        if (map.getUfaController().getStage() != UFAController.GAMING) {
            return;
        }
        if (!Tool.check(map.getGameLoc1(), map.getGameLoc2(), bbe.getBlock().getLocation())) {
            return;
        }
        UFAPlayer player = map.getUfaController().getPlayerInGame(bbe.getPlayer());
        if (player == null) {
            return;
        }
        block(map, bbe, player);
    }

    private void block(UFAMap map, BlockBreakEvent bbe, UFAPlayer player) {
        for (Block block : map.getBlocks().getBlocks()) {
            if (block.getLocation().equals(bbe.getBlock().getLocation())) {
                player.addScore(block.getScore());
                map.getUfaController().sendMessage(
                        map.getMessage().getGame("message.game_getscore_block", map)
                                .replace("{team}", player.getTeam().getName())
                                .replace("{player}", player.getPlayer().getName())
                                .replace("{score}", String.valueOf(block.getScore())));
                player.getPlayer().playSound(bbe.getBlock().getLocation(),
                        Sound.ENTITY_PLAYER_LEVELUP, map.getConfig().getVolume(),
                        map.getConfig().getPitch());
                block.restart();
                bbe.setDropItems(false);
                map.getUfaController().getEvents().event(Events.LUCK, player, block.getScore());
                return;
            }
        }
        if (bbe.getBlock().getType().equals(Material.GLASS)) {
            bbe.setDropItems(false);
            return;
        }
        bbe.setCancelled(true);
    }
}
