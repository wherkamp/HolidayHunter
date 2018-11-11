package me.kingtux.holidayhunter.lang;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum LangFile {
    MUST_BE_A_PLAYER("Sorry, you must be a player"),

    ITEM_NAME("Place Head to make a holiday head"),
    ITEM_LORE(Collections.singletonList("Place Head on grab"));
    @ConfigValue
    private Object value;

    LangFile(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getString() {
        return (String) getValue();
    }

    public List<String> getStringList() {
        if (!(getValue() instanceof List) || (((List<?>) getValue()).size() >= 1 || (!(((List<?>) getValue()).get(0) instanceof String)))) {
            return null;
        }
        return (List<String>) getValue();
    }

    public String getColorValue() {
        return ChatColor.translateAlternateColorCodes('&', getString());
    }
}
