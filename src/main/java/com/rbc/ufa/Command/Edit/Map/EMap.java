package com.rbc.ufa.Command.Edit.Map;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.Edit.Map.Set.EMapSet;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class EMap extends UFACommandToDo {

    public EMap(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "map";
        subCmds.add(new MapInfo(this, modeManager, message));
        subCmds.add(new MapLoc(this, modeManager, message));
        subCmds.add(new MapName(this, modeManager, message));
        subCmds.add(new MapWorld(this, modeManager, message));
        subCmds.add(new EMapSet(this, modeManager, message));
    }
}
