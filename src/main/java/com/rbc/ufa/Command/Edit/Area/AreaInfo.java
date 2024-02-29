package com.rbc.ufa.Command.Edit.Area;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AreaInfo extends UFACommandToDo {
    private final MapManager mapManager;

    public AreaInfo(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "info";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        switch (args.length) {
            case 0:
                sender.sendMessage(message.getMap("info.area_tip"));
                for (String info : map.getAreas().info()) {
                    sender.sendMessage(info);
                }
                break;
            case 1:
                if (args[0].equals("loc")) {
                    sender.sendMessage(message.getMap("info.area_tip"));
                    for (String info : map.getAreas().getLoc(map.getSecLoc1())) {
                        sender.sendMessage(info);
                    }
                } else {
                    try {
                        int num = Integer.parseInt(args[0]);
                        sender.sendMessage(message.getMap("info.area_tip"));
                        sender.sendMessage(map.getAreas().infoNum(num));
                    } catch (NumberFormatException nfe) {
                        sender.sendMessage(getCmd().concat(subNames(sender)));
                    }
                }
                break;
            default:
                sender.sendMessage(getCmd().concat(subNames(sender)));
        }
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " _/()/loc";
        return sn;
    }

    @Override
    public ArrayList<String> tab(CommandSender sender, String[] args) {
        ArrayList<String> thisCmds = new ArrayList<>();
        if ("loc".contains(args[0])) {
            thisCmds.add("loc");
        }
        return thisCmds;
    }
}
