package me.driftay.spawners.utils;

import me.driftay.spawners.dSpawners;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static FileConfiguration config = dSpawners.instance.getConfig();

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> color(List<String> messages) {
        List<String> list = new ArrayList<>();
        for (String line : messages) {
            list.add(color(line));
        }
        return list;
    }

}
