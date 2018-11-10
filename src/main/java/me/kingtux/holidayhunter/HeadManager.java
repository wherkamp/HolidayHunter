package me.kingtux.holidayhunter;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        try {
            connection.createStatement().execute(SQL.TABLE.sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public HeadProduct getHeadByName(String name) {
        return null;
    }

    public boolean isHead(String name) {
        return true;
    }


    public int createHead(HeadProduct headProduct) {
        int i = 0;
        try {
            PreparedStatement t = connection.prepareStatement(SQL.CREATE_HEAD.getSql());
            t.setString(1, headProduct.getName());
            t.setString(2, badlyCombine(headProduct.getCommands()));
            t.setString(3, badlyCombine(headProduct.getMessages()));
            t.setString(4, "true");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    private String badlyCombine(String[] stuff) {
        StringBuilder sb = new StringBuilder();
        for (String s : stuff) {
            sb = sb.append(s).append("__-__");
        }
        return sb.toString();
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

    public String[] getAllNamesOfHeads() {
        List<String> names = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery(SQL.GET_HEADS_NAMES.toString());
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names.toArray(new String[0]);

    }

    private enum SQL {
        TABLE("CREATE TABLE IF NOT EXISTS heads ( id integer PRIMARY KEY AUTOINCREMENT, name text, commands text, messages text, alive text); "),
        GET_HEADS_NAMES("SELECT name from heads where alive=\"true\";"),
        CREATE_HEAD("INSERT INTO heads (name, commands, messages, alive) VALUES (?,?,?,?);");
        private String sql;

        public String getSql() {
            return sql;
        }

        SQL(String sql) {
            this.sql = sql;
        }
    }
}
