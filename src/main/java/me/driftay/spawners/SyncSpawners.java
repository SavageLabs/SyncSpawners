package me.driftay.spawners;

import me.driftay.spawners.commands.CmdSyncReload;
import me.driftay.spawners.file.CustomFile;
import me.driftay.spawners.file.impl.MessageFile;
import me.driftay.spawners.listeners.ChunkThread;
import me.driftay.spawners.listeners.Event;
import me.driftay.spawners.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyncSpawners extends JavaPlugin {
    public static SyncSpawners instance;
    private FileManager fileManager;

    public static List<CreatureSpawner> creatureSpawners = new ArrayList<>();
    public static List<CreatureSpawner> queue_ready = new ArrayList<>();
    public static List<Chunk> chunk_queue_load = new ArrayList<>();
    public static List<Chunk> chunk_queue_unload = new ArrayList<>();


    public static ChunkThread chunkThread;

    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        fileManager = new FileManager(this);
        Collections.singletonList(new MessageFile(this)).forEach(CustomFile::init);

        Bukkit.getPluginManager().registerEvents(new Event(), this);

        getCommand("syncspawners").setExecutor(new CmdSyncReload());

        startTimer();

    }

    public void onDisable(){
        chunkThread.interrupt();
    }

    public static void startTimer(){

        Bukkit.getScheduler().cancelTasks(instance);

        if (chunkThread != null){
            chunkThread.interrupt();
        }

        chunk_queue_load.clear(); // clear the chunks in the queue, for reloading purposes
        chunk_queue_unload.clear();

        chunkThread = new ChunkThread();
        chunkThread.start();

        int interval = instance.getConfig().getInt("spawn-time"); // config

        new BukkitRunnable(){
            @Override
            public void run() {
                for (int i = creatureSpawners.size() - 1; i >= 0; i--){
                    CreatureSpawner creatureSpawner = creatureSpawners.get(i);
                    if (creatureSpawner != null && creatureSpawner.isPlaced()) {
                        if (!queue_ready.contains(creatureSpawner)){
                            queue_ready.add(creatureSpawner);
                        }
                        creatureSpawner.setDelay(0);
                        creatureSpawner.update();
                        //Bukkit.broadcastMessage("updated.");
                    }
                }
            }
        }.runTaskTimer(instance, 0, interval*20);
    }


    public FileManager getFileManager() {
        return fileManager;
    }
}
