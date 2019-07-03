package me.driftay.spawners.commands;

import me.driftay.spawners.SyncSpawners;
import me.driftay.spawners.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CmdSyncReload implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("syncspawners")) {
            if (sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("syncspawners.admin")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        SyncSpawners.instance.reloadConfig();
                        SyncSpawners.startTimer();
                        sender.sendMessage(Message.RELOADED.getMessage());
                    }
                } else {
                    sender.sendMessage(Message.COMMAND_FORMAT.getMessage());
                }
            } else {
                sender.sendMessage(Message.NO_PERMISSION.getMessage());
            }
        }
        return false;
    }
}
