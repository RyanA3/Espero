package me.felnstaren.espero.module.nations.claim;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.chat.Color;

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
		
		if(data == null || region.getLocalOwner(data.owner()) == null) return null;

		Espero.LOGGER.stream("Get chunk (" + x + "," + z + ") -> " + region.getLocalOwnerType(data.owner()).name());
		
		return new ClaimChunk(x, z, region.getLocalOwner(data.owner()), region.getLocalOwnerType(data.owner())); 
	}
	
	public ClaimData getRawClaim(int x, int z) {
		ClaimRegion region = getRegion(x, z);
		return region.getClaim(x, z);
	}
	
	//Checks if a specified chunk is claimed by a specified owner (nation or town)
	public boolean isOwnedBy(int x, int z, UUID owner) {
		ClaimChunk claim = getClaim(x, z);
		return claim != null && claim.owner.equals(owner);
	}
	//Checks if a specified chunk is claimed by a non-descript town
	public boolean isTown(int x, int z)   {
		ClaimChunk claim = getClaim(x, z);
		return claim != null && claim.owner_type == OwnerType.TOWN;
	}
	//Checks if a specified chunk is claimed by a non-descript nation
	public boolean isNation(int x, int z) {
		ClaimChunk claim = getClaim(x, z);
		return claim != null && claim.owner_type == OwnerType.NATION;
	}
	//Checks if a specified chunk is adjacent to one owned by the specified owner
	public boolean isAdjacent(int x, int z, UUID owner) {
		return isOwnedBy(x+1, z, owner) 
				|| isOwnedBy(x-1, z, owner)
				|| isOwnedBy(x, z+1, owner)
				|| isOwnedBy(x, z-1, owner);
	}
	//Returns a count of the number of adjacent chunks owned by the specified owner
	public int countAdjacent(int x, int z, UUID owner) {
		int i = 0;
		if(isOwnedBy(x+1, z, owner)) i++;
		if(isOwnedBy(x-1, z, owner)) i++;
		if(isOwnedBy(x, z+1, owner)) i++;
		if(isOwnedBy(x, z-1, owner)) i++;
		return i;
	}
	
	/**
	 * Sets the claimed chunk at the specified chunk coordinates
	 * @param owner Nation or Town to claim for
	 * @param x The x Chunk Coordinate
	 * @param z The z Chunk Coordinate
	 */
	public void claim(int x, int z, UUID owner) {
		ClaimRegion region = getRegion(x, z);
		region.claim(x, z, owner);
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
		Espero.LOGGER.debug("Saving all claim regions...");
		for(ClaimRegion region : loaded_regions) {
			Espero.LOGGER.stream("Saving claim region " + region.x() + "x " + region.z() + "z...");
			region.save();
		}
	}
	
	public void unload() {
		Espero.LOGGER.debug("Unloading all claim regions...");
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
	
	
	
	public static String map(int cx, int cz, Color direction) {
		String map = "/===========\\\n";
		for(int offz = -5; offz <= 5; offz++) {
			map += "||";
			for(int offx = -5; offx <= 5; offx++) {
				if(offz == 0 && offx == 0) { map += direction; continue; }
				ClaimData claim = ClaimBoard.inst().getRawClaim(cx + offx, cz + offz);
				if(claim == null) map += "-";
				else map += "#";
			}
			map += "||\n";
		}
		map += "\\===========/";
		return map;
	}
	
}
