package me.driftay.spawners.listeners;

import me.driftay.spawners.dSpawners;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
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

        if (!dSpawners.chunk_queue_load.contains(chunk)){
            dSpawners.chunk_queue_load.add(chunk);
        }
    }

    @EventHandler
    public void chunk(ChunkUnloadEvent event){
        Chunk chunk = event.getChunk();

        if (!dSpawners.chunk_queue_unload.contains(chunk)){
            dSpawners.chunk_queue_unload.add(chunk);
        }
    }

    @EventHandler
    public void place(BlockPlaceEvent e){
        Block block = e.getBlockPlaced();
        if (block.getState() instanceof CreatureSpawner){
            CreatureSpawner creatureSpawner = (CreatureSpawner)block.getState();
            if (creatureSpawner != null){
                if (!dSpawners.creatureSpawners.contains(creatureSpawner)){
                    dSpawners.creatureSpawners.add(creatureSpawner);
                   // e.getPlayer().sendMessage("ttt");
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
                if (dSpawners.creatureSpawners.contains(creatureSpawner)){
                    dSpawners.creatureSpawners.remove(creatureSpawner);
                   // e.getPlayer().sendMessage("!!!!!ttt");
                }
            }
        }
    }


    @EventHandler
    public void spawn(SpawnerSpawnEvent e){
        // e.setCancelled(true);
        //            e.getSpawner().setDelay(100000);
        //            e.getSpawner().update();

        List<CreatureSpawner> l = dSpawners.creatureSpawners;

        CreatureSpawner creatureSpawner = e.getSpawner();

        if (l.contains(creatureSpawner)) {
            //good
            if (dSpawners.queue_ready.contains(creatureSpawner)){
                //is ready to spawn... let it spawn
                e.setCancelled(false);//let it spawn...

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        dSpawners.queue_ready.remove(creatureSpawner);
                    }
                }.runTaskLater(dSpawners.instance, 5);

            }else{
                //is good, and is not ready to spawn, do not let it spawn
                e.setCancelled(true);
                e.getSpawner().setDelay(100000);
                e.getSpawner().update();
            }
        }else{
            //not good, add to good
            dSpawners.creatureSpawners.add(creatureSpawner);
            e.setCancelled(true);
            e.getSpawner().setDelay(100000);
            e.getSpawner().update();
        }

    }

}