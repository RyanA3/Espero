package me.felnstaren.espero.module.nations.claim;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import me.felnstaren.espero.config.Loader;

public class ClaimRegion {

	private String path;
	private HashMap<UUID, NationRegionClaims> claims;
	private int x;
	private int z;
	
	public ClaimRegion(int x, int z) {
		claims = new HashMap<UUID, NationRegionClaims>();
		path = "/chunkdata/" + x + "x" + z + "z.txt";
		
		String data = Loader.readData(path, null);
		String[] nations = data.split("\n");
		for(int i = 0; i < nations.length; i++) {
			UUID nation = UUID.fromString(data.split(",")[0]);
			claims.put(nation, new NationRegionClaims(nations[i]));
		}
	}
	
	
	
	public NationRegionClaims getClaims(UUID nation) {
		return claims.get(nation);
	}
	
	public UUID getWhoClaimed(int x, int z) {
		x %= 32; z %= 32;
		for(UUID nation : claims.keySet())
			if(claims.get(nation).isClaimed(x, z)) 
				return nation;
		return null;
	}
	
	public ClaimChunkData getClaim(int x, int z) {
		x %= 32; z %= 32;
		for(UUID nation : claims.keySet())
			if(claims.get(nation).isClaimed(x, z)) 
				return claims.get(nation).getClaim(x, z).data(nation);
		return null;
	}
	
	public void claim(UUID nation, ClaimChunk claim) {
		x %= 32; z %= 32;
		if(!claims.containsKey(nation)) claims.put(nation, new NationRegionClaims());
		claims.get(nation).claim(claim);
	}
	
	public void unclaim(int x, int z) {
		x %= 32; z %= 32;
		for(UUID nation : claims.keySet())
			claims.get(nation).unclaim(x, z);
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	
	public String path() {
		return path;
	}
	
	public String data() {
		String data = "";
		for(UUID nation : claims.keySet()) {
			data += nation.toString();
			for(ClaimChunk chunk : claims.get(nation).getClaims()) 
				data += "," + chunk.data();
			data += "\n";
		}
		
		return data;
	}
	
	//EZ Dub
	public void save() {
		File file = Loader.load(path);
		Loader.save(data(), file);
	}
	
}
