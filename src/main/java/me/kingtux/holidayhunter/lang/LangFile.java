package me.kingtux.holidayhunter.lang;

import org.bukkit.ChatColor;

public enum LangFile {
    MUST_BE_A_PLAYER("Sorry, you must be a player");
    @ConfigValue
    private String value;

    LangFile(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getColorValue() {
        return ChatColor.translateAlternateColorCodes('&', getValue());
    }
}
