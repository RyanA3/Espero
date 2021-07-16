package me.felnstaren.espero.module.acoustic.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.module.acoustic.item.AbstractInstrument;
import me.felnstaren.felib.item.custom.CustomMaterial;
import me.felnstaren.felib.item.custom.CustomMaterialData;

public class InstrumentListener implements Listener {

	@EventHandler
	public void execute(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInOffHand();
		if(item == null) return;
		
		CustomMaterialData mat = CustomMaterial.inst().as(item);
		if(mat == null) return;
		if(!(mat instanceof AbstractInstrument)) return;
		((AbstractInstrument) mat).execute(event);
	}
	
}
