package me.felnstaren.espero.module.nations.claim;

import java.util.UUID;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.espero.module.nations.nation.Town;

/**
 * Modifying this class wont do anything so
 * it doesn't matter if shits public lol
 * @author ryan
 */

public class ClaimChunk {
	
	public int x;
	public int z;
	public int town;
	public UUID nation;
	
	public ClaimChunk(int x, int z, UUID nation, int town) {
		this.x = x;
		this.z = z;
		this.nation = nation;
		this.town = town;
	}
	
	
	
	public Nation getNation() {
		return Nations.inst().getNation(nation);
	}
	
	public Town getTown() {
		return getNation().getTown(town);
	}
	
	public ClaimChunk getRelative(int offX, int offZ) {
		return ClaimBoard.inst().getClaim(x + offX, z + offZ);
	}
	
	
	
	public static boolean isNation(ClaimChunk chunk, UUID nation) {
		if(chunk == null || chunk.nation == null) return false;
		return chunk.nation.equals(nation);
	}
	
	/**
	 * 
	 * @param chunk
	 * @param nation
	 * @param town  -1 = Ignore Town Argument   0 = No Town   1-Infinity = Town ID
	 * @return
	 */
	public static boolean isTown(ClaimChunk chunk, UUID nation, int town) {
		if(chunk == null || chunk.nation == null) return false;
		if(!chunk.nation.equals(nation)) return false;
		if(town == -1) return true;
		return chunk.town == town;
	}

}
