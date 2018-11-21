package me.kingtux.holidayhunter;

import de.tr7zw.itemnbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import me.kingtux.holidayhunter.lang.LangFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HolidayManager {

    private Connection connection;
    private HolidayHunter holidayHunter;

    public HolidayManager(HolidayHunter holidayHunter) {
        this.holidayHunter = holidayHunter;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:" + new File(holidayHunter.getDataFolder(), "holiday.sql").getAbsolutePath());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.createStatement().execute(SQL.CREATE_HEAD_TABLE.sql);
            connection.createStatement().execute(SQL.CREATE_PLACED_TABLE.sql);
            connection.createStatement().execute(SQL.CREATE_USERS_TABLE.sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public HolidaySession.HeadProduct getHeadByName(String name) {
        HolidaySession.HeadProduct product = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.GET_HEAD_BY_NAME.sql);
            preparedStatement.setString(1, name.toLowerCase());
            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                product = new HolidaySession.HeadProduct(set.getString("commands").split("__-__"), set.getString("messages").split("__-__"), name);
                product.setId(set.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public HolidaySession.HeadProduct getHeadByID(int name) {
        HolidaySession.HeadProduct product = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.GET_HEAD_BY_ID.sql);
            preparedStatement.setInt(1, name);
            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                product = new HolidaySession.HeadProduct(set.getString("commands").split("__-__"), set.getString("messages").split("__-__"), set.getString("name"));
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


    public int createHead(HolidaySession.HeadProduct headProduct) {
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

    public ItemStack createItem(HolidaySession.HeadProduct headProduct) {
        if (headProduct == null) {
            return null;
        }
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
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


    public HolidaySession.HeadProduct getHeadProductAt(Location location) {
        HolidaySession.HeadProduct headProduct = null;
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

    public int getPlaceID(Location location) {
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

    public void deletePlacedHead(Location location) {
        int id = getPlaceID(location);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.DELETE_HEAD.sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfPlacedHeads() {
        int totalHeads = 0;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(SQL.GET_PLACED_HEADS.sql);
            while (resultSet.next()) {
                totalHeads++;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalHeads;
    }

    public void resetUsers() {
        try {
            connection.createStatement().execute("DROP TABLE users;");
            connection.createStatement().execute(SQL.CREATE_USERS_TABLE.sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetPlaced() {
        try {
            connection.createStatement().execute("DROP TABLE placed;");
            connection.createStatement().execute(SQL.CREATE_PLACED_TABLE.sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetHead() {
        try {
            connection.createStatement().execute("DROP TABLE heads;");
            connection.createStatement().execute(SQL.CREATE_PLACED_TABLE.sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUser(UUID uuid) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.CREATE_USER.sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, "");
            preparedStatement.setInt(3, 0);
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

    public void collectHead(Player player, Location location) {
        if (hasCollected(player, location)) {
            return;
        }
        if (!hasStarted(player)) {
            LangFile.STARTED_THE_HUNT.getStringList().forEach(s -> {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, s)));
            });
            holidayHunter.getConfig().getStringList("start-hunt").forEach(s -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, s.replace("%player%", player.getName()))));
            });
        }
        int numberCollectedHeads = numberOfCollectedHeads(player);
        int placeId = getPlaceID(location);
        System.out.println("placeId = " + placeId);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : getCollectedHeads(player)) {
            if (isPlacedHead(i)) {
                stringBuilder = stringBuilder.append(i).append("__-__");
            }
        }
        stringBuilder = stringBuilder.append(placeId);
        System.out.println("stringBuilder.toString() = " + stringBuilder.toString());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.UPDATE_USER.sql);
            preparedStatement.setString(1, stringBuilder.toString());
            preparedStatement.setInt(2, numberCollectedHeads + 1);
            preparedStatement.setString(3, player.getUniqueId().toString());
            System.out.println(preparedStatement.execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (hasCompleted(player)) {
            LangFile.COMPLETED_THE_HUNT.getStringList().forEach(s -> {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, s)));
            });
            holidayHunter.getConfig().getStringList("end-hunt").forEach(s -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, s.replace("%player%", player.getName()))));
            });
        }
    }

    private boolean isPlacedHead(int i) {
        boolean b = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.GET_PLACED_BY_ID.sql);
            preparedStatement.setInt(1, i);
            ResultSet resultSet = preparedStatement.executeQuery();
            b = resultSet.next();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public boolean hasCollected(Player player, Location location) {
        return getCollectedHeads(player).contains(getPlaceID(location));

    }

    public List<Integer> getCollectedHeads(Player player) {
        List<Integer> collectedHeads = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL.GET_USER.sql);
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            if (result.getInt("found") != 0) {
                for (String s : result.getString("heads").split("__-__")) {
                    collectedHeads.add(Integer.valueOf(s));
                }
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return collectedHeads;
    }

    public int numberOfCollectedHeads(Player player) {
        return getCollectedHeads(player).size();
    }

    public boolean hasCompleted(Player player) {
        System.out.println("numberOfCollectedHeads(player) = " + numberOfCollectedHeads(player));
        System.out.println("getNumberOfPlacedHeads() = " + getNumberOfPlacedHeads());
        return numberOfCollectedHeads(player) >= getNumberOfPlacedHeads();
    }

    public boolean hasStarted(Player player) {
        System.out.println("numberOfCollectedHeads(player) = " + numberOfCollectedHeads(player));
        if (numberOfCollectedHeads(player) != 0) {
            return true;
        }
        return false;
    }

    public Connection getConnection() {
        return connection;
    }

    private enum SQL {
        CREATE_HEAD_TABLE("CREATE TABLE IF NOT EXISTS heads ( id integer PRIMARY KEY AUTOINCREMENT, name text, commands text, messages text, alive text);"),
        CREATE_PLACED_TABLE("CREATE TABLE IF NOT EXISTS placed ( id integer PRIMARY KEY AUTOINCREMENT, head integer, world text, x REAL, y REAL, z REAL, pitch REAL, yaw REAL); "),
        CREATE_USERS_TABLE("CREATE TABLE IF NOT EXISTS users ( id integer PRIMARY KEY AUTOINCREMENT, uuid text, heads text, found integer);"),
        GET_HEADS_NAMES("SELECT name FROM heads WHERE alive='true' ;"),
        CREATE_HEAD("INSERT INTO heads (name, commands, messages, alive) VALUES (?,?,?,?);"),
        GET_HEAD_BY_NAME("SELECT * from heads where name=?;"),
        GET_HEAD_BY_ID("SELECT * from heads where id=?;"),
        PLACE_HEAD("INSERT INTO placed (head, world, x, y, z, pitch, yaw) VALUES (?,?,?,?,?,?,?);"),
        HEAD_AT("SELECT * FROM placed WHERE  world=? and x=? and y=? and z=? and pitch = ? and yaw=?"),
        GET_PLACED_HEADS("SELECT * FROM placed;"),
        GET_PLACED_BY_ID("SELECT * FROM placed WHERE id=?;"),
        DELETE_HEAD("DELETE FROM placed WHERE id=?; "),
        CREATE_USER("INSERT INTO users (uuid, heads,found) VALUES (?,?,?);"),
        UPDATE_USER("UPDATE users SET heads=? , found=? WHERE uuid=?;"),
        GET_USER("SELECT * FROM users WHERE uuid=?;");
        private String sql;

        SQL(String sql) {
            this.sql = sql;
        }
    }
}
