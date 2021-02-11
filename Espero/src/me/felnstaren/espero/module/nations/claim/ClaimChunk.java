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
		return Nations.getInstance().getNation(nation);
	}
	
	public Town getTown() {
		return getNation().getTown(town);
	}
	
	public ClaimChunk getRelative(int offX, int offZ) {
		return ClaimBoard.getInstance().getClaim(x + offX, z + offZ);
	}

}
