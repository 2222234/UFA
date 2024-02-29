package com.rbc.ufa.Tools;

import com.earth2me.essentials.api.Economy;
import com.rbc.ufa.Map.UFAMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class Tool {
    public static boolean check(Location loc1, Location loc2, Location c) {
        if (loc1 == null || loc2 == null || c == null) {
            return false;
        }
        boolean world = Objects.equals(loc1.getWorld(), loc2.getWorld()) && Objects.equals(loc1.getWorld(), c.getWorld()) && Objects.equals(loc2.getWorld(), c.getWorld());
        if (world) {
            boolean x1 = loc1.getBlockX() <= c.getBlockX() && c.getBlockX() <= loc2.getBlockX();
            boolean x2 = loc1.getBlockX() >= c.getBlockX() && c.getBlockX() >= loc2.getBlockX();
            boolean y1 = loc1.getBlockY() <= c.getBlockY() && c.getBlockY() <= loc2.getBlockY();
            boolean y2 = loc1.getBlockY() >= c.getBlockY() && c.getBlockY() >= loc2.getBlockY();
            boolean z1 = loc1.getBlockZ() <= c.getBlockZ() && c.getBlockZ() <= loc2.getBlockZ();
            boolean z2 = loc1.getBlockZ() >= c.getBlockZ() && c.getBlockZ() >= loc2.getBlockZ();
            return (x1 || x2) && (y1 || y2) && (z1 || z2);
        }
        return false;
    }

    public static boolean checkWorld(Location loc, World world) {
        if (loc == null || world == null) {
            return false;
        }
        return Objects.equals(loc.getWorld(), world);
    }


    public static int getSubscript(Iterable iterable, Object obj) {
        int i = 0;
        for (Object o : iterable) {
            if (o.equals(obj)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static ArrayList<Integer> getSubscriptList(Iterable iterable, Object obj) {
        int i = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (Object o : iterable) {
            if (o.equals(obj)) {
                list.add(i);
            }
            i++;
        }
        return list;
    }

    public static void reward(Player player, BigDecimal bigDecimal, UFAMap ufaMap) {
        try {
            Economy.add(player.getUniqueId(), bigDecimal);
        } catch (Exception e) {
            player.sendMessage(ufaMap.getMessage().getGame("error.reward", ufaMap));
        }
    }
}
