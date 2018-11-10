package me.kingtux.holidayhunter.commands.api;


import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InternalOtherCommand {
    private  final OtherCommand annotation;
    private final Method methodToInvoke;
    private final IkeaCommand commandObject;

    protected InternalOtherCommand(OtherCommand annotation, Method methodToInvoke, IkeaCommand commandObject) {
        this.annotation = annotation;
        this.methodToInvoke = methodToInvoke;
        this.commandObject = commandObject;
    }

    public void invoke(String[] args, CommandSender sender, String commandUsed) {
        try {
            methodToInvoke.invoke(commandObject, CommandUtils.getParameters(args, methodToInvoke, commandObject,sender,commandUsed));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public OtherCommand getAnnotation() {
        return annotation;
    }

    public Method getMethodToInvoke() {
        return methodToInvoke;
    }

    public IkeaCommand getCommandObject() {
        return commandObject;
    }
}
