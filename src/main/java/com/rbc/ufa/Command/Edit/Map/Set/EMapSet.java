package com.rbc.ufa.Command.Edit.Map.Set;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class EMapSet extends UFACommandToDo {

    public EMapSet(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "set";
        subCmds.add(new KillScore(this, modeManager, message));
        subCmds.add(new AccidentalInjury(this, modeManager, message));
        subCmds.add(new KillMatePunish(this, modeManager, message));
        subCmds.add(new MaxBlood(this, modeManager, message));
        subCmds.add(new RandomEvents(this, modeManager, message));
        subCmds.add(new RandomTeam(this, modeManager, message));
        subCmds.add(new RandomSpawn(this, modeManager, message));
        subCmds.add(new SetInfo(this, modeManager, message));
    }
}
