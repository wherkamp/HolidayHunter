package me.kingtux.holidayhunter;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static me.kingtux.holidayhunter.HolidaySession.HeadProduct;

public class HeadManager {
    private Connection connection;
    private HolidayHunter holidayHunter;

    public HeadManager(HolidayHunter holidayHunter) {
        this.holidayHunter = holidayHunter;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:" + new File(holidayHunter.getDataFolder(), "heads.sql").getAbsolutePath());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    public HeadProduct getHeadByName(String name) {
        return null;
    }

    public void placeHead(Location location, String name) {
        placeHead(location, getHeadByName(name));
    }

    public void placeHead(Location location, HeadProduct headProduct) {

    }

    public void createHead(HeadProduct headProduct) {

    }

    public ItemStack createItem(String name) {
        return createItem(getHeadByName(name));
    }

    public ItemStack createItem(HeadProduct headProduct) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("PURPOSE", holidayHunter.getName().toUpperCase());
        nbti.setInteger("ID", headProduct.getId());
        itemStack = nbti.getItem();

        return itemStack;
    }

    private enum SQL {

    }
}
