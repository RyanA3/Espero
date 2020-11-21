package me.felnstaren.espero.module.nations.claim;

import java.util.UUID;

public class ClaimChunk {

	private int x;
	private int z;
	private int id;
	
	public ClaimChunk(int x, int z, int id) {
		this.x = x % 32;
		this.z = z % 32;
		this.id = id;
	}
	
	public ClaimChunk(String data) {
		String[] values = data.split(".");
		this.x = Integer.parseInt(values[0]);
		this.z = Integer.parseInt(values[1]);
		this.id = Integer.parseInt(values[2]);
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	public ClaimType getType() {
		return id == 0 ? ClaimType.TERRITORY : ClaimType.TOWN;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String data() {
		return x + "." + z + "." + id;
	}
	
	public ClaimChunkData data(UUID nation) {
		return new ClaimChunkData(x, z, id, nation);
	}
	
}
