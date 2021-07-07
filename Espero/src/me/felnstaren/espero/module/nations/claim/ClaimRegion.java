package me.felnstaren.espero.module.nations.claim;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.util.data.BinarySearchable;

public class ClaimRegion extends BinarySearchable<ClaimData> {
	
	public static final int WIDTH = 32;
	public static final int HEIGH = 32;
	
	private int x;
	private int z;
	private ArrayList<UUID> owners; //Towns & Nations owning a claim in this region
	private ArrayList<OwnerType> owner_types;
	private String path;
	
	public ClaimRegion(int x, int z) {
		this.x = x;
		this.z = z;
		
		//claims = new ArrayList<ClaimData>();
		owners = new ArrayList<UUID>();
		owner_types = new ArrayList<OwnerType>();
		path = "/chunkdata/" + x + "x" + z + "z.txt";
		
		String data = Espero.LOADER.readString(path, null);
		if(data.equals("")) return;
		
		String[] line = data.split("\\n");
		if(line.length < 2) return;
		
		String[] unparsed_owners = line[0].split(",");
		for(String unparsed_owner : unparsed_owners) {
			if(unparsed_owner.startsWith("t")) owner_types.add(OwnerType.TOWN);
			else 							   owner_types.add(OwnerType.NATION);
			owners.add(UUID.fromString(unparsed_owner.substring(1, unparsed_owner.length())));
		}
		
		String[] unparsed_chunks = line[1].split(",");
		for(String unparsed_chunk : unparsed_chunks)
			/*claims.*/add(new ClaimData(unparsed_chunk));
		
		//Remove Deleted Nations & Towns
		for(int i = 0; i < owners.size(); i++) {
			if(NationRegistry.inst().getNation(owners.get(i)) != null) continue;
			if(TownRegistry  .inst().getTown  (owners.get(i)) != null) continue;
			clear(owners.get(i)); i--;
		}
	}
	
	
	
	public  ClaimData getClaim(int x, int z) {
		int offx = Math.abs(x) % WIDTH; int offz = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		return getClaim(offz * WIDTH + offx);
	}
	private ClaimData getClaim(int location) {
		return get(location); 
	}
	
	public  void claim    (int x, int z, UUID owner) {
		x = Math.abs(x) % WIDTH; z = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		claim(z * WIDTH + x, owner);
	}
	private void claim    (int location, UUID owner) {
		if(!owners.contains(owner)) {
			owners.add(owner);
			if(NationRegistry.inst().getNation(owner) == null) owner_types.add(OwnerType.TOWN);
			else											   owner_types.add(OwnerType.NATION);
		}
		int native_owner_id = owners.indexOf(owner);
		
		ClaimData chunk = getClaim(location);
		if(chunk == null) 
			add(new ClaimData(location, native_owner_id));
		else 
			chunk.setOwner(native_owner_id);
	}
	
	public  void unclaim  (int x, int z) {
		x = Math.abs(x) % WIDTH; z = Math.abs(z) % HEIGH;  //Modulo to get into relative chunk coords
		unclaim(z * WIDTH + x);
	} 
	private void unclaim  (int location) { 
		remove(location); 
	}
	
	public    int   x()						 { return x; }
	public    int   z() 					 { return z; }	
	protected UUID  	getLocalOwner    (int owner) { return owners.get(owner); 	  }
	protected OwnerType getLocalOwnerType(int owner) { return owner_types.get(owner); }
	
	//Clear all claims of a specific local owner in this region
	public  void clear    (UUID owner)   {
		int index = owners.indexOf(owner);
		for(int i = 0; i < values.size(); i++) {
			if(values.get(i).owner() != index) { //If the claim isn't of the target owner
				if(values.get(i).owner() > index) {//and is greater than the target owner 
					values.get(i).setOwner(values.get(i).owner() - 1); //Then shift the owner if it's higher than this one's index
				} continue; //and skip this value
			}
			values.remove(i); i--; //Otherwise remove it
		}
		owners.remove(index);
		owner_types.remove(index);
	}
	
	//NOTE: FOR DEBUG PURPOSES ONLY
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
	
	
	
	private String data() {
		String data = "";
		
		//Local Claim Holders Identifications
		for(int i = 0; i < owners.size(); i++) {
			if(i > 0) data += ",";
			data += owner_types.get(i).name().substring(0, 1).toLowerCase() + owners.get(i).toString();
		}
		data += "\n";
		
		//Raw Claims Data
		for(int i = 0; i < values.size(); i++) {
			if(i > 0) data += ",";
			data += values.get(i).data();
		}

		return data;
	}
	
	public void save() {
		Espero.LOADER.save(path, data());
	}

}
