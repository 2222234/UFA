package com.rbc.ufa.Game.RandomEvents;

import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Only extends RandomEvent {
    private final HashMap<UFAPlayer, ItemStack[]> armor;

    public Only(UFAMap map) {
        super(map);
        name = "only";
        armor = new HashMap<>();
    }

    private ItemStack[] getArmor() {
        ItemStack[] itemStacks = new ItemStack[4];
        itemStacks[3] = new ItemStack(Material.LEATHER_HELMET);
        itemStacks[2] = new ItemStack(Material.LEATHER_CHESTPLATE);
        itemStacks[1] = new ItemStack(Material.LEATHER_LEGGINGS);
        itemStacks[0] = new ItemStack(Material.LEATHER_BOOTS);
        for (ItemStack itemStack : itemStacks) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            leatherArmorMeta.setColor(Color.BLACK);
            leatherArmorMeta.setUnbreakable(true);
            leatherArmorMeta.addEnchant(Enchantment.BINDING_CURSE, 32767, true);
            itemStack.setItemMeta(leatherArmorMeta);
        }
        return itemStacks;
    }

    @Override
    public void initPlayer(UFAPlayer ufaPlayer) {
        ufaPlayer.getPlayer().getInventory().setArmorContents(getArmor());
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, allTime * 20, 0));
        ufaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, allTime * 20, 0));
    }

    @Override
    public void eventStart() {
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            armor.put(ufaPlayer, ufaPlayer.getPlayer().getInventory().getArmorContents().clone());
        }
        super.eventStart();
    }

    @Override
    public void eventStop() {
        super.eventStop();
        for (UFAPlayer ufaPlayer : armor.keySet()) {
            ufaPlayer.getPlayer().getInventory().setArmorContents(armor.get(ufaPlayer));
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            ufaPlayer.getPlayer().removePotionEffect(PotionEffectType.BAD_OMEN);
        }
        armor.clear();
    }
}
