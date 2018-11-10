package me.kingtux.holidayhunter.commands.api;

import java.lang.annotation.Annotation;

public class MyIkeaCommandRules implements IkeaCommandRules {
    private String command, description, format;
    private String[] aliases;
    private IkeaCommandRules ikeaCommandRules;

    public MyIkeaCommandRules(IkeaCommandRules ikeaCommandRules) {
        command = ikeaCommandRules.command();
        description = ikeaCommandRules.description();
        format = ikeaCommandRules.format();
        aliases = ikeaCommandRules.aliases();
        this.ikeaCommandRules = ikeaCommandRules;
    }

    public String command() {
        return command;
    }

    public String description() {
        return description;
    }

    public String format() {
        return format;
    }

    public String[] aliases() {
        return aliases;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return ikeaCommandRules.annotationType() ;
    }
}
