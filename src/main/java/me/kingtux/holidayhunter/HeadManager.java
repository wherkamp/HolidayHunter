package me.kingtux.holidayhunter;

import de.tr7zw.itemnbtapi.NBTItem;
import me.kingtux.holidayhunter.lang.LangFile;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.sql.*;
import java.util.*;

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
            connection.createStatement().execute(SQL.TABLE_TWO.sql);
            connection.createStatement().execute(SQL.TABLE_THREE.sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public HeadProduct getHeadByName(String name) {
        HeadProduct product = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.GET_HEAD_BY_NAME.sql);
            preparedStatement.setString(1, name.toLowerCase());
            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                product = new HeadProduct(set.getString("commands").split("__-__"), set.getString("messages").split("__-__"), name);
                product.setId(set.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public HeadProduct getHeadByID(int name) {
        HeadProduct product = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.GET_HEAD_BY_ID.sql);
            preparedStatement.setInt(1, name);
            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                product = new HeadProduct(set.getString("commands").split("__-__"), set.getString("messages").split("__-__"), set.getString("name"));
                product.setId(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public boolean isHead(String name) {
        return getHeadByName(name) != null;
    }


    public int createHead(HeadProduct headProduct) {
        int i = 0;
        try {
            PreparedStatement t = connection.prepareStatement(SQL.CREATE_HEAD.sql);
            t.setString(1, headProduct.getName());
            t.setString(2, badlyCombine(headProduct.getCommands()));
            t.setString(3, badlyCombine(headProduct.getMessages()));
            t.setString(4, "true");
            t.execute();
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
        if (headProduct == null) {
            return null;
        }
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("PURPOSE", holidayHunter.getName().toUpperCase());
        nbti.setInteger("ID", headProduct.getId());
        itemStack = nbti.getItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', LangFile.ITEM_NAME.getString()));
        itemMeta.setLore(LangFile.ITEM_LORE.getStringList());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public String[] getAllNamesOfHeads() {
        List<String> names = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery(SQL.GET_HEADS_NAMES.sql);
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names.toArray(new String[0]);

    }

    public HeadProduct getHeadAtLocation(Location location) {
        HeadProduct headProduct = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.HEAD_AT.sql);
            preparedStatement.setString(1, location.getWorld().getName());
            preparedStatement.setDouble(2, location.getX());
            preparedStatement.setDouble(3, location.getY());
            preparedStatement.setDouble(4, location.getZ());
            preparedStatement.setDouble(5, location.getPitch());
            preparedStatement.setDouble(6, location.getYaw());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                headProduct = getHeadByID(resultSet.getInt("head"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return headProduct;
    }

    public int getHeadID(Location location) {
        int i = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.HEAD_AT.sql);
            preparedStatement.setString(1, location.getWorld().getName());
            preparedStatement.setDouble(2, location.getX());
            preparedStatement.setDouble(3, location.getY());
            preparedStatement.setDouble(4, location.getZ());
            preparedStatement.setDouble(5, location.getPitch());
            preparedStatement.setDouble(6, location.getYaw());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                i = resultSet.getInt("id");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public void placeHead(Location location, int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.PLACE_HEAD.sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setDouble(3, location.getX());
            preparedStatement.setDouble(4, location.getY());
            preparedStatement.setDouble(5, location.getZ());
            preparedStatement.setDouble(6, location.getPitch());
            preparedStatement.setDouble(7, location.getYaw());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createUser(UUID uuid) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.CREATE_USER.sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, "0");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesUserExist(UUID uuid) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.GET_USER.sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                return true;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void brokeHead(UUID uuid, Location location) {
        int i = getHeadID(location);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.UPDATE_USER.sql);
            preparedStatement.setString(1, StringUtils.join(getCollectedHeads(uuid), "__-__") + "__-__" + i);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[] getCollectedHeads(UUID uuid) {
        String[] stuff = new String[0];
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.GET_USER.sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stuff = resultSet.getString("heads").split("__-__");
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Arrays.stream(stuff).map(this::parseInt).mapToInt(i -> i).toArray();
    }

    public int parseInt(String in) {
        try {
            return Integer.parseInt(in);
        } catch (NumberFormatException e) {

        }
        return 0;
    }

    public boolean canBreak(Player player, Location location) {
        int[] heads = getCollectedHeads(player.getUniqueId());
        int head = getHeadID(location);
        return !Arrays.asList(heads).contains(head);
    }

    private enum SQL {
        TABLE("CREATE TABLE IF NOT EXISTS heads ( id integer PRIMARY KEY AUTOINCREMENT, name text, commands text, messages text, alive text); "),
        TABLE_TWO("CREATE TABLE IF NOT EXISTS placed ( id integer PRIMARY KEY AUTOINCREMENT, head integer, world text, x REAL, y REAL, z REAL, pitch REAL, yaw REAL); "),
        TABLE_THREE("CREATE TABLE IF NOT EXISTS users ( id integer PRIMARY KEY AUTOINCREMENT, uuid text, heads text);"),
        GET_HEADS_NAMES("SELECT name FROM heads WHERE alive='true' ;"),
        CREATE_HEAD("INSERT INTO heads (name, commands, messages, alive) VALUES (?,?,?,?);"),
        GET_HEAD_BY_NAME("SELECT * from heads where name=?;"),
        GET_HEAD_BY_ID("SELECT * from heads where id=?;"),
        PLACE_HEAD("INSERT INTO placed (head, world, x, y, z, pitch, yaw) VALUES (?,?,?,?,?,?,?);"),
        HEAD_AT("SELECT * FROM placed WHERE  world=? and x=? and y=? and z=? and pitch = ? and yaw=?"),
        CREATE_USER("INSERT INTO users (uuid, heads) VALUES (?,?);"),
        UPDATE_USER("UPDATE users SET heads=? WHERE uuid=?;"),
        GET_USER("SELECT * FROM users WHERE uuid=?;");
        private String sql;

        SQL(String sql) {
            this.sql = sql;
        }
    }
}
