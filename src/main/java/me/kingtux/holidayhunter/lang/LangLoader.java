package me.kingtux.holidayhunter.lang;

import me.kingtux.simpleannotation.AnnotationFinder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class LangLoader {
    public static void loadLang(File file, Class<? extends Enum> enu, boolean writeUnsetValues) {
        Field[] fields = AnnotationFinder.getFieldsWithAnnotation(enu, ConfigEntry.class);
        //There should only be one of them.
        Field editibleThing = AnnotationFinder.getFieldsWithAnnotation(enu, ConfigValue.class)[0];
        editibleThing.setAccessible(true);

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        for (Field field : fields) {
            field.setAccessible(true);
            ConfigEntry configEntry = field.getAnnotationsByType(ConfigEntry.class)[0];
            //Basically if it is not found in the needed language it will revert back to the default in English
            if (fileConfiguration.get(configEntry.path()) != null) {
                try {
                    editibleThing.set(Enum.valueOf(enu, field.getName()), fileConfiguration.get(configEntry.path()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        if (writeUnsetValues) {
            for (Field field : fields) {
                ConfigEntry configEntry = field.getAnnotationsByType(ConfigEntry.class)[0];
                //Basically if it is not found in the needed language it will revert back to the default in English
                if (fileConfiguration.get(configEntry.path()) != null) {
                    try {
                        fileConfiguration.set(configEntry.path(), editibleThing.get(field));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                fileConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
