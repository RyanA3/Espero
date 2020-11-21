package me.felnstaren.espero.module.nations.claim;

public enum ClaimType {

	TERRITORY(0),
	TOWN(1);
	
	
	private int id;
	
	private ClaimType(int id) {
		this.id = id;
	}
	
	public int id() {
		return id;
	}
	
	
	
	public static ClaimType from(int id) {
		if(id == 0) return TERRITORY;
		else return TOWN;
	}
	
}
