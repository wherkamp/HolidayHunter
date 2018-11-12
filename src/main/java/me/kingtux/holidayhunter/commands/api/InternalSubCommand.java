package me.kingtux.holidayhunter.commands.api;


import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InternalSubCommand {
    private final SubCommand annotation;
    private final TabCompleter tabCompleter;
    private final Method methodToInvoke, tabCompleterMethod;
    private final IkeaCommand commandObject;

    protected InternalSubCommand(SubCommand annotation, TabCompleter tabCompleter, Method methodToInvoke, Method tabCompleterMethod, IkeaCommand commandObject) {
        this.annotation = annotation;
        this.tabCompleter = tabCompleter;
        this.methodToInvoke = methodToInvoke;
        this.tabCompleterMethod = tabCompleterMethod;
        this.commandObject = commandObject;
    }

    public void invoke(String[] args, CommandSender sender, String commandUsed) {
        try {
            Object[] stuff = CommandUtils.getParameters(args, methodToInvoke, commandObject, sender, commandUsed);
            if (stuff == null) {
                return;
            }
            methodToInvoke.invoke(commandObject, stuff);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public List<String> invokeTab(String[] args, CommandSender sender, String commandUsed) {
        try {
            Object[] stuff = CommandUtils.getParameters(args, methodToInvoke, commandObject, sender, commandUsed);
            if (stuff == null) {
                return Collections.emptyList();
            }
            return (List<String>) tabCompleterMethod.invoke(commandObject, stuff);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SubCommand getAnnotation() {
        return annotation;
    }

    public Method getMethodToInvoke() {
        return methodToInvoke;
    }

    public IkeaCommand getCommandObject() {
        return commandObject;
    }

    public TabCompleter getTabCompleter() {
        return tabCompleter;
    }

    public Method getTabCompleterMethod() {
        return tabCompleterMethod;
    }
}
