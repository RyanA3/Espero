package me.felnstaren.espero.module.nations.claim;

import me.felnstaren.felib.util.data.SearchObject;

public class ClaimData implements SearchObject {

	private int location;
	private int owner;    //Can be a nation or town --> specified in containing ClaimRegion
	
	public ClaimData(int location, int owner) {
		this.location = location;
		this.owner = owner;
	}
	
	public ClaimData(int x, int z, int owner) {
		this.location = z * ClaimRegion.WIDTH + x;
		this.owner = owner;
	}
	
	public ClaimData(String data) {
		String[] values = data.split("\\.");
		this.location = Integer.parseInt(values[0]);
		this.owner = Integer.parseInt(values[1]);
	}
	
	public String data() {
		return location + "." + owner;
	}
	
	
	
	public int  x() 				{ return location % ClaimRegion.WIDTH;					 }
	public int  z() 				{ return (int) Math.floor(location / ClaimRegion.WIDTH); }
	public int  location() 			{ return location; 										 }	
	public int  owner()				{ return owner; 										 }	
	public void setOwner(int owner) { this.owner = owner; 									 }

	

	public int searchValue() {
		return location;
	}
	
}
