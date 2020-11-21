package me.felnstaren.espero.module.nations.system;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.claim.ClaimChunkData;
import me.felnstaren.espero.module.nations.claim.ClaimRegion;
import me.felnstaren.espero.util.math.Vec2d;

public class Board {
	
	private static Board INSTANCE;
	
	public static Board getInstance() {
		if(INSTANCE == null) INSTANCE = new Board();
		return INSTANCE;
	}
	
	
	
	private ArrayList<ClaimRegion> loaded_regions;
	private ArrayList<Vec2d> loaded_regions_locations;
	
	
	public ClaimChunkData getClaim(int x, int z) {
		return getRegion(x / 32, z / 32).getClaim(x, z);
	}
	
	public void claim(UUID nation, int x, int z, int id) {
		ClaimRegion region = getRegion(x / 32, z / 32);
		region.claim(nation, new ClaimChunk(x, z, id));
	}
	
	public void unclaim(int x, int z) {
		ClaimRegion region = getRegion(x / 32, z / 32);
		region.unclaim(x, z);
	}
	
	public void save() {
		for(ClaimRegion region : loaded_regions)
			region.save();
	}
	
	public void clear() {
		loaded_regions.clear();
		loaded_regions_locations.clear();
	}
	
	private ClaimRegion getRegion(int x, int z) {
		for(int i = 0; i < loaded_regions_locations.size(); i++) {
			Vec2d check = loaded_regions_locations.get(i);
			if(check.x == x && check.y == z)
				return loaded_regions.get(i);
		}
		
		return loadRegion(x, z);
	}
	
	private ClaimRegion loadRegion(int x, int z) {
		ClaimRegion region = new ClaimRegion(x, z);
		loaded_regions.add(region);
		loaded_regions_locations.add(new Vec2d(x, z));
		return region;
	}

}
