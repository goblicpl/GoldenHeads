package com.goblicpl.goldenheads.data;

import com.goblicpl.goldenheads.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ConfigHandler {

    private FileConfiguration configuration;
    private File configurationFile;

    public ConfigHandler() {
        loadConfig();
    }

    private void loadConfig() {
        configuration = new YamlConfiguration();

        configurationFile = new File(Main.getInstance().getDataFolder(), "config.yml");

        if(!configurationFile.exists()) {
            Main.getInstance().getDataFolder().mkdirs();
            copyFile(Main.getInstance().getResource("config.yml"), configurationFile);
        }

        try {
            configuration.load(configurationFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public File getConfigurationFile() {
        return configurationFile;
    }

    private void copyFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ConfigHandler getInstance() {
        return Main.getInstance().getConfigHandler();
    }
}
