package com.rbc.ufa.Command.Game.Area;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class GArea extends UFACommandToDo {

    public GArea(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "area";
        subCmds.add(new GameAreaInfo(this, modeManager, message));
        subCmds.add(new RepAreaNow(this, modeManager, message));
    }
}
