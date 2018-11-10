package me.kingtux.holidayhunter.commands.api;

public interface IkeaCommand {
    default  String mustBeAPlayer(){
        return "";
    }
}
