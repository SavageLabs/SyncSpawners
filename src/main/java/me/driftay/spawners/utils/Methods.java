package me.driftay.spawners.utils;

import me.driftay.spawners.SyncSpawners;
import me.driftay.spawners.utils.FileManager.Files;
import me.driftay.spawners.utils.enums.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Methods {

    public static FileConfiguration config = SyncSpawners.instance.getConfig();

    public static String pl(String M) {
        return ChatColor.translateAlternateColorCodes('&', M);
    }

    public static String getPrefix() {
        return getPrefix("");
    }

    public static String getPrefix(String string) {
        return pl(Files.LANG.getFile().getString("Messages.Prefix") + string);
    }

    public static boolean hasPermission(CommandSender p, String perm, Boolean toggle) {
        if (p instanceof Player) {
            return hasPermission((Player) p, perm, toggle);
        } else {
            return true;
        }
    }
    private static boolean hasPermission(Player target, String perm, Boolean toggle) {
        if (target.hasPermission("syncspawners." + perm) || target.hasPermission("syncspawners.admin")) {
            return true;
        } else {
            if (toggle) {
                target.sendMessage(Message.NO_PERMS.getMessage());
            }
            return false;
        }
    }

}
