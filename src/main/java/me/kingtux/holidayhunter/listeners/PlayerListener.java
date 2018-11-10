package me.kingtux.holidayhunter.listeners;

import de.tr7zw.itemnbtapi.NBTTileEntity;
import me.kingtux.holidayhunter.HolidayHunter;
import me.kingtux.holidayhunter.utils.NMSUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import static java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class PlayerListener implements Listener {
    private HolidayHunter holidayHunter;

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        NBTTileEntity nbtTileEntity = new NBTTileEntity(event.getBlock().getState());
        if (!nbtTileEntity.hasKey("PURPOSE") || !nbtTileEntity.getString("PURPOSE").equals(holidayHunter.getName().toUpperCase())) {
            return;
        }

        Set<Entry<String, String>> stuff = holidayHunter.getHeads().entrySet();
        Entry[] entry = new Entry[stuff.size()];
        int i = 0;
        for (Entry<String, String> stringStringEntry : stuff) {
            entry[i] = stringStringEntry;
            i++;
        }
        NMSUtil.applyTextureToItem(entry[new Random().nextInt(entry.length)].getValue(), event.getBlock());
        event.getPlayer().sendMessage("HeadHunter Block Placed");
    }

    public PlayerListener(HolidayHunter holidayHunter) {
        this.holidayHunter = holidayHunter;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {

        holidayHunter.remove(e.getPlayer());
    }
}
