package com.rbc.ufa.Command.Edit.Area;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class EArea extends UFACommandToDo {

    public EArea(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "area";
        subCmds.add(new AreaCreate(this, modeManager, message));
        subCmds.add(new AreaRemove(this, modeManager, message));
        subCmds.add(new AreaInfo(this, modeManager, message));
        subCmds.add(new AreaTime(this, modeManager, message));
        subCmds.add(new AreaLoc(this, modeManager, message));
    }
}
