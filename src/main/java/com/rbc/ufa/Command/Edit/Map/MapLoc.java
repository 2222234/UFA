package com.rbc.ufa.Command.Edit.Map;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapLoc extends UFACommandToDo {
    private final MapManager mapManager;

    public MapLoc(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "location";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            UFAMap map = (UFAMap) mapManager.getMap((Player) sender);
            map.setGameLoc1(map.getSecLoc1());
            map.setGameLoc2(map.getSecLoc2());
            sender.sendMessage(map.editInfo());
        } else {
            sender.sendMessage(getCmd().concat(subNames(sender)));
        }
    }

    @Override
    public String subNames(CommandSender sender) {
        String sn = " ";
        return sn;
    }
}
