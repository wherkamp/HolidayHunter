package me.kingtux.holidayhunter.listeners;

import de.tr7zw.itemnbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import me.kingtux.holidayhunter.HolidayHunter;
import me.kingtux.holidayhunter.HolidaySession;
import me.kingtux.holidayhunter.lang.LangFile;
import me.kingtux.holidayhunter.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import static java.util.Map.Entry;

public class PlayerListener implements Listener {
    private HolidayHunter holidayHunter;

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock() == null) {
            return;
        }
        if (event.getBlock().getType() != Material.PLAYER_HEAD) {
            return;
        }
        NBTItem item = new NBTItem(event.getItemInHand());
        if (!item.hasKey("PURPOSE")) {
            return;
        }

        Set<Entry<String, String>> stuff = holidayHunter.getHeads().entrySet();
        Entry[] entry = new Entry[stuff.size()];
        int i = 0;
        for (Entry<String, String> stringStringEntry : stuff) {
            entry[i] = stringStringEntry;
            i++;
        }
        NMSUtil.applyTextureToItem((String) entry[new Random().nextInt(entry.length)].getValue(), event.getBlock());
        holidayHunter.getHolidayManager().placeHead(event.getBlock().getLocation(), item.getInteger("ID"));
        event.getPlayer().sendMessage("HeadHunter Block Placed");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock() == null) {
            return;
        }
        if (event.getBlock().getType() != Material.PLAYER_HEAD) {
            return;
        }
        if (holidayHunter.getHolidayManager().getHeadProductAt(event.getBlock().getLocation()) == null) {
            return;
        }
        if (!event.getPlayer().hasPermission("hh.breaker")) {
            event.setCancelled(true);
            return;
        }
        event.getPlayer().sendMessage(LangFile.DESTROYED_SKULL.getColorValue());
        holidayHunter.getHolidayManager().deletePlacedHead(event.getBlock().getLocation());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.PLAYER_HEAD) {
            return;
        }
        if (holidayHunter.getHolidayManager().getHeadProductAt(event.getClickedBlock().getLocation()) == null) {
            return;
        }
        if (holidayHunter.getHolidayManager().hasCollected(event.getPlayer(), event.getClickedBlock().getLocation())) {
            event.getPlayer().sendMessage(LangFile.ALREADY_COLLECTED.getColorValue());
            return;
        }
        HolidaySession.HeadProduct product = holidayHunter.getHolidayManager().getHeadProductAt(event.getClickedBlock().getLocation());
        act(product, event.getPlayer());
        holidayHunter.getHolidayManager().collectHead(event.getPlayer(), event.getClickedBlock().getLocation());

    }

    private void act(HolidaySession.HeadProduct product, Player player) {
        if (product == null) {
            return;
        }
        Arrays.stream(product.getCommands()).forEach(s -> Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, s.replace("%player%", player.getName()))));
        Arrays.stream(product.getMessages()).forEach(s -> player.sendMessage(ChatColor.translateAlternateColorCodes('&'
                , PlaceholderAPI.setPlaceholders(player, s.replace("%player%", player.getName())))));
    }


    public PlayerListener(HolidayHunter holidayHunter) {
        this.holidayHunter = holidayHunter;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        holidayHunter.remove(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!holidayHunter.getHolidayManager().doesUserExist(event.getPlayer().getUniqueId())) {
            holidayHunter.getHolidayManager().createUser(event.getPlayer().getUniqueId());
        }
    }
}
