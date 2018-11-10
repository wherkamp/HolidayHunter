package me.kingtux.holidayhunter.utils;

import com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.lang.reflect.Method;
import java.util.UUID;

public class NMSUtil {

    private static Class<?> tileEntityClass;
    private static Class<?> blockPositionClass;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            tileEntityClass = Class.forName("net.minecraft.server." + version + ".TileEntitySkull");
            blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void applyTextureToItem(String texture, Block b) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new com.mojang.authlib.properties.Property("textures", texture));
        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new com.mojang.authlib.properties.Property("textures", texture));

            Object nmsWorld = b.getWorld().getClass().getMethod("getHandle", new Class[0]).invoke(b.getWorld());
            Object tileEntity;

            Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", blockPositionClass);
            tileEntity = tileEntityClass.cast(getTileEntity.invoke(nmsWorld,
                    getBlockPositionFor(b.getX(), b.getY(), b.getZ())));
            tileEntityClass.getMethod("setGameProfile", GameProfile.class).invoke(tileEntity, profile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static Object getBlockPositionFor(int x, int y, int z) {
        Object blockPosition = null;
        try {
            java.lang.reflect.Constructor<?> cons = blockPositionClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            blockPosition = cons.newInstance(x, y, z);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return blockPosition;
    }
}
