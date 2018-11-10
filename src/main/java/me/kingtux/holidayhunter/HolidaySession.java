package me.kingtux.holidayhunter;

import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class HolidaySession {
    private SessionStatus sessionStatus;
    private Player player;
    private String[] commands, messages;
    private String name;

    public HolidaySession(Player player) {
        this.player = player;
        sessionStatus = SessionStatus.NAME;
    }

    public void name(String name) {
        //They are Lower Case!
        this.name = name.toLowerCase();
        sessionStatus = SessionStatus.COMMANDS;
    }

    private void parseCommand(String stuff) {

        sessionStatus = SessionStatus.MESSAGES;
    }

    private void parseMessages() {

        sessionStatus = SessionStatus.FINISH;
    }

    public HeadProduct finish() {
        if (sessionStatus != SessionStatus.FINISH) {
            Logger.getLogger("HolidaySession").warning("You cannot finish until the above is ready");
            return null;
        }
        return new HeadProduct(commands, messages, name);

    }

    public static class HeadProduct {
        private String[] commands, messages;
        private String name;
        private int id;

        HeadProduct(String[] commands, String[] messages, String name) {
            this.commands = commands;
            this.messages = messages;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String[] getCommands() {
            return commands;
        }

        public String[] getMessages() {
            return messages;
        }

        public String getName() {
            return name;
        }
    }

    public enum SessionStatus {
        NAME,
        COMMANDS,
        MESSAGES,
        FINISH;
    }
}
