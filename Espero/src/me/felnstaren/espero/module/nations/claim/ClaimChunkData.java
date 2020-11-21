package me.felnstaren.espero.module.nations.claim;

import java.util.UUID;

public class ClaimChunkData {
	
	public int x;
	public int z;
	public int id;
	public UUID nation;
	
	public ClaimChunkData(int x, int z, int id, UUID nation) {
		this.x = x;
		this.z = z;
		this.id = id;
		this.nation = nation;
	}

}
