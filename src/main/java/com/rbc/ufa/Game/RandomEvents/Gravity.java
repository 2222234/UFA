package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Gravity extends RandomEvent {
    public Gravity(UFAMap map) {
        super(map);
        name = "gravity";
    }

    @Override
    public void eventStart() {
        super.eventStart();
        map.getUfaController().playSound(Sound.ITEM_FIRECHARGE_USE);
    }

    @Override
    public void initPlayer(UFAPlayer ufaPlayer) {
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, allTime * 20, 6));
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, allTime * 20, 6));
    }

    @Override
    public void eventStop() {
        super.eventStop();
        map.getUfaController().playSound(Sound.BLOCK_FIRE_EXTINGUISH);
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.JUMP);
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.SLOW_FALLING);
        }
    }
}
