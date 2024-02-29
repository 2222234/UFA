package com.rbc.ufa.Command.Edit.Block;

import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.ufa.Command.UFACommandToDo;
import com.rbc.ufa.Tools.Message;

public class EBlock extends UFACommandToDo {

    public EBlock(UFACommandToDo superCmd, ModeManager modeManager, Message message) {
        super(superCmd, modeManager, message);
        name = "block";
        subCmds.add(new BlockCreate(this, modeManager, message));
        subCmds.add(new BlockRemove(this, modeManager, message));
        subCmds.add(new BlockInfo(this, modeManager, message));
        subCmds.add(new BlockScore(this, modeManager, message));
        subCmds.add(new BlockTime(this, modeManager, message));
        subCmds.add(new BlockLoc(this, modeManager, message));
    }
}
