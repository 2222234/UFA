package com.rbc.ufa.Command.Edit.Map.Set;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomSpawn extends UFACommandToDo {
    private final MapManager mapManager;

    public RandomSpawn(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "randomspawn";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        if (args.length == 1) {
            map.getSets().setRandomSpawn(Boolean.parseBoolean(args[0]));
            sender.sendMessage(map.getSets().info());
        } else {
            sender.sendMessage(getCmd().concat(subNames(sender)));
        }
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " true/false";
        return sn;
    }
}