package me.felnstaren.espero.module.nations.system;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.module.nations.claim.ClaimChunkData;
import me.felnstaren.espero.module.nations.claim.ClaimRegion;

public class Board {
	
	private static Board INSTANCE;
	
	public static void init() {
		INSTANCE = new Board();
	}
	
	public static Board getInstance() {
		return INSTANCE;
	}
	
	
	
	private ArrayList<ClaimRegion> loaded_regions;
	
	private Board() {
		loaded_regions = new ArrayList<ClaimRegion>();
	}
	
	/**
	 * Gets the claimed chunk (if any) at the specified chunk coordinates
	 * @param x The x Chunk Coordinate
	 * @param z The z Chunk Coordinate
	 * @return
	 */
	public ClaimChunkData getClaim(int x, int z) {
		return getRegion(x / 32, z / 32).getClaim(x, z);    //32 chunks in a region
	}
	
	/**
	 * Sets the claimed chunk at the specified chunk coordinates
	 * @param nation Nation to claim for
	 * @param x The x Chunk Coordinate
	 * @param z The z Chunk Coordinate
	 * @param id Type of claim
	 */
	public void claim(UUID nation, int x, int z, int id) {
		ClaimRegion region = getRegion(x / 32, z / 32);     //32 chunks in a region
		region.claim(nation, x, z, id);
	}
	
	/**
	 * Removes the claimed chunk at the specified chunk coordinates
	 * @param x The x Chunk Coordinate
	 * @param z The z Chunk Coordinate
	 */
	public void unclaim(int x, int z) {
		ClaimRegion region = getRegion(x / 32, z / 32);
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
	
	private ClaimRegion getRegion(int x, int z) {
		for(int i = 0; i < loaded_regions.size(); i++) {
			ClaimRegion check = loaded_regions.get(i);
			if(check.getX() == x && check.getZ() == z)
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
