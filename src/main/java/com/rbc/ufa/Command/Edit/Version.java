package com.rbc.ufa.Command.Edit;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;
import com.rbc.ufa.UFA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Version extends UFACommandToDo {
    public Version(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "version";
    }

    @Override
    public void toDo(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("gunfight.main")) {
            sender.sendMessage(
                    message.getPluginName()
                            .concat(" ")
                            .concat(modeManager.getMode(UFA.class).getModeName())
                            .concat(" ")
                            .concat(modeManager.getMode(UFA.class).getModeVersion())
                            .concat(" ")
                            .concat(modeManager.getMode(UFA.class).getModeAuthor()));
            ;
        } else {
            sender.sendMessage(message.getNoPermission());
        }
    }
}
