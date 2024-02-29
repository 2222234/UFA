package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Speed extends RandomEvent {
    public Speed(UFAMap map) {
        super(map);
        name = "speed";
    }

    @Override
    public void eventStart() {
        super.eventStart();
        map.getUfaController().playSound(Sound.BLOCK_BEACON_ACTIVATE);
    }

    @Override
    public void initPlayer(UFAPlayer ufaPlayer) {
        ufaPlayer.setMaxBlood(20.0d);
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, allTime * 20, 2));
    }

    @Override
    public void eventStop() {
        super.eventStop();
        map.getUfaController().playSound(Sound.BLOCK_BEACON_DEACTIVATE);
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            ufaPlayer.setMaxBlood(map.getSets().getMaxBlood());
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
        }
    }
}
