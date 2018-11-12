package me.kingtux.holidayhunter;

import me.kingtux.holidayhunter.commands.HolidayCommand;
import me.kingtux.holidayhunter.commands.api.CommandManager;
import me.kingtux.holidayhunter.commands.api.IkeaCommandRules;
import me.kingtux.holidayhunter.commands.api.MyIkeaCommandRules;
import me.kingtux.holidayhunter.lang.LangFile;
import me.kingtux.holidayhunter.lang.LangLoader;
import me.kingtux.holidayhunter.listeners.PlayerListener;
import me.kingtux.simpleannotation.AnnotationWriter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class HolidayHunter extends JavaPlugin {
    private Map<String, String> heads = new HashMap<>();
    private Map<Player, HolidaySession> sessions = new HashMap<>();
    private HolidayManager holidayManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        getConfig().getConfigurationSection("heads").getKeys(false).forEach(s -> {
            heads.put(s.toLowerCase(), getConfig().getString("heads." + s));
        });
        saveResource("lang.yml", false);
        try {
            //Make Sure that static part loads!
            Class.forName("me.kingtux.holidayhunter.utils.NMSUtil");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        holidayManager = new HolidayManager(this);
        //Use IkeaCommand command library
        getLogger().info("IkeaEssentials Command and Lang API are implemented into this jar!");
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
        new PlaceHolder(this).register();
    }

    @Override
    public void onDisable() {
        try {
            holidayManager.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void newSession(Player sender) {
        sessions.put(sender, new HolidaySession(sender));
    }

    public void updateSession(Player player, HolidaySession session) {
        sessions.put(player, session);
    }

    public HolidayManager getHolidayManager() {
        return holidayManager;
    }
}
