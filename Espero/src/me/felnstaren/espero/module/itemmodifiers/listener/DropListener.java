package me.felnstaren.espero.module.itemmodifiers.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.felnstaren.espero.module.itemmodifiers.ItemModifier;

public class DropListener implements Listener {
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(!ItemModifier.SOULBOUND.isModifiedWith(event.getItemDrop().getItemStack())) return;
		event.setCancelled(true);
	}

}
