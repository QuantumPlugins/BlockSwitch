package me.quantum.blockswitch;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

public class WorldListener implements Listener {

	public static BlockSwitch plugin;
	
	public WorldListener(BlockSwitch blockSwitch) {
		plugin = blockSwitch;
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		plugin.stripChunk(event.getChunk());
	}
	
	@EventHandler
	public void onChunkPopulated(ChunkPopulateEvent event) {
		plugin.stripChunk(event.getChunk());
	}
	
}
