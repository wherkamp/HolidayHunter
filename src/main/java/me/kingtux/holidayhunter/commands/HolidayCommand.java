package me.kingtux.holidayhunter.commands;

import me.kingtux.holidayhunter.HeadManager;
import me.kingtux.holidayhunter.HolidayHunter;
import me.kingtux.holidayhunter.HolidayManager;
import me.kingtux.holidayhunter.HolidaySession;
import me.kingtux.holidayhunter.commands.api.*;
import me.kingtux.holidayhunter.lang.LangFile;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@IkeaCommandRules(command = "/holiday-hunter", description = "The Holiday Hunter", aliases = "hh", format = "/hh")
public class HolidayCommand implements IkeaCommand {
    private HolidayHunter holidayHunter;

    public HolidayCommand(HolidayHunter holidayHunter) {
        this.holidayHunter = holidayHunter;
    }

    @BaseCommand
    public void baseCommand(CommandSender player) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        player.sendMessage("Welcome to the HolidayCommand plugin by KingTux. Use /hh help to learn how to use it");
    }

    @SubCommand(subcommand = "create")
    public void createCommand(Player player) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }

        if (holidayHunter.getSessions().containsKey(player)) {
            player.sendMessage(LangFile.ALREADY_IN_SESSIONS.getColorValue());
            return;
        }
        holidayHunter.newSession(player);
        player.sendMessage(LangFile.SET_NAME.getColorValue());
    }

    @SubCommand(subcommand = "head")
    public void head(Player player, String[] args) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Invalid Format please use /hh head {headname}"));
            return;
        }
        if (!holidayHunter.getHolidayManager().isHead(args[0])) {
            player.sendMessage(LangFile.NOT_A_HEAD.getColorValue());
            return;
        }
        ItemStack itemStack = getHolidayManager().createItem(args[0]);
        if (itemStack == null) {
            player.sendMessage(LangFile.NOT_A_HEAD.getColorValue());
            return;
        }
        player.getInventory().addItem(itemStack);
        player.sendMessage(LangFile.HERE_IS_YOUR_HEAD.getColorValue());
    }

    public HolidayManager getHolidayManager() {
        return holidayHunter.getHolidayManager();
    }

    @TabCompleter(type = TabCompleter.TYPE.SUB_COMMAND, subCommandToEffect = "head")
    public List<String> headTabCompleter() {
        return Arrays.asList(getHolidayManager().getAllNamesOfHeads());
    }

    @SubCommand(subcommand = "drop")
    public void dropSubCommand(Player player, String[] args) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        if (args.length != 1) {
            player.sendMessage("You Failed");
            return;
        }
        if (args[0].equalsIgnoreCase("users")) {
            getHolidayManager().resetUsers();
        } else if (args[0].equalsIgnoreCase("heads")) {
            getHolidayManager().resetHead();
        } else if (args[0].equalsIgnoreCase("placed")) {
            getHolidayManager().resetPlaced();
        } else {
            player.sendMessage("Invalid Thing to Drop");
        }
        player.sendMessage("You dropped " + args[0]);
    }

    @TabCompleter(type = TabCompleter.TYPE.SUB_COMMAND, subCommandToEffect = "drop")
    public List<String> dropTabCompleter(HolidayCommand sender, String[] args) {
        return Arrays.asList("users", "heads", "placed");

    }

    @SubCommand(subcommand = "name")
    public void nameCommand(Player player, String[] args) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        if (!holidayHunter.getSessions().containsKey(player)) {
            player.sendMessage(LangFile.MISSING_SESSION.getColorValue());
            return;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Invalid Format please use /hh name {name}"));
            return;
        }
        HolidaySession holidaySession = holidayHunter.getSessions().get(player);

        if (args.length >= 2) {
            player.sendMessage("Names may not have multiple words if you want to use _");
        }
        holidaySession.name(args[0]);
        holidayHunter.updateSession(player, holidaySession);
        player.sendMessage(LangFile.SET_COMMANDS.colorStringList().toArray(new String[0]));
    }

    @SubCommand(subcommand = "command", alias = "commands")
    public void commandSubCommand(Player player, String[] args) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        if (!holidayHunter.getSessions().containsKey(player)) {
            player.sendMessage(LangFile.MISSING_SESSION.getColorValue());
            return;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Invalid Format please use /hh command {commands}"));
            return;
        }
        HolidaySession holidaySession = holidayHunter.getSessions().get(player);
        holidaySession.parseCommand(StringUtils.join(args, " "));
        holidayHunter.updateSession(player, holidaySession);
        player.sendMessage(LangFile.SET_MESSAGES.colorStringList().toArray(new String[0]));


    }

    @OtherCommand(commandType = OtherCommand.CommandType.HELP)
    @SubCommand(subcommand = "help")
    public void helpSubCommand(Player player, String[] args) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        player.sendMessage("AT the moment please message KingTUx for help");
    }

    @SubCommand(subcommand = "message", alias = "messages")
    public void messageSubCommand(Player player, String[] args) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        if (!holidayHunter.getSessions().containsKey(player)) {
            player.sendMessage(LangFile.MISSING_SESSION.getColorValue());
            return;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Invalid Format please use /hh message {message}"));
            return;
        }
        HolidaySession holidaySession = holidayHunter.getSessions().get(player);
        holidaySession.parseMessages(StringUtils.join(args, " "));
        holidayHunter.updateSession(player, holidaySession);
        player.sendMessage(LangFile.FINISH.colorStringList().toArray(new String[0]));

    }

    @SubCommand(subcommand = "finish")
    public void finishSubCommand(Player player, String[] args) {
        if (!player.hasPermission("hh.admin")) {
            player.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        if (!holidayHunter.getSessions().containsKey(player)) {
            player.sendMessage(LangFile.MISSING_SESSION.getColorValue());
            return;
        }
        getHolidayManager().createHead(holidayHunter.getSessions().get(player).finish());
        player.getInventory().addItem(getHolidayManager().createItem(holidayHunter.getSessions().get(player).getName()));
        player.sendMessage(LangFile.HERE_IS_YOUR_HEAD.getColorValue());
        holidayHunter.remove(player);

    }

    @SubCommand(subcommand = "clear-sessions")
    public void clearSessions(CommandSender s) {
        if (!s.hasPermission("hh.admin")) {
            s.sendMessage(LangFile.LACK_OF_PERMISSION.getColorValue());
            return;
        }
        holidayHunter.clear();
    }


    @Override
    public String mustBeAPlayer() {
        return LangFile.MUST_BE_A_PLAYER.getColorValue();
    }
}
