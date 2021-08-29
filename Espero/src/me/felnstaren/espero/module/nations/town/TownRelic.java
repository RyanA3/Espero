package me.felnstaren.espero.module.nations.town;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.config.Option;
import me.felnstaren.felib.item.util.ItemBuild;
import other.bananapuncher714.nbt.NBTEditor;

public class TownRelic {

	private Entity ender_crystal;
	private double x, y, z;
	private World world;
	private Town town;
	
	public TownRelic(Town town) { 
		this.town = town; 
		this.world = Bukkit.getWorlds().get(0);
	};
	public TownRelic(Town town, double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = Bukkit.getWorlds().get(0);
		this.town = town;
	}
	public TownRelic(Town town, String data) {
		String[] values = data.split(",");
		this.x = Double.parseDouble(values[0]);
		this.y = Double.parseDouble(values[1]);
		this.z = Double.parseDouble(values[2]);
		this.world = Bukkit.getWorld(values[3]);
		if(values.length > 4) ender_crystal = Bukkit.getEntity(UUID.fromString(values[4]));
		
		this.town = town;
	}
	
	
	
	public ItemStack item() {
		return new ItemBuild(Material.END_CRYSTAL)
				.setFlag("modifier0", "SOULBOUND")
				.setFlag("town_relic", town.getName())
				.setLore("&e&oSoulbound")
				.setName("&6" + town.getDisplayName() + "'s Relic")
				.construct();
	}
	
	public boolean isHolding(Player player) {
		for(ItemStack s : player.getInventory().getContents()) {
			if(s == null) continue;
			String tag = NBTEditor.getString(s, "town_relic");
			if(tag != null && tag.equals(town.getName())) return true;
		}
		return false;
	}
	
	public boolean exists() {
		if(ender_crystal != null && ender_crystal.isDead()) ender_crystal = null;
		return ender_crystal != null; 
	}
	public void setLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void teleport(double x, double y, double z) {
		setLocation(x, y, z);
		if(ender_crystal != null) ender_crystal.teleport(new Location(Bukkit.getWorld(Option.DEFAULT_WORLD), x, y, z));
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public int getChunkX() {
		return (int) (x / 16);
	}
	public int getChunkZ() {
		return (int) (z / 16);
	}
	
	public void destroy() {
		if(ender_crystal == null) return;
		ender_crystal.remove();
		ender_crystal = null;
	}
	
	public void spawn() {
		if(ender_crystal != null) destroy();
		ender_crystal = world.spawnEntity(new Location(world, x, y, z), EntityType.ENDER_CRYSTAL);
		ender_crystal.setCustomName(town.getName());
	}
	
	public String data() {
		String data = x + "," + y + "," + z + "," + world.getName();
		if(exists() && !ender_crystal.isDead()) data += "," + ender_crystal.getUniqueId().toString(); 
		return data;
	}

}
