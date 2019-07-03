package me.driftay.spawners.listeners;

import me.driftay.spawners.SyncSpawners;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;

public class ChunkThread extends java.lang.Thread {

    @Override
    public void run() {

        while(true){

            try {
                ChunkThread.sleep(100L);
                //
                for (int i = SyncSpawners.chunk_queue_load.size() - 1; i >= 0; i--){
                    Chunk chunk = SyncSpawners.chunk_queue_load.get(i);
                    if (chunk != null && chunk.isLoaded()){

                        for (BlockState blockState : chunk.getTileEntities()){
                            if (blockState != null && blockState instanceof CreatureSpawner){
                                //is a spawner
                                CreatureSpawner creatureSpawner = (CreatureSpawner)blockState;

                                if (creatureSpawner != null){
                                    if (!SyncSpawners.creatureSpawners.contains(creatureSpawner)){
                                        SyncSpawners.creatureSpawners.add(creatureSpawner);
                                       // System.out.print("\n\n\n Added a loaded spawner... \n\n\n");
                                    }
                                }
                            }
                        }
                        SyncSpawners.chunk_queue_load.remove(chunk);
                    }
                }
                for (int i = SyncSpawners.chunk_queue_unload.size() - 1; i >= 0; i--){
                    Chunk chunk = SyncSpawners.chunk_queue_unload.get(i);
                    if (chunk != null && chunk.isLoaded()){

                        for (BlockState blockState : chunk.getTileEntities()){
                            if (blockState != null && blockState instanceof CreatureSpawner){
                                //is a spawner
                                CreatureSpawner creatureSpawner = (CreatureSpawner)blockState;

                                if (creatureSpawner != null){
                                    if (SyncSpawners.creatureSpawners.contains(creatureSpawner)){
                                        SyncSpawners.creatureSpawners.remove(creatureSpawner);
                                       // System.out.print("\n\n\n Removed a loaded spawner... \n\n\n");
                                    }
                                }
                            }
                        }
                        SyncSpawners.chunk_queue_unload.remove(chunk);
                    }
                }

                }catch(InterruptedException e) {
                break;
            }
        }
    }
}