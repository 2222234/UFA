package com.rbc.ufa.Command.Game.Control;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class GControl extends UFACommandToDo {

    public GControl(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "control";
        subCmds.add(new Jump(this, modeManager, message));
        subCmds.add(new Info(this, modeManager, message));
        subCmds.add(new Event(this, modeManager, message));
    }
}
