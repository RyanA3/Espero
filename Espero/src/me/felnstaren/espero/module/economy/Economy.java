package me.felnstaren.espero.module.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.felib.item.util.InventoryEditor;
import me.felnstaren.felib.item.util.ItemBuild;

public class Economy {
	
	private static final ItemStack ECO_ITEM = new ItemBuild(Material.EMERALD, 1).setName("&aMynt").setFlag("currency", true).construct();

	public static int balance(Player player) {
		return InventoryEditor.countItems(player.getInventory(), ECO_ITEM);
	}
	
	public static void withdraw(Player player, int amount) {
		InventoryEditor.remove(player.getInventory(), ECO_ITEM, amount);
	}
	
	public static void deposit(Player player, int amount) {
		InventoryEditor.add(player.getInventory(), ECO_ITEM, amount, true);
	}
	
}
