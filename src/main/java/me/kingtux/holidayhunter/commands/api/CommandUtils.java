package me.kingtux.holidayhunter.commands.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CommandUtils {
    public static CommandMap getCommandMap() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            return (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object[] getParameters(String[] args, Method command, IkeaCommand ikeaCommand, CommandSender commandSender, String commandUsed) {
        Class<?>[] parameterTypes = command.getParameterTypes();
        final Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (type == String[].class) {
                parameters[i] = args;
            } else if (type == String.class) {
                parameters[i] = commandUsed;
            } else if (type.isInstance(commandSender)) {
                if (type == Player.class && !(commandSender instanceof Player)) {
                    commandSender.sendMessage(ikeaCommand.mustBeAPlayer());
                } else {
                    parameters[i] = commandSender;
                }
            }
        }
        return parameters;
    }
}
