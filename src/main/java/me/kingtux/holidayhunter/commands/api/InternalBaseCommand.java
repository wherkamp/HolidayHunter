package me.kingtux.holidayhunter.commands.api;


import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class InternalBaseCommand {
    private final BaseCommand annotation;
    private final Method methodToInvoke, tabCompleter;
    private final IkeaCommand commandObject;

    protected InternalBaseCommand(BaseCommand annotation, Method methodToInvoke, Method tabCompleter, IkeaCommand commandObject) {
        this.annotation = annotation;
        this.methodToInvoke = methodToInvoke;
        this.tabCompleter = tabCompleter;
        this.commandObject = commandObject;
    }

    public void invoke(String[] args, CommandSender sender, String commandUsed) {
        try {
            methodToInvoke.invoke(commandObject, CommandUtils.getParameters(args, methodToInvoke, commandObject,sender,commandUsed));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }catch (Exception e){
            System.out.println("Failed to pass command to "+ methodToInvoke.getName());
            e.printStackTrace();
        }
    }

    public List<String> invokeTab(String[] args, CommandSender sender, String commandUsed) {
        try {
            return (List<String>) tabCompleter.invoke(commandObject, CommandUtils.getParameters(args, tabCompleter, commandObject,sender,commandUsed));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public BaseCommand getAnnotation() {
        return annotation;
    }

    public Method getMethodToInvoke() {
        return methodToInvoke;
    }

    public Method getTabCompleter() {
        return tabCompleter;
    }

    public IkeaCommand getCommandObject() {
        return commandObject;
    }
}
