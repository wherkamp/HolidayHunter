package me.kingtux.holidayhunter;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PlaceHolder extends PlaceholderExpansion {
    private HolidayHunter holidayHunter;

    public PlaceHolder(HolidayHunter holidayHunter) {
        this.holidayHunter = holidayHunter;
    }

    @Override
    public String getIdentifier() {
        return "hh";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (params.equalsIgnoreCase("total-heads")) {
            return String.valueOf(holidayHunter.getHolidayManager().getNumberOfPlacedHeads());
        } else if (params.equalsIgnoreCase("completed-heads")) {
            if(holidayHunter.getHolidayManager().numberOfCollectedHeads(p)==holidayHunter.getHolidayManager().getNumberOfPlacedHeads()){
                return String.valueOf(holidayHunter.getHolidayManager().getNumberOfPlacedHeads());
            }
            return String.valueOf(holidayHunter.getHolidayManager().numberOfCollectedHeads(p) +1);
        }

        return "";
    }

    @Override
    public String getAuthor() {
        return "KingTux";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getRequiredPlugin() {
        return "HolidayHunter";
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList("total-heads", "completed-heads");
    }
}
