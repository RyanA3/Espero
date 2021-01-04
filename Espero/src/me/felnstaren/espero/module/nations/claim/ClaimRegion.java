package me.felnstaren.espero.module.nations.claim;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.nations.system.Nations;

public class ClaimRegion {

	private String path;
	private HashMap<UUID, NationRegionClaims> claims;
	private int x;
	private int z;
	
	public ClaimRegion(int x, int z) {
		claims = new HashMap<UUID, NationRegionClaims>();
		path = "/chunkdata/" + x + "x" + z + "z.txt";
		
		String data = Espero.LOADER.readData(path, null);
		if(data.equals("")) return;
		
		String[] nations = data.split("\n");
		for(int i = 0; i < nations.length; i++) {
			UUID nation = UUID.fromString(data.split(",")[0]);
			if(Nations.getInstance().getNation(nation) == null) continue;
			claims.put(nation, new NationRegionClaims(nations[i]));
		}
	}
	
	
	
	public ClaimChunkData getClaim(int x, int z) {
		x %= 32; z %= 32;
		for(UUID nation : claims.keySet()) {
			if(Nations.getInstance().getNation(nation) == null) continue;
			ClaimChunk claim = claims.get(nation).getClaim(x, z);
			if(claim != null) return claim.data(nation);
		}
		return null;
	}
	
	public void claim(UUID nation, int x, int z, int id) {
		x %= 32; z %= 32;
		if(!claims.containsKey(nation)) claims.put(nation, new NationRegionClaims());
		claims.get(nation).claim(x, z, id);
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
	
	private String data() {
		String data = "";
		for(UUID nation : claims.keySet()) {
			data += nation.toString();
			for(ClaimChunk chunk : claims.get(nation).getClaims()) 
				data += "," + chunk.data();
			data += "\n";
		}
		
		return data;
	}

	public void save() {
		File file = Espero.LOADER.load(path);
		Espero.LOADER.save(data(), file);
	}
	
}
