package me.felnstaren.espero.module.itemmodifiers.listener;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;

public class RespawnListener implements Listener {
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		EsperoPlayer player = Espero.PLAYERS.getPlayer(event.getPlayer());
		if(player.getRespawnInventory().size() == 0) return;
		
		ArrayList<ItemStack> items = player.getRespawnInventory();
		for(int i = 0; i < items.size(); i++) 
			event.getPlayer().getInventory().addItem(items.get(i));
		player.clearRespawnInventory();
	}

}
