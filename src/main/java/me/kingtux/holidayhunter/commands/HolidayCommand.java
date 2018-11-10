package me.kingtux.holidayhunter.commands;

import me.kingtux.holidayhunter.HolidayHunter;
import me.kingtux.holidayhunter.commands.api.BaseCommand;
import me.kingtux.holidayhunter.commands.api.IkeaCommand;
import me.kingtux.holidayhunter.commands.api.IkeaCommandRules;
import me.kingtux.holidayhunter.commands.api.SubCommand;
import me.kingtux.holidayhunter.lang.LangFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if(holidayHunter.getSessions().containsKey(player)){
            //BAD PERSON
        }

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
