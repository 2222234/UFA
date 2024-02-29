package com.rbc.ufa.Command.Game.Player;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GPlayerInfo extends UFACommandToDo {
    private final MapManager mapManager;

    public GPlayerInfo(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "info";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        switch (args.length) {
            case 0:
                sender.sendMessage(message.getGame("info.player_tip", map));
                for (String info : map.getUfaController().playersInfo()) {
                    sender.sendMessage(info);
                }
                break;
            case 1:
                try {
                    sender.sendMessage(message.getGame("info.player_tip", map));
                    sender.sendMessage(map.getUfaController().playerInfoName(args[0]));
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
        String sn = " ()";
        return sn;
    }

    @Override
    public ArrayList<String> tab(CommandSender sender, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        ArrayList<String> thisCmds = new ArrayList<>();
        for (String n : map.getUfaController().getPlayerNames()) {
            if (n.contains(args[0])) {
                thisCmds.add(n);
            }
        }
        return thisCmds;
    }
}
