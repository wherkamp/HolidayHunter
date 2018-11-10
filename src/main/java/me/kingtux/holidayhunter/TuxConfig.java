package me.kingtux.holidayhunter;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TuxConfig extends YamlConfiguration {

    private File configFile;

    public TuxConfig(File file) throws IOException, InvalidConfigurationException {
        configFile = file;
        if (!configFile.exists()) {
            configFile.createNewFile();

        }
        if (configFile.isDirectory()) {
            throw new IllegalArgumentException("It cannot be a directory");
        }
        load();
    }

    public TuxConfig(String path) throws InvalidConfigurationException, IOException {
        configFile = new File(path);
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
        if (configFile.isDirectory()) {
            throw new IllegalArgumentException("It cannot be a directory");
        }
        load();


    }

    public void save() throws IOException {
        save(configFile);
    }

    public void load() throws InvalidConfigurationException, IOException {
        options().copyDefaults(true);
        options().copyHeader(true);
        load(configFile);
    }

    public File getConfigFile() {
        return configFile;
    }
}