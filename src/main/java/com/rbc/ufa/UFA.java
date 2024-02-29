package com.rbc.ufa;

import com.rbc.gunfight.Command.CommandToDo;
import com.rbc.gunfight.Manager.ModeManager;
import com.rbc.gunfight.Mode.ModeController;
import com.rbc.gunfight.Mode.ModeMain;
import com.rbc.gunfight.Mode.ModeMap;
import com.rbc.ufa.Command.Edit.Area.EArea;
import com.rbc.ufa.Command.Edit.Block.EBlock;
import com.rbc.ufa.Command.Edit.Map.EMap;
import com.rbc.ufa.Command.Edit.Team.ETeam;
import com.rbc.ufa.Command.Edit.Version;
import com.rbc.ufa.Command.Game.Area.GArea;
import com.rbc.ufa.Command.Game.Block.GBlock;
import com.rbc.ufa.Command.Game.Control.GControl;
import com.rbc.ufa.Command.Game.Player.GPlayer;
import com.rbc.ufa.Command.Game.ReJoin;
import com.rbc.ufa.Command.Game.Team.GTeam;
import com.rbc.ufa.Command.Game.Vote;
import com.rbc.ufa.Events.*;
import com.rbc.ufa.Game.Config;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Message;
import com.rbc.ufa.Tools.ReadConfig;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class UFA extends ModeMain {
    private ArrayList<CommandToDo> editCmds;
    private ArrayList<CommandToDo> gameCmds;
    private ArrayList<CommandToDo> controlCmds;
    private ArrayList<CommandToDo> mainCmds;
    private ReadConfig readConfig;
    private Config config;
    private Message message;
    private ModeManager modeManager;

    @Override
    public void onEnable() {
        modeManager.getEventsManager().reload(getModeName());
    }

    @Override
    public void onDisable() {
        message.saveNF();
    }

    @Override
    public void reg() {
        editCmds = new ArrayList<>();
        gameCmds = new ArrayList<>();
        controlCmds = new ArrayList<>();
        mainCmds = new ArrayList<>();
        readConfig = new ReadConfig(this);
        config = new Config(readConfig);
        message = new Message(readConfig);
        modeManager = gunFight.getModeManager();
        editCmds.add(new EBlock(null, modeManager, message));
        editCmds.add(new EArea(null, modeManager, message));
        editCmds.add(new ETeam(null, modeManager, message));
        editCmds.add(new EMap(null, modeManager, message));
        mainCmds.add(new Version(null, modeManager, message));
        gameCmds.add(new ReJoin(null, modeManager, message));
        gameCmds.add(new Vote(null, modeManager, message));
        controlCmds.add(new GBlock(null, modeManager, message));
        controlCmds.add(new GArea(null, modeManager, message));
        controlCmds.add(new GTeam(null, modeManager, message));
        controlCmds.add(new GPlayer(null, modeManager, message));
        controlCmds.add(new GControl(null, modeManager, message));
    }

    @Override
    public ArrayList<CommandToDo> getEditCmds() {
        return editCmds;
    }

    @Override
    public ArrayList<CommandToDo> getGameCmds() {
        return gameCmds;
    }

    @Override
    public ArrayList<CommandToDo> getControlCmds() {
        return controlCmds;
    }

    @Override
    public ArrayList<CommandToDo> getMainCmds() {
        return mainCmds;
    }

    @Override
    public void regEvents(PluginManager pluginManager) {
        pluginManager.registerEvents(new Damage(modeManager), this);
        pluginManager.registerEvents(new DropKill(modeManager), this);
        pluginManager.registerEvents(new GetScoreFromBlcok(modeManager), this);
        pluginManager.registerEvents(new GetScoreFromKill(modeManager), this);
        pluginManager.registerEvents(new InOutGameArea(modeManager), this);
        pluginManager.registerEvents(new PlayerJoin(modeManager), this);
        pluginManager.registerEvents(new PlayerQuit(modeManager), this);
        pluginManager.registerEvents(new ReSpawn(modeManager), this);
    }

    @Override
    public boolean reload() {
        readConfig.readConfig();
        readConfig.readMessage();
        message.reload();
        return true;
    }

    @Override
    public ModeController getModeController() {
        return null;
    }

    @Override
    public ModeMap createMap(String s) {
        File file = new File(modeManager.getMapManager().getMapFolder(), s + ".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UFAMap(this, file, s, config, message);
    }

    @Override
    public ModeMap loadMap(File file) {
        UFAMap map = new UFAMap(this, file, null, config, message);
        map.load();
        return map;
    }
}
