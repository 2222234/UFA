package com.rbc.ufa.Tools;

import com.rbc.ufa.Map.UFAMap;
import org.bukkit.configuration.file.FileConfiguration;

public class Message {
    private final ReadConfig readConfig;
    private FileConfiguration message;
    private String pluginName;
    private String version;
    private String notFindMessage;
    private String notFind;
    private String subMessage;
    private String noPermission;
    private String reload;
    private boolean change = false;

    public Message(ReadConfig readConfig) {
        this.readConfig = readConfig;
        reload();
    }

    public void reload() {
        saveNF();
        message = readConfig.readMessage();
        pluginName = message.getString("name");
        version = message.getString("version");
        notFindMessage = message.getString("notfindmessage");
        notFind = message.getString("notfind");
        subMessage = message.getString("submessage");
        noPermission = message.getString("nopermission");
        reload = message.getString("reload");
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getVersion() {
        return version;
    }

    public String getMap(String string) {
        String message = gs("message.map." + string);
        return pluginName.concat(" ").concat(message);
    }

    public String getGame(String string, UFAMap map) {
        String message = gs("message.game." + string);
        if (map != null && map.getName() != null) {
            String name = gs("message.game.message.game").replace("{game}", map.getName());
            return message.replace("{game}", name);
        }
        return message.replace("{game}", "");
    }

    public String gs(String path) {
        String message = this.message.getString(path);
        if (message != null) {
            return message;
        } else {
            this.message.set(path, notFindMessage);
            return noPermission;
        }
    }

    public String getNotFind() {
        return notFind;
    }

    public String getSubMessage() {
        return subMessage;
    }

    public String getNoPermission() {
        return noPermission;
    }

    public String getReload() {
        return reload;
    }

    public void saveNF() {
        if (message != null && change) {
            readConfig.saveNF(message);
        }
    }
}
