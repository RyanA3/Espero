package me.felnstaren.espero.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class WorldUtil {

	public static Location findHighestBlock(World world, int x, int z) {
		for(int y = 255; y > 0; y--) {
			if(isIgnore(world.getBlockAt(x, y, z).getType())) continue;
			return new Location(world, x, y, z);
		}
		return new Location(world, x, 0, z);
	}
	
	private static boolean isIgnore(Material material) {
		if(material == Material.AIR) return true;
		if(material.name().contains("GLASS")) return true;
		return false;
	}
	
}
