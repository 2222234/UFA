package com.rbc.ufa.Command.Game.Block;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameBlockInfo extends UFACommandToDo {
    private final MapManager mapManager;

    public GameBlockInfo(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "info";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        switch (args.length) {
            case 0:
                sender.sendMessage(message.getGame("info.map_block_tip", map));
                for (String info : map.getBlocks().info()) {
                    sender.sendMessage(info);
                }
                break;
            case 1:
                try {
                    int num = Integer.parseInt(args[0]);
                    sender.sendMessage(message.getGame("info.map_block_tip", map));
                    sender.sendMessage(map.getBlocks().infoNum(num));
                } catch (NumberFormatException nfe) {
                    sender.sendMessage(getCmd().concat(subNames(sender)));
                }
                break;
            default:
                sender.sendMessage(getCmd().concat(subNames(sender)));
        }
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " _/()";
        return sn;
    }
}

