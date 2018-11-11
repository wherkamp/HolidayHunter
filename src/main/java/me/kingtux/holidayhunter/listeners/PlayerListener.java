package me.kingtux.holidayhunter.listeners;

import de.tr7zw.itemnbtapi.NBTItem;
import de.tr7zw.itemnbtapi.NBTTileEntity;
import me.kingtux.holidayhunter.HolidayHunter;
import me.kingtux.holidayhunter.HolidaySession;
import me.kingtux.holidayhunter.lang.LangFile;
import me.kingtux.holidayhunter.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.Map;

import static java.util.Map.Entry;

import java.util.Random;
import java.util.Set;

public class PlayerListener implements Listener {
    private HolidayHunter holidayHunter;

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
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
        holidayHunter.getHeadManager().placeHead(event.getBlock().getLocation(), item.getInteger("ID"));
        event.getPlayer().sendMessage("HeadHunter Block Placed");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock() == null) {
            return;
        }
        if (event.getBlock().getType() != Material.SKULL) {
            return;
        }
        if (!holidayHunter.getHeadManager().canBreak(event.getPlayer(), event.getBlock().getLocation())) {
            return;
        }
        if (!event.getPlayer().hasPermission("hh.breaker")) {
            event.setCancelled(true);
            return;
        }
        event.getPlayer().sendMessage(LangFile.DESTROYED_SKULL.getColorValue());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.SKULL) {
            return;
        }
        if (!holidayHunter.getHeadManager().canBreak(event.getPlayer(), event.getClickedBlock().getLocation())) {
            return;
        }
        HolidaySession.HeadProduct product = holidayHunter.getHeadManager().getHeadAtLocation(event.getClickedBlock().getLocation());
        act(product, event.getPlayer());
        holidayHunter.getHeadManager().brokeHead(event.getPlayer().getUniqueId(), event.getClickedBlock().getLocation());
    }

    private void act(HolidaySession.HeadProduct product, Player player) {
        if (product == null) {
            return;
        }
        Arrays.stream(product.getCommands()).forEach(s -> Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), s.replace("%player%", player.getName())));
        Arrays.stream(product.getMessages()).forEach(s -> Bukkit.broadcastMessage(s.replace("%player%", player.getName())));
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
        if (!holidayHunter.getHeadManager().doesUserExist(event.getPlayer().getUniqueId())) {
            holidayHunter.getHeadManager().createUser(event.getPlayer().getUniqueId());
        }
    }
}
