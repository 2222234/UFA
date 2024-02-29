package com.rbc.ufa.Command.Edit.Map.Set;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaxBlood extends UFACommandToDo {
    private final MapManager mapManager;

    public MaxBlood(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "maxblood";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        try {
            double punish = Double.parseDouble(args[0]);
            map.getSets().setMaxBlood(punish);
            sender.sendMessage(map.getSets().info());
        } catch (NumberFormatException nfe) {
            sender.sendMessage(getCmd().concat(subNames(sender)));
        }
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " ()";
        return sn;
    }
}
