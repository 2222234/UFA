package com.rbc.ufa.Command.Game.Control;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Event extends UFACommandToDo {
    private final MapManager mapManager;

    public Event(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "event";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        if (args.length == 1) {
            try {
                if (!map.getUfaController().getEvents().startEvent(args[0])) {
                    sender.sendMessage(
                            message.getGame("error.eventfail", map)
                                    .replace("{event}", String.valueOf(args[0])));
                }
            } catch (NumberFormatException nfe) {
                sender.sendMessage(getCmd().concat(subNames(sender)));
            }
        } else {
            sender.sendMessage(getCmd().concat(subNames(sender)));
        }
    }

    @Override
    public ArrayList<String> tab(CommandSender commandSender, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) commandSender);
        ArrayList<String> thisCmds = new ArrayList<>();
        for (String name : map.getUfaController().getEvents().getEventNames()) {
            if (name.contains(args[0])) {
                thisCmds.add(name);
            }
        }
        return thisCmds;
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " _";
        return sn;
    }
}
