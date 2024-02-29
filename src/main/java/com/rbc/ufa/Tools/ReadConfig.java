package com.rbc.ufa.Tools;

import com.rbc.gunfight.Mode.ModeMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ReadConfig {
    private final ModeMain modeMain;

    public ReadConfig(ModeMain modeMain) {
        this.modeMain = modeMain;
    }

    public FileConfiguration readConfig() {
        File config = getFile("config.yml");
        return YamlConfiguration.loadConfiguration(config);
    }

    public FileConfiguration readMessage() {
        File message = getFile("message.yml");
        return YamlConfiguration.loadConfiguration(message);
    }

    public void saveNF(FileConfiguration nf) {
        File message = getFile("message.yml");
        try {
            nf.save(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String path) {
        File file = new File(modeMain.getFolder(), path);
        if (!file.exists()) {
            try {
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
                Files.copy(inputStream, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
