package me.kingtux.holidayhunter.commands;

import me.kingtux.holidayhunter.HeadManager;
import me.kingtux.holidayhunter.HolidayHunter;
import me.kingtux.holidayhunter.HolidaySession;
import me.kingtux.holidayhunter.commands.api.*;
import me.kingtux.holidayhunter.lang.LangFile;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@IkeaCommandRules(command = "/holiday-hunter", description = "The Holiday Hunter", aliases = "hh", format = "/hh")
public class HolidayCommand implements IkeaCommand {
    private HolidayHunter holidayHunter;

    public HolidayCommand(HolidayHunter holidayHunter) {
        this.holidayHunter = holidayHunter;
    }

    @BaseCommand
    public void baseCommand(CommandSender commandSender) {

    }

    @SubCommand(subcommand = "reload")
    public void reloadCommand(CommandSender commandSender) {
        //Lets Start a reload
    }


    @SubCommand(subcommand = "create")
    public void createCommand(Player player) {
        if (holidayHunter.getSessions().containsKey(player)) {
            //BAD PERSON
        }
        holidayHunter.newSession(player);
        player.sendMessage("Hey run /hh name YOUR NAME");
    }

    @SubCommand(subcommand = "head")
    public void head(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Not Enough Arguments");
        }
        if (!holidayHunter.getHeadManager().isHead(args[0])) {
            player.sendMessage("Not a Head!");
        }
        player.getInventory().addItem(getHeadManager().createItem(args[0]));
        player.sendMessage("Here is your head");
    }

    public HeadManager getHeadManager() {
        return holidayHunter.getHeadManager();
    }

    @TabCompleter(type = TabCompleter.TYPE.SUB_COMMAND, subCommandToEffect = "head")
    public List<String> headTabCompleter() {
        return Arrays.asList(getHeadManager().getAllNamesOfHeads());
    }


    @SubCommand(subcommand = "name")
    public void nameCommand(Player player, String[] args) {
        if (!holidayHunter.getSessions().containsKey(player)) {
            //BAD PERSON
        }
        if (args.length == 0) {
            player.sendMessage("Please provide a name");
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
        if (!holidayHunter.getSessions().containsKey(player)) {
            //BAD PERSON
        }
        if (args.length == 0) {
            player.sendMessage("Please provide a command");
        }
        HolidaySession holidaySession = holidayHunter.getSessions().get(player);
        holidaySession.parseCommand(StringUtils.join(args));
        holidayHunter.updateSession(player, holidaySession);
        player.sendMessage(LangFile.SET_MESSAGES.colorStringList().toArray(new String[0]));


    }

    @OtherCommand(commandType = OtherCommand.CommandType.HELP)
    @SubCommand(subcommand = "help")
    public void helpSubCommand(Player player, String[] args) {
        player.sendMessage("AT the moment please message KingTUx for help");
    }

    @SubCommand(subcommand = "message", alias = "messages")
    public void messageSubCommand(Player player, String[] args) {
        if (!holidayHunter.getSessions().containsKey(player)) {
            //BAD PERSON
        }
        if (args.length == 0) {
            player.sendMessage("Please provide a command");
        }
        HolidaySession holidaySession = holidayHunter.getSessions().get(player);
        holidaySession.parseMessages(StringUtils.join(args));
        holidayHunter.updateSession(player, holidaySession);
        player.sendMessage(LangFile.FINISH.colorStringList().toArray(new String[0]));

    }

    @SubCommand(subcommand = "finish")
    public void finishSubCommand(Player player, String[] args) {
        if (!holidayHunter.getSessions().containsKey(player)) {
            //BAD PERSON
        }
        getHeadManager().createHead(holidayHunter.getSessions().get(player).finish());
        player.getInventory().addItem(getHeadManager().createItem(holidayHunter.getSessions().get(player).getName()));
        player.sendMessage(LangFile.HERE_IS_YOUR_HEAD.getColorValue());
    }

    @SubCommand(subcommand = "clear-sessions")
    public void clearSessions(CommandSender s) {
        if (s.hasPermission("holidayhunter.clear-sessions")) {
            holidayHunter.clear();
        } else {

        }

    }


    @Override
    public String mustBeAPlayer() {
        return LangFile.MUST_BE_A_PLAYER.getColorValue();
    }
}
