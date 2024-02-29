package com.rbc.ufa.Command.Game.Block;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RepBlockNow extends UFACommandToDo {
    private final MapManager mapManager;

    public RepBlockNow(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "rep";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        if (args.length == 2) {
            try {
                int num = Integer.parseInt(args[0]);
                boolean sendMessage = Boolean.parseBoolean(args[1]);
                map.getBlocks().rep(num, sendMessage);
                sender.sendMessage(message.getGame("rep.map_block", map)
                        .replace("{num}", String.valueOf(num)));
            } catch (NumberFormatException nfe) {
                sender.sendMessage(getCmd().concat(subNames(sender)));
            }
        } else {
            sender.sendMessage(getCmd().concat(subNames(sender)));
        }
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " () true/false";
        return sn;
    }
}

