package me.felnstaren.espero.module.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.felib.item.util.InventoryEditor;
import me.felnstaren.felib.item.util.ItemBuild;

public class Economy {
	
	private static final ItemStack ECO_ITEM_SINGULAR = new ItemBuild(Material.EMERALD, 1).setName("&aMynt").setFlag("currency", true).construct();
	private static final ItemStack ECO_ITEM_COMPACTED = new ItemBuild(Material.EMERALD_BLOCK, 1).setName("&aMynt").setFlag("currency", true).construct();
	
	public static int balance(Player player) {
		return InventoryEditor.countItems(player.getInventory(), ECO_ITEM_SINGULAR);
	}
	
	public static void withdraw(Player player, int amount) {
		InventoryEditor.remove(player.getInventory(), ECO_ITEM_SINGULAR, amount);
	}
	
	public static void deposit(Player player, int amount) {
		InventoryEditor.add(player.getInventory(), ECO_ITEM_SINGULAR, amount, true);
	}
	
}
