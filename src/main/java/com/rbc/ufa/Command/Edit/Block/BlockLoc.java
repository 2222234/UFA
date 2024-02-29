package com.rbc.ufa.Command.Edit.Block;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlockLoc extends UFACommandToDo {
    private final MapManager mapManager;

    public BlockLoc(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "location";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        if (args.length == 1) {
            try {
                int num = Integer.parseInt(args[0]);
                if (map.getBlocks().setLoc(num, map.getSecLoc1())) {
                    sender.sendMessage(map.getBlocks().infoNum(num));
                } else {
                    sender.sendMessage(getCmd().concat(subNames(sender)));
                }
            } catch (NumberFormatException nfe) {
                sender.sendMessage(getCmd().concat(subNames(sender)));
            }
        } else {
            sender.sendMessage(getCmd().concat(subNames(sender)));
        }
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " ()";
        return sn;
    }
}

