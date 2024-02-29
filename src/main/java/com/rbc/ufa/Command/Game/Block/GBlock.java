package com.rbc.ufa.Command.Game.Block;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class GBlock extends UFACommandToDo {

    public GBlock(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "block";
        subCmds.add(new GameBlockInfo(this, modeManager, message));
        subCmds.add(new RepBlockNow(this, modeManager, message));
    }
}
