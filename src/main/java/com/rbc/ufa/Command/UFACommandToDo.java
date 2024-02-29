package com.rbc.ufa.Command;

import com.rbc.gunfight.Command.CommandToDo;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Tools.Message;

public class UFACommandToDo extends CommandToDo {
    protected Message message;

    public UFACommandToDo(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager);
        this.message = message;
    }

}
