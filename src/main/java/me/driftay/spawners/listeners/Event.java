package me.driftay.spawners.listeners;

import me.driftay.spawners.SyncSpawners;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Event implements Listener {

    @EventHandler
    public void chunkLoad(ChunkLoadEvent event){
        Chunk chunk = event.getChunk();

        if (!SyncSpawners.chunk_queue_load.contains(chunk)){
            SyncSpawners.chunk_queue_load.add(chunk);
        }
    }

    @EventHandler
    public void chunk(ChunkUnloadEvent event){
        Chunk chunk = event.getChunk();

        if (!SyncSpawners.chunk_queue_unload.contains(chunk)){
            SyncSpawners.chunk_queue_unload.add(chunk);
        }
    }

    @EventHandler
    public void place(BlockPlaceEvent e){
        Block block = e.getBlockPlaced();
        if (block.getState() instanceof CreatureSpawner){
            CreatureSpawner creatureSpawner = (CreatureSpawner)block.getState();
            if (creatureSpawner != null){
                if (!SyncSpawners.creatureSpawners.contains(creatureSpawner)){
                    SyncSpawners.creatureSpawners.add(creatureSpawner);
                }
            }
        }
    }
    @EventHandler
    public void place(BlockBreakEvent e){
        Block block = e.getBlock();
        if (block.getState() instanceof CreatureSpawner){
            CreatureSpawner creatureSpawner = (CreatureSpawner)block.getState();
            if (creatureSpawner != null){
                if (SyncSpawners.creatureSpawners.contains(creatureSpawner)){
                    SyncSpawners.creatureSpawners.remove(creatureSpawner);
                }
            }
        }
    }


    @EventHandler
    public void spawn(SpawnerSpawnEvent e){

        List<CreatureSpawner> l = SyncSpawners.creatureSpawners;

        CreatureSpawner creatureSpawner = e.getSpawner();

        if (l.contains(creatureSpawner)) {
            if (SyncSpawners.queue_ready.contains(creatureSpawner)){
                e.setCancelled(false);

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        SyncSpawners.queue_ready.remove(creatureSpawner);
                    }
                }.runTaskLater(SyncSpawners.instance, 5);

            } else{
                e.setCancelled(true);
                e.getSpawner().setDelay(100000);
                e.getSpawner().update();
            }
        } else{
            SyncSpawners.creatureSpawners.add(creatureSpawner);
            e.setCancelled(true);
            e.getSpawner().setDelay(100000);
            e.getSpawner().update();
        }

    }

}