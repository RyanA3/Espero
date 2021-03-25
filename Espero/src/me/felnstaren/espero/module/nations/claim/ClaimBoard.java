package me.felnstaren.espero.module.nations.claim;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.module.nations.nation.Nations;

public class ClaimBoard {

	private static ClaimBoard INSTANCE;
	
	public static void init() {
		INSTANCE = new ClaimBoard();
	}
	
	public static ClaimBoard inst() {
		return INSTANCE;
	}
	
	
	
	private ArrayList<ClaimRegion> loaded_regions;
	
	private ClaimBoard() {
		loaded_regions = new ArrayList<ClaimRegion>();
	}
	
	/**
	 * Gets the claimed chunk (if any) at the specified chunk coordinates
	 * @param x The x Chunk Coordinate
	 * @param z The z Chunk Coordinate
	 * @return
	 */
	public ClaimChunk getClaim(int x, int z) {
		ClaimRegion region = getRegion(x, z);
		ClaimData data = region.getClaim(x, z);
		
		if(data == null) return null;
		UUID nation = region.getRelativeNation(data.nation());
		if(Nations.inst().getNation(nation) == null) return null;
		
		return new ClaimChunk(x, z, region.getRelativeNation(data.nation()), data.town()); 
	}
	
	/**
	 * Sets the claimed chunk at the specified chunk coordinates
	 * @param nation Nation to claim for
	 * @param x The x Chunk Coordinate
	 * @param z The z Chunk Coordinate
	 * @param town Type of claim
	 */
	public void claim(int x, int z, UUID nation, int town) {
		ClaimRegion region = getRegion(x, z);     
		region.claim(x, z, nation, town);
	}
	
	/**
	 * Removes the claimed chunk at the specified chunk coordinates
	 * @param x The x Chunk Coordinate
	 * @param z The z Chunk Coordinate
	 */
	public void unclaim(int x, int z) {
		ClaimRegion region = getRegion(x, z);
		region.unclaim(x, z);
	}
	
	
	
	
	public void save() {
		for(ClaimRegion region : loaded_regions)
			region.save();
	}
	
	public void unload() {
		save();
		loaded_regions.clear();
	}
	
	public ClaimRegion getRegion(int x, int z) {
		if(x < 0) x -= 31; if(z < 0) z -= 31;           //Offset by 31 if it's negative, so for example, chunk (-5, -5) will be region -1, -1. Otherwise it'd be considered region 0, 0 because -5 / 32 = 0
		x /= ClaimRegion.WIDTH; z /= ClaimRegion.HEIGH; //Divide to get into Region Coords
		
		for(int i = 0; i < loaded_regions.size(); i++) {
			ClaimRegion check = loaded_regions.get(i);
			if(check.x() == x && check.z() == z)
				return loaded_regions.get(i);
		}
		
		return loadRegion(x, z);
	}
	
	private ClaimRegion loadRegion(int x, int z) {
		ClaimRegion region = new ClaimRegion(x, z);
		loaded_regions.add(region);
		return region;
	}
	
}
