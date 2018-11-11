package me.kingtux.holidayhunter.lang;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum LangFile {
    //Common Entries
    @ConfigEntry(path = "common.must-be-a-player")
    MUST_BE_A_PLAYER("Sorry, you must be a player"),
    @ConfigEntry(path = "common.lack-of-permission")
    LACK_OF_PERMISSION("Sorry you lack permission to do that!"),
    ITEM_NAME("Place Head to make a holiday head"),
    ITEM_LORE("Place Head on grab"),
    DESTROYED_SKULL("The head has been destroyed"),
    SET_COMMANDS("Please provide the commands"),
    SET_MESSAGES("Please provide the messages"),
    FINISH("Run /hh finish to finish the command"),
    HERE_IS_YOUR_HEAD("Here is your head!");
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
