package com.rbc.ufa.Command.Game.Player;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class GPlayer extends UFACommandToDo {

    public GPlayer(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "player";
        subCmds.add(new GPlayerInfo(this, modeManager, message));
    }
}
