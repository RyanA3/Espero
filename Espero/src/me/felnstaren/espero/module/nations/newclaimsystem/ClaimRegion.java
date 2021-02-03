package me.felnstaren.espero.module.nations.newclaimsystem;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.logger.Level;

public class ClaimRegion {
	
	public static final int WIDTH = 32;
	public static final int HEIGH = 32;
	
	private int x;
	private int z;
	private ArrayList<ClaimData> claims;
	private ArrayList<UUID> nations;
	private String path;
	
	public ClaimRegion(int x, int z) {
		this.x = x;
		this.z = z;
		
		claims = new ArrayList<ClaimData>();
		nations = new ArrayList<UUID>();
		path = "/chunkdata/" + x + "x" + z + "z.txt";
		
		String data = Espero.LOADER.readString(path, null);
		if(data.equals("")) return;
		
		String[] line = data.split("\\n");
		if(line.length < 2) return;
		
		String[] unparsed_nations = line[0].split(",");
		for(String unparsed_nation : unparsed_nations)
			nations.add(UUID.fromString(unparsed_nation));
		
		String[] unparsed_chunks = line[1].split(",");
		for(String unparsed_chunk : unparsed_chunks)
			claims.add(new ClaimData(unparsed_chunk));
	}
	
	
	
	public ClaimData getClaim(int x, int z) {
		int offx = Math.abs(x) % WIDTH; int offz = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		Espero.LOGGER.log(Level.DEBUG, "Chunk(" + x + "," + z + ") -> (" + offx + "," + offz + ") -> " + (offz * WIDTH + offx));
		return getClaim(offz * WIDTH + offx);
	}
	
	public ClaimData getClaim(int location) {
		for(ClaimData c : claims)
			if(c.location() == location) return c;
		return null;
	}
	
	public void claim(int x, int z, UUID nation, int town) {
		x = Math.abs(x) % WIDTH; z = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		claim(z * WIDTH + x, nation, town);
	}
	
	public void claim(int location, UUID nation, int town) {
		if(!nations.contains(nation)) nations.add(nation);
		int native_nation_id = nations.indexOf(nation);
		
		ClaimData chunk = getClaim(location);
		if(chunk == null) 
			claims.add(new ClaimData(location, native_nation_id, town));
		else {
			chunk.setNation(nations.indexOf(nation));
			chunk.setTown(town);
		}
	}
	
	public void unclaim(int x, int z) {
		x = Math.abs(x) % WIDTH; z = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		unclaim(z * WIDTH + x);
	}
	
	public void unclaim(int location) {
		int nation_hits = 0;
		ClaimData chunk = getClaim(location);
		
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
		for(int i = 0; i < nations.size(); i++) {
			if(i > 0) data += ",";
			data += nations.get(i).toString();
		}
		data += "\n";
		
		for(int i = 0; i < claims.size(); i++) {
			if(i > 0) data += ",";
			data += claims.get(i).data();
		}

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
	
	protected UUID getRelativeNation(int nation) {
		return nations.get(nation);
	}
	
	public String map(int px, int pz) {
		String map = "";
		px = Math.abs(px); pz = Math.abs(pz);
		for(int offx = 0; offx < WIDTH; offx++) {
			for(int offz = 0; offz < HEIGH; offz++) {
				
				ClaimData data = getClaim(offx, offz);
				if(offx == px && offz == pz) map += "+";
				else if(data == null) map += "-";
				else map += "#";
				Espero.LOGGER.log(Level.DEBUG, "Chunk at (" + (offx + (x * WIDTH)) + "," + (offz + (z * HEIGH)) + "): " + data == null ? data.nation() + "" : "none");
			}
			map += "\n";
		}
		map += "\n region(" + x + "," + z + ")"; 
		return map;
	}

}
