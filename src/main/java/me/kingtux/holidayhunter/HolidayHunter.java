package me.kingtux.holidayhunter;

import me.kingtux.holidayhunter.commands.api.CommandManager;
import me.kingtux.holidayhunter.commands.HolidayCommand;
import me.kingtux.holidayhunter.commands.api.IkeaCommandRules;
import me.kingtux.holidayhunter.commands.api.MyIkeaCommandRules;
import me.kingtux.holidayhunter.lang.LangFile;
import me.kingtux.holidayhunter.lang.LangLoader;
import me.kingtux.holidayhunter.listeners.PlayerListener;
import me.kingtux.simpleannotation.AnnotationWriter;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class HolidayHunter extends JavaPlugin {
    private Map<String, String> heads = new HashMap<>();
    private Map<Player, HolidaySession> sessions = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();

        getConfig().getConfigurationSection("heads").getKeys(false).forEach(s -> {
            heads.put(s.toLowerCase(), getConfig().getString("heads." + s));
        });

        try {
            //Make Sure that static part loads!
            Class.forName("me.kingtux.holidayhunter.utils.NMSUtil");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //Use IkeaCommand command library
        CommandManager commandManager = new CommandManager(this);
        MyIkeaCommandRules ikeaCommandRules =
                new MyIkeaCommandRules(HolidayCommand.class.getAnnotation(IkeaCommandRules.class));
        ikeaCommandRules.setCommand(getConfig().getString("command.command"));
        ikeaCommandRules.setFormat(getConfig().getString("command.format"));
        ikeaCommandRules.setDescription(getConfig().getString("command.description"));
        ikeaCommandRules.setAliases(getConfig().getStringList("command.aliases").toArray(new String[0]));
        AnnotationWriter.writeToAnnotation(HolidayCommand.class, IkeaCommandRules.class, ikeaCommandRules);

        commandManager.register(new HolidayCommand(this));
        //Use IkeaLang Library
        LangLoader.loadLang(new File(getDataFolder(), "lang.yml"), LangFile.class, false);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public static String fileToString(File file) throws IOException {
        if (file.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            return getContentFromBufferedReader(bufferedReader);
        }

        return null;

    }

    public static String getContentFromBufferedReader(BufferedReader bufferedReader)
            throws IOException {

        String fullContent = "";
        String singleLine = "";
        while ((singleLine = bufferedReader.readLine()) != null) {
            fullContent += singleLine + "\n";
        }
        return fullContent;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Map<String, String> getHeads() {
        return heads;
    }

    public void remove(Player player) {
        sessions.remove(player);
    }

    public void clear() {
        sessions.clear();
    }

    public Map<Player, HolidaySession> getSessions() {
        return sessions;
    }
}
