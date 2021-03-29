package me.felnstaren.espero.module.economy;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.item.util.InventoryEditor;
import me.felnstaren.felib.item.util.ItemBuild;
import me.felnstaren.felib.logger.Level;

public class Economy {
	
	public static final ItemStack ECO_ITEM_SINGULAR = new ItemBuild(Material.EMERALD, 1).setName("&aMynt").setFlag("currency", true).construct();
	public static final ItemStack ECO_ITEM_COMPACTED = new ItemBuild(Material.EMERALD_BLOCK, 1).setName("&aMynt").setFlag("currency", true).construct();
	
	public static int balance(Player player) {
		return InventoryEditor.countItems(player.getInventory(), ECO_ITEM_SINGULAR) + (InventoryEditor.countItems(player.getInventory(), ECO_ITEM_COMPACTED) * 9);
	}
	
	public static void withdraw(Player player, int amount) {
		int singles_held = InventoryEditor.countItems(player.getInventory(), ECO_ITEM_SINGULAR);
		int nines_held = InventoryEditor.countItems(player.getInventory(), ECO_ITEM_COMPACTED);
		int singles_to_remove = amount % 9;
		int nines_to_remove = amount / 9;
		int nines_to_split = 0;
		
		if(nines_to_remove > nines_held) {
			singles_to_remove += (nines_to_remove - nines_held) * 9;
			nines_to_remove = nines_held;
		}
		
		if(singles_to_remove > singles_held) {
			nines_to_split = (int) Math.ceil((singles_to_remove - singles_held) / 9.0);
		}
		
		if(nines_to_split > 0) {
			Espero.LOGGER.log(Level.DEBUG, "Remove " + nines_to_split + " nines");
			InventoryEditor.remove(player.getInventory(), ECO_ITEM_COMPACTED, nines_to_split);
			Espero.LOGGER.log(Level.DEBUG, "Add " + nines_to_split * 9 + " singles");
			InventoryEditor.add(player.getInventory(), ECO_ITEM_SINGULAR, nines_to_split * 9, false);
		}
		
		Espero.LOGGER.log(Level.DEBUG, "Remove " + nines_to_remove + " nines");
		InventoryEditor.remove(player.getInventory(), ECO_ITEM_COMPACTED, nines_to_remove);
		Espero.LOGGER.log(Level.DEBUG, "Remove " + singles_to_remove + " singles");
		InventoryEditor.remove(player.getInventory(), ECO_ITEM_SINGULAR, singles_to_remove);
	}
	
	public static void deposit(Player player, int amount) {
		InventoryEditor.add(player.getInventory(), money(amount), true);
	}
	
	public static boolean isEconomyItem(ItemStack item) {
		return InventoryEditor.isSimilar(item, ECO_ITEM_SINGULAR) || InventoryEditor.isSimilar(item, ECO_ITEM_COMPACTED);
	}
	
	public static ArrayList<ItemStack> money(int amount) {
		int compact_items = amount / 9;
		int compact_stacks = compact_items / 64;
		int single_items = amount % 9;
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		ItemStack singular = ECO_ITEM_SINGULAR.clone();
		singular.setAmount(single_items);
		items.add(singular);
		
		for(int i = 0; i < compact_stacks; i++) {
			ItemStack stack = ECO_ITEM_COMPACTED.clone();
			stack.setAmount(64);
			items.add(stack);
		}
		
		ItemStack compact = ECO_ITEM_COMPACTED.clone();
		compact.setAmount(compact_items % 64);
		items.add(compact);
		
		return items;
	}
	
	public static ItemStack compacted(int amount) {
		ItemStack compact = ECO_ITEM_COMPACTED.clone();
		compact.setAmount(amount);
		return compact;
	}
	
	public static ItemStack singular(int amount) {
		ItemStack singular = ECO_ITEM_SINGULAR.clone();
		singular.setAmount(amount);
		return singular;
	}
	
}
