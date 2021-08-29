package me.felnstaren.espero.module.itemmodifiers.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.itemmodifiers.ItemModifier;

public class DeathListener implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		List<ItemStack> drops = event.getDrops();
		ArrayList<ItemStack> new_drops = new ArrayList<ItemStack>();
		EsperoPlayer player = Espero.PLAYERS.getPlayer(event.getEntity());
		
		player.clearRespawnInventory();
		for(ItemStack drop : drops) {
			if(ItemModifier.SOULBOUND.isModifiedWith(drop)) player.addToRespawnInventory(drop);
			else new_drops.add(drop);
		}
		
		event.getDrops().clear();
		event.getDrops().addAll(new_drops);
	}
	
}
