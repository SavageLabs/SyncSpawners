package me.driftay.spawners;

import me.driftay.spawners.commands.Commands;
import me.driftay.spawners.listeners.ChunkThread;
import me.driftay.spawners.listeners.Event;
import me.driftay.spawners.utils.FileManager;
import me.driftay.spawners.utils.FileManager.Files;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SyncSpawners extends JavaPlugin {

    private static FileManager fm = FileManager.getInstance();

    public static SyncSpawners instance;

    public static List<CreatureSpawner> creatureSpawners = new ArrayList<>();
    public static List<CreatureSpawner> queue_ready = new ArrayList<>();
    public static List<Chunk> chunk_queue_load = new ArrayList<>();
    public static List<Chunk> chunk_queue_unload = new ArrayList<>();


    public static ChunkThread chunkThread;

    public void onEnable(){

        instance = this;
        fm.logInfo(true).setup(this);

        Bukkit.getPluginManager().registerEvents(new Event(), this);

        getCommand("ss").setExecutor(new Commands());

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

        chunk_queue_load.clear();
        chunk_queue_unload.clear();

        chunkThread = new ChunkThread();
        chunkThread.start();

        int interval = Files.CONFIG.getFile().getInt("spawn-time");

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
                    }
                }
            }
        }.runTaskTimer(instance, 0, interval*20);
    }
}
