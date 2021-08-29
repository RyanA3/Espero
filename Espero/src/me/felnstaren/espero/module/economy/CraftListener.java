package me.felnstaren.espero.module.economy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.logger.Level;

public class CraftListener implements Listener {
	
	@EventHandler
	public void onCraft(CraftItemEvent event) {
		int slotcount = 0;
		if(event.getClickedInventory().getType() == InventoryType.WORKBENCH) slotcount = 9;
		else if(event.getClickedInventory().getType() == InventoryType.CRAFTING) slotcount = 4;
		else return;
		Espero.LOGGER.log(Level.DEBUG, "Craft Event");
		
		int eco_items = 0;
		//int non_items = 0;
		int norm_items = 0;
		ItemStack[] cslots = new ItemStack[slotcount];
		for(int i = 0; i < slotcount; i++) {
			cslots[i] = event.getClickedInventory().getItem(i+1);
			if(cslots[i] == null); //non_items++;
			else if(Economy.isEconomyItem(cslots[i])) eco_items++;
			else norm_items++;
			//Espero.LOGGER.log(Level.DEBUG, i + cslots[i].getType().name());
		}
		
		
		if(eco_items == 0) {
			return;
		}
		
		if(event.isShiftClick()) event.setCancelled(true);
		
		if(eco_items == 1 && norm_items == 0) {
			event.setCurrentItem(Economy.singular(9));
			return;
		}
		
		if(eco_items == 9) {
			event.setCurrentItem(Economy.compacted(1));
			return;
		}
		
		event.setCancelled(true);
	}
	
	
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(event.getPlayer() == null) return;
		if(!Economy.isEconomyItem(event.getItemInHand())) return;
		event.setCancelled(true);
	}
	
}
