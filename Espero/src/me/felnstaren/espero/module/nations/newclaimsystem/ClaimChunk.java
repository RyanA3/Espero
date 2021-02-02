package me.felnstaren.espero.module.nations.newclaimsystem;

public class ClaimChunk {

	private int location;
	private int town;
	private int nation;
	
	public ClaimChunk(int location, int nation, int town) {		
		this.location = location;
		this.town = town;
		this.nation = nation;
	}
	
	public ClaimChunk(int x, int z, int nation, int town) {
		this.location = z * ClaimRegion.WIDTH + x;
		this.town = town;
		this.nation = nation;
	}
	
	public ClaimChunk(String data) {
		String[] values = data.split("\\.");
		this.location = Integer.parseInt(values[0]);
		this.town = Integer.parseInt(values[1]);
		this.nation = Integer.parseInt(values[2]);
	}
	
	public String data() {
		return location + "." + town;
	}
	
	
	
	public int x() {
		int x = location % ClaimRegion.WIDTH;
		return x;
	}
	
	public int z() {
		int z = (int) (location / ClaimRegion.WIDTH); 
		return z;
	}
	
	public int location() {
		return location;
	}
	
	public int town() {
		return town;
	}
	
	public int nation() {
		return nation;
	}
	
	public void setTown(int town) {
		this.town = town;
	}
	
	public void setNation(int nation) {
		this.nation = nation;
	}
	
}
