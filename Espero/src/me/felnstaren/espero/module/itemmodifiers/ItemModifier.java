package me.felnstaren.espero.module.itemmodifiers;

import org.bukkit.inventory.ItemStack;

import other.bananapuncher714.nbt.NBTEditor;

public enum ItemModifier {

	SOULBOUND("soulbound");
	
	private String tag;
	
	private ItemModifier(String tag) {
		this.tag = tag;
	}
	
	public ItemStack modify(ItemStack item) {
		return NBTEditor.set(item, tag, "modifier" + this.ordinal());
	}
	
	public boolean isModifiedWith(ItemStack item) {
		return NBTEditor.getString(item, "modifier" + this.ordinal()) != null;
	}
	
}
