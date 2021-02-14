package me.felnstaren.espero.module.morph.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.felnstaren.espero.module.morph.MorphManager;

public class MorphPlayerListener implements Listener {
	
	private MorphManager mman;
	
	public MorphPlayerListener(MorphManager mman) {
		this.mman = mman;
	}
	
	
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		mman.removeMorph(event.getPlayer());
	}
	
}
