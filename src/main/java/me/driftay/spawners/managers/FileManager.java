package me.driftay.spawners.managers;

import me.driftay.spawners.file.CustomFile;
import me.driftay.spawners.file.impl.MessageFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FileManager {

    private JavaPlugin plugin;
    private static Map<String, CustomFile> fileMap = new HashMap<>();

    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
        addFile(new MessageFile(plugin));
    }

    private void addFile(CustomFile file) {
        fileMap.put(file.getName(), file);
        plugin.getLogger().log(Level.INFO, file.getName() + " has initialized.");
        file.init();
    }

    public static Map<String, CustomFile> getFileMap() {
        return fileMap;
    }

}
