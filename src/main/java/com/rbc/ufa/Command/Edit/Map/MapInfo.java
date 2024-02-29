package com.rbc.ufa.Command.Edit.Map;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapInfo extends UFACommandToDo {
    private final MapManager mapManager;

    public MapInfo(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "info";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
        if (args.length == 0) {
            sender.sendMessage(message.getMap("info.tip"));
            sender.sendMessage(map.editInfo());
        }
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " ";
        return sn;
    }
}
