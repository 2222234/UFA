package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Kill extends RandomEvent {
    private int time = 1;

    public Kill(UFAMap map) {
        super(map);
        name = "kill";
    }


    private ItemStack getSword() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = sword.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 32767, true);
        String en = map.getMessage().getGame("events." + name + ".event", map);
        String displayName = en.substring(0, en.lastIndexOf("|"));
        String lore = en.substring(en.lastIndexOf("|") + 1);
        itemMeta.setDisplayName(displayName);
        List<String> lores = new ArrayList<>();
        lores.add(lore);
        itemMeta.setLore(lores);
        itemMeta.setLocalizedName("114514");
        sword.setItemMeta(itemMeta);
        return sword;
    }

    @Override
    public void eventStart() {
        super.eventStart();
        map.getUfaController().playSound(Sound.BLOCK_ENCHANTMENT_TABLE_USE);
    }

    @Override
    public void event(Object... objects) {
        if (objects[0] instanceof PlayerDropItemEvent) {
            PlayerDropItemEvent pdit = (PlayerDropItemEvent) objects[0];
            ItemMeta itemMeta = pdit.getItemDrop().getItemStack().getItemMeta();
            if (itemMeta != null && "114514".equals(itemMeta.getLocalizedName())) {
                pdit.setCancelled(true);
            }
            return;
        }
        if (objects[0] instanceof PlayerDeathEvent) {
            PlayerDeathEvent pde = (PlayerDeathEvent) objects[0];
            List<ItemStack> itemStacks = pde.getDrops();
            for (ItemStack itemStack : itemStacks) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null && "114514".equals(itemMeta.getLocalizedName())) {
                    itemStack.setAmount(0);
                }
            }
        }
    }

    @Override
    public void initPlayer(UFAPlayer ufaPlayer) {
        ufaPlayer.getPlayer().getInventory().addItem(getSword());
    }

    @Override
    public void timePerSecond(Object... objects) {
        super.timePerSecond(objects);
        if (time != 2) {
            time++;
            return;
        }
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            ItemStack itemStack = ufaPlayer.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null && "114514".equals(itemMeta.getLocalizedName())) {
                ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3 * 20, 1));
            }
        }
        time = 1;
    }


    @Override
    public void eventStop() {
        super.eventStop();
        map.getUfaController().playSound(Sound.BLOCK_ANVIL_DESTROY);
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            for (ItemStack itemStack : ufaPlayer.getPlayer().getInventory().getContents()) {
                if (itemStack == null) {
                    continue;
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null && "114514".equals(itemMeta.getLocalizedName())) {
                    itemStack.setAmount(0);
                }
            }
        }
    }
}
