package me.kingtux.holidayhunter.lang;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum LangFile {
    //Common Entries
    @ConfigEntry(path = "common.must-be-a-player")
    MUST_BE_A_PLAYER("Sorry, you must be a player"),
    @ConfigEntry(path = "common.lack-of-permission")
    LACK_OF_PERMISSION("Sorry you lack permission to do that!"),
    @ConfigEntry(path = "head-missing")
    NOT_A_HEAD("Head Not Found"),
    @ConfigEntry(path = "already-have-a-session")
    ALREADY_IN_SESSIONS("You character is already found in the sessions"),
    @ConfigEntry(path = "please-run-hh-create-first")
    MISSING_SESSION("A session does not exist for you"),
    @ConfigEntry(path = "item.name")
    ITEM_NAME("Place Head to make a holiday head"),
    @ConfigEntry(path = "item.lore")
    ITEM_LORE("Place Head on grab"),
    @ConfigEntry(path = "destroy-skull")
    DESTROYED_SKULL("The head has been destroyed"),
    @ConfigEntry(path = "command.set-name")
    SET_NAME("PLease the the na,e using /hh name {name}"),

    @ConfigEntry(path = "command.set-command")
    SET_COMMANDS("Please provide the commands"),

    @ConfigEntry(path = "command.set-message")
    SET_MESSAGES("Please provide the messages"),

    @ConfigEntry(path = "command.finish")
    FINISH("Run /hh finish to finish the command"),

    @ConfigEntry(path = "command.here-is-your-head")
    HERE_IS_YOUR_HEAD("Here is your head!"),

    @ConfigEntry(path = "hunt-announcements.finished-the-hunt")
    COMPLETED_THE_HUNT("%player_displayname% has joined the hunt"),

    @ConfigEntry(path = "hunt-announcements.started-the-hunt")
    STARTED_THE_HUNT("%player_displayname% has finished the hunt"),
    @ConfigEntry(path = "already-collected")
    ALREADY_COLLECTED("You already collected that one"),
    @ConfigEntry(path = "placed-head")
    PLACED_HEAD("You have placed a head");
    @ConfigValue
    private String value;

    LangFile(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getString() {
        return (String) getValue();
    }

    public List<String> getStringList() {
        return Arrays.asList(getValue().split("\n"));
    }

    public List<String> colorStringList() {
        List<String> strings = new ArrayList<>();
        for (String s : getStringList()) {
            strings.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return strings;
    }

    public String getColorValue() {
        return ChatColor.translateAlternateColorCodes('&', getString());
    }
}
