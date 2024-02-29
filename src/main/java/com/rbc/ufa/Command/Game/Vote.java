package com.rbc.ufa.Command.Game;

import com.rbc.gunfight.Manager.MapManager;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote extends UFACommandToDo {
    private final MapManager mapManager;

    public Vote(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        mapManager = modeManager.getMapManager();
        name = "vote";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        UFAMap map = UFAMap.getMapByPlayer(mapManager.getMaps(), (Player) sender);
        if (map != null) {
            if (map.getUfaController().voteToJumpPrepare((Player) sender)) {
                return;
            }
        }
        sender.sendMessage(message.getGame("error.votefail", map));
    }
}
