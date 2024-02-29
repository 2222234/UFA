package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Sand extends RandomEvent {
    public Sand(UFAMap map) {
        super(map);
        name = "sand";
    }

    @Override
    public void eventStart() {
        super.eventStart();
        map.getUfaController().playSound(Sound.BLOCK_SAND_FALL);
    }


    @Override
    public void initPlayer(UFAPlayer ufaPlayer) {
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, allTime * 20, 1));
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, allTime * 20, 3));
    }

    @Override
    public void timePerSecond(Object... objects) {
        super.timePerSecond(objects);
        map.getUfaController().playSound(Sound.BLOCK_SAND_BREAK);
    }

    @Override
    public void eventStop() {
        super.eventStop();
        map.getUfaController().playSound(Sound.BLOCK_SAND_PLACE);
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }
}
