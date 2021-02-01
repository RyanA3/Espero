package me.felnstaren.espero.module.nations.claim;

import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.logger.Level;

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
		Espero.LOGGER.log(Level.DEBUG, "Loading chunk from data\n" + data);
		String[] values = data.split("\\.");
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
	
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	

	
	public String data() {
		return x + "." + z + "." + id;
	}
	
	public ClaimChunkData data(UUID nation) {
		return new ClaimChunkData(x, z, id, nation);
	}
	
}
