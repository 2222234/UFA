package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class See extends RandomEvent {
    public See(UFAMap map) {
        super(map);
        name = "see";
    }

    @Override
    public void timePerSecond(Object... objects) {
        super.timePerSecond(objects);
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            if (checkHeadHasBlock(ufaPlayer)) {
                ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2 * 20, 1));
                ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2 * 20, 0));
            }
        }
    }

    private boolean checkHeadHasBlock(UFAPlayer ufaPlayer) {
        Location location = ufaPlayer.getPlayer().getLocation().clone();
        for (int y = ufaPlayer.getPlayer().getLocation().getBlockY(); y <= map.getGameWorld().getMaxHeight(); y++) {
            location.setY(y);
            if (!location.getBlock().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void eventStop() {
        super.eventStop();
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
        }
    }
}
