package me.felnstaren.espero.module.itemmodifiers.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import me.felnstaren.espero.module.itemmodifiers.ItemModifier;

public class InventorySwapListener implements Listener {
	
	private static final InventoryAction[] cancellable = { 
			InventoryAction.MOVE_TO_OTHER_INVENTORY,
			InventoryAction.DROP_ALL_CURSOR,
			InventoryAction.DROP_ALL_SLOT,
			InventoryAction.DROP_ONE_CURSOR,
			InventoryAction.DROP_ONE_SLOT
	};
	
	@EventHandler
	public void onInventorySwap(InventoryClickEvent event) {
		for(InventoryAction action : cancellable) {
			if(event.getAction() == action) {
				if(ItemModifier.SOULBOUND.isModifiedWith(event.getCurrentItem())) event.setCancelled(true);
				return;
			}
		}
		
		if(event.getClickedInventory() == null) return;
		if(event.getClickedInventory().getType() != InventoryType.PLAYER) {
			if(ItemModifier.SOULBOUND.isModifiedWith(event.getCursor())) event.setCancelled(true);
		}
	}

}
