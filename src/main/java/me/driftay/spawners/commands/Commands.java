package me.driftay.spawners.commands;

import me.driftay.spawners.SyncSpawners;
import me.driftay.spawners.utils.FileManager.Files;
import me.driftay.spawners.utils.Methods;
import me.driftay.spawners.utils.enums.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender p, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            if (!Methods.hasPermission(p, "help", true)) {
                return true;
            }
            p.sendMessage(Methods.getPrefix() + Methods.pl("&cThis shows a list of available commands."));
            p.sendMessage(Methods.pl("&8- &c/ss reload"));
            return true;
        } else {
            if (args[0].equalsIgnoreCase("reload")) {

                if (!Methods.hasPermission(p, "reload", true)) {
                    return true;
                }
                Files.CONFIG.reloadFile();
                Files.LANG.reloadFile();
                SyncSpawners.startTimer();
                p.sendMessage(Message.CONFIG_RELOAD.getMessage());
            }
        }
        return true;
    }
}
