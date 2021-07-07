package me.felnstaren.espero.module.nations.claim;

import java.util.UUID;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;

/**
 * Modifying this class wont do anything so
 * it doesn't matter if shits public lol
 * @author ryan
 */

public class ClaimChunk {
	
	public int x;
	public int z;
	public UUID owner;
	public OwnerType owner_type;
	
	public ClaimChunk(int x, int z, UUID owner, OwnerType owner_type) {
		this.x = x;
		this.z = z;
		this.owner = owner;
		this.owner_type = owner_type;
	}
	
	
	
	//UNPROTECTED: Owner could be Town OR Nation, but NOT BOTH
	public Nation getNation() { return NationRegistry.inst().getNation(owner); }
	public Town   getTown()   { return TownRegistry.inst().getTown(owner);     }
	
	//Get a claim relative to this one, if there is one
	public ClaimChunk getRelative(int offX, int offZ) { return ClaimBoard.inst().getClaim(x + offX, z + offZ); }
	
	
	//Check a chunk is owned by the specified owner
	public static boolean isOwnedBy(ClaimChunk chunk, UUID owner) {
		if(chunk == null || chunk.owner == null) return false;
		return chunk.owner.equals(owner);
	}
	
	/**
	 * 
	 * @param chunk
	 * @param nation
	 * @param town  -1 = Ignore Town Argument   0 = No Town   1-Infinity = Town ID
	 * @return
	 */
	@Deprecated // ??? WHAT ???
	public static boolean isTown(ClaimChunk chunk, UUID owner, int town) {
		/*if(chunk == null || chunk.nation == null) return false;
		if(!chunk.owner.equals(nation)) return false;
		if(town == -1) return true;
		return chunk.town == town;*/
		return chunk != null && 
			   chunk.owner_type == OwnerType.TOWN && 
			   chunk.owner.equals(owner);
	}

}
