package me.felnstaren.espero.module.nations.newclaimsystem;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;

public class ClaimRegion {
	
	public static final int WIDTH = 32;
	public static final int HEIGH = 32;
	
	private int x;
	private int z;
	private ArrayList<ClaimChunk> claims;
	private ArrayList<UUID> nations;
	private String path;
	
	public ClaimRegion(int x, int z) {
		this.x = x;
		this.z = z;
		
		claims = new ArrayList<ClaimChunk>();
		path = "/chunkdata/" + x + "x" + z + "z.txt";
		
		String data = Espero.LOADER.readString(path, null);
		if(data.equals("")) return;
		
		String[] line = data.split("\\n");
		
		String[] unparsed_nations = line[0].split(",");
		for(String unparsed_nation : unparsed_nations)
			nations.add(UUID.fromString(unparsed_nation));
		
		String[] unparsed_chunks = line[1].split(",");
		for(String unparsed_chunk : unparsed_chunks)
			claims.add(new ClaimChunk(unparsed_chunk));
	}
	
	
	
	public ClaimChunk getClaim(int x, int z) {
		x %= WIDTH; z %= HEIGH;
		return getClaim(z * WIDTH + x);
	}
	
	public ClaimChunk getClaim(int location) {
		for(ClaimChunk c : claims)
			if(c.location() == location) return c;
		return null;
	}
	
	public void claim(int x, int z, UUID nation, int town) {
		x %= WIDTH; z %= HEIGH;
		claim(z * WIDTH + x, nation, town);
	}
	
	public void claim(int location, UUID nation, int town) {
		if(!nations.contains(nation)) nations.add(nation);
		int native_nation_id = nations.indexOf(nation);
		
		ClaimChunk chunk = getClaim(location);
		if(chunk == null) 
			claims.add(new ClaimChunk(location, native_nation_id, town));
		else {
			chunk.setNation(nations.indexOf(nation));
			chunk.setTown(town);
		}
	}
	
	public void unclaim(int x, int z) {
		x %= WIDTH; z %= HEIGH;
		unclaim(z * WIDTH + x);
	}
	
	public void unclaim(int location) {
		int nation_hits = 0;
		ClaimChunk chunk = getClaim(location);
		
		for(int i = 0; i < claims.size(); i++) {
			if(claims.get(i).nation() == chunk.nation()) nation_hits++;
			if(claims.get(i).location() != location) continue;
			claims.remove(i);
			i--;
		}
		
		if(nation_hits > 1) return;
		nations.remove(chunk.nation());
	}
	
	
	
	private String data() {
		String data = "";
		for(UUID nation : nations)
			data += nation.toString() + ",";
		data = data.substring(0, data.length() - 2);
		data += "\n";
		
		for(ClaimChunk claim : claims) 
			data += claim.data() + ",";
		data = data.substring(0, data.length() - 2);
		return data;
	}
	
	public void save() {
		Espero.LOADER.save(path, data());
	}
	
	
	
	public int x() {
		return x;
	}
	
	public int z() {
		return z;
	}

}
