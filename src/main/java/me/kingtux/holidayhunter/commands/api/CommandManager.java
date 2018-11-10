package me.kingtux.holidayhunter.commands.api;

import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

public class CommandManager {
    private Plugin plugin;

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
        if (CommandUtils.getCommandMap() == null) {
            plugin.getLogger().severe("Sorry, your server does not support this!");
        }
    }

    public void register(IkeaCommand ikeaCommand) {
        CommandMap commandMap = CommandUtils.getCommandMap();
        commandMap.register(plugin.getName().toLowerCase(), new InternalCommand(ikeaCommand, ikeaCommand.getClass().getAnnotation(IkeaCommandRules.class)));
    }

    public InternalCommand getInternalCommand(String command) {
        return (InternalCommand) CommandUtils.getCommandMap().getCommand(command);
    }
}
