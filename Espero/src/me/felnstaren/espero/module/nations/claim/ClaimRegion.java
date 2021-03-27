package me.felnstaren.espero.module.nations.claim;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.util.data.BinarySearchable;

public class ClaimRegion extends BinarySearchable<ClaimData> {
	
	public static final int WIDTH = 32;
	public static final int HEIGH = 32;
	
	private int x;
	private int z;
	//private ArrayList<ClaimData> claims;
	private ArrayList<UUID> nations;
	private String path;
	
	public ClaimRegion(int x, int z) {
		this.x = x;
		this.z = z;
		
		//claims = new ArrayList<ClaimData>();
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
			/*claims.*/add(new ClaimData(unparsed_chunk));
		
		//Remove Deleted Nations
		for(int i = 0; i < nations.size(); i++) {
			if(Nations.inst().getNation(nations.get(i)) != null) continue;
			clear(nations.get(i)); i--;
		}
	}
	
	
	
	public ClaimData getClaim(int x, int z) {
		int offx = Math.abs(x) % WIDTH; int offz = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		//Espero.LOGGER.log(Level.DEBUG, "Chunk(" + x + "," + z + ") -> (" + offx + "," + offz + ") -> " + (offz * WIDTH + offx));
		return getClaim(offz * WIDTH + offx);
	}
	
	private ClaimData getClaim(int location) {
		return get(location);
		//for(ClaimData c : claims)
		//	if(c.location() == location) return c;
		//return null;
	}
	
	public void claim(int x, int z, UUID nation, int town) {
		x = Math.abs(x) % WIDTH; z = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		claim(z * WIDTH + x, nation, town);
	}
	
	private void claim(int location, UUID nation, int town) {
		if(!nations.contains(nation)) nations.add(nation);
		int native_nation_id = nations.indexOf(nation);
		
		ClaimData chunk = getClaim(location);
		if(chunk == null) 
			/*claims.*/add(new ClaimData(location, native_nation_id, town));
		else {
			chunk.setNation(native_nation_id);
			chunk.setTown(town);
		}
	}
	
	public void unclaim(int x, int z) {
		x = Math.abs(x) % WIDTH; z = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		unclaim(z * WIDTH + x);
	}
	
	private void unclaim(int location) { //TODO: Dirty, doesn't remove nation if all nation's claims are gone
		remove(location);
		//int nation_hits = 0;
		//ClaimData chunk = getClaim(location);
		
		//for(int i = 0; i < claims.size(); i++) {
		//	if(claims.get(i).nation() == chunk.nation()) nation_hits++;
		//	if(claims.get(i).location() != location) continue;
		//	claims.remove(i);
		//	i--;
		//}
		
		//if(nation_hits > 1) return;
		//nations.remove(chunk.nation());
		//for(ClaimData shift : claims)
		//	if(shift.nation() > chunk.nation()) shift.setNation(shift.nation() - 1);
	}
	
	
	
	private String data() {
		String data = "";
		for(int i = 0; i < nations.size(); i++) {
			if(i > 0) data += ",";
			data += nations.get(i).toString();
		}
		data += "\n";
		
		for(int i = 0; i < values.size(); i++) {
			if(i > 0) data += ",";
			data += values.get(i).data();
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
	
	public void clear(UUID nation) {
		int index = nations.indexOf(nation);
		for(int i = 0; i < values.size(); i++) {
			if(values.get(i).nation() != index) { //If the claim isn't of the target nation
				if(values.get(i).nation() > index) //and is greater than the target nation 
					values.get(i).setNation(values.get(i).nation() - 1); //Then shift the nation if it's higher than this one's index
				continue; //and skip this value
			}
			values.remove(i); i--; //Otherwise remove it
		}
		nations.remove(index);
	}
	
	public String map(int px, int pz) {
		String map = "";
		px = Math.abs(px); pz = Math.abs(pz);
		for(int offz = 0; offz < HEIGH; offz++) {
			for(int offx = 0; offx < WIDTH; offx++) {
				
				ClaimData data = getClaim(offx, offz);
				if(offx == px && offz == pz) map += "+";
				else if(data == null) map += "-";
				else map += "#";
				//Espero.LOGGER.log(Level.DEBUG, "Chunk at (" + (offx + (x * WIDTH)) + "," + (offz + (z * HEIGH)) + "): " + data == null ? data.nation() + "" : "none");
			}
			map += "\n";
		}
		map += "\n region(" + x + "," + z + ")"; 
		return map;
	}

}
