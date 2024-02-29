package com.rbc.ufa.Events;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Game.UFAController;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.UFA;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class Damage implements Listener {
    private final ModeManager modeManager;

    public Damage(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @EventHandler
    public void d(EntityDamageByEntityEvent edbee) {
        ArrayList<ModeMap> maps = modeManager.getMapManager().getMaps(modeManager.getMode(UFA.class));
        if (!(edbee.getDamager() instanceof Player && edbee.getEntity() instanceof Player)) {
            return;
        }
        UFAMap DMap = UFAMap.getMapByPlayer(maps, (Player) edbee.getDamager());
        UFAMap EMap = UFAMap.getMapByPlayer(maps, (Player) edbee.getEntity());
        if (DMap == null || EMap == null) {
            return;
        }
        if (!DMap.equals(EMap)) {
            return;
        }
        if (DMap.getUfaController().getStage() != UFAController.GAMING) {
            return;
        }
        UFAPlayer damager = DMap.getUfaController().getPlayerInGame((Player) edbee.getDamager());
        UFAPlayer vic = DMap.getUfaController().getPlayerInGame((Player) edbee.getEntity());
        damage(DMap, edbee, damager, vic);
    }

    private void damage(UFAMap map, EntityDamageByEntityEvent edbee, UFAPlayer damager, UFAPlayer vic) {
        if (damager.getTeam().equals(vic.getTeam())) {
            damager.getPlayer().playSound(damager.getPlayer().getLocation(),
                    Sound.ENTITY_VILLAGER_HURT,
                    map.getConfig().getVolume(),
                    map.getConfig().getPitch());
            if (!map.getSets().isAccidentalInjury()) {
                edbee.setCancelled(false);
            }
        }
    }
}
