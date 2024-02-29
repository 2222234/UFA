package com.rbc.ufa.Command.Edit.Team;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class ETeam extends UFACommandToDo {

    public ETeam(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "team";
        subCmds.add(new TeamCreate(this, modeManager, message));
        subCmds.add(new TeamRemove(this, modeManager, message));
        subCmds.add(new TeamInfo(this, modeManager, message));
        subCmds.add(new TeamName(this, modeManager, message));
        subCmds.add(new TeamPrepare(this, modeManager, message));
        subCmds.add(new TeamSpawn(this, modeManager, message));
        subCmds.add(new TeamBag(this, modeManager, message));
    }
}
