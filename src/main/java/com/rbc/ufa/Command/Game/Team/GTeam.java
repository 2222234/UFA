package com.rbc.ufa.Command.Game.Team;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class GTeam extends UFACommandToDo {

    public GTeam(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "team";
        subCmds.add(new GTeamInfo(this, modeManager, message));
    }
}
