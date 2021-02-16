package me.felnstaren.espero.module.morph.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.felnstaren.espero.module.morph.MorphManager;
import me.felnstaren.espero.module.morph.morph.EntityMorph;

public class MorphPlayerListener implements Listener {
	
	private MorphManager mman;
	
	public MorphPlayerListener(MorphManager mman) {
		this.mman = mman;
	}
	
	
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		EntityMorph morph = mman.getMorphByPlayer(event.getPlayer().getEntityId());
		if(morph == null) return;
		mman.unmorph(morph);
	}
	
}
