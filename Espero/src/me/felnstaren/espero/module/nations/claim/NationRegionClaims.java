package me.felnstaren.espero.module.nations.claim;

import java.util.ArrayList;

public class NationRegionClaims {

	private ArrayList<ClaimChunk> claims;
	
	public NationRegionClaims() {
		claims = new ArrayList<ClaimChunk>();
	}
	
	public NationRegionClaims(String data) {
		claims = new ArrayList<ClaimChunk>();
		String[] chunks = data.split(",");
		for(int i = 1; i < chunks.length; i++) 
			claims.add(new ClaimChunk(chunks[i]));
	}
	
	public ArrayList<ClaimChunk> getClaims() {
		return claims;
	}
	
	public ClaimChunk getClaim(int x, int z) {
		x %= 32; z %= 32;
		for(ClaimChunk claim : claims)
			if(claim.getX() == x && claim.getZ() == z) return claim;
		return null;
	}
	
	public void claim(ClaimChunk claim) {
		for(ClaimChunk check : claims)
			if(check.getX() == claim.getX() && check.getZ() == claim.getZ()) { check.setId(claim.getId()); return; }
		claims.add(claim);
	}
	
	public void unclaim(int x, int z) {
		int remove = -1;
		for(int i = 0; i < claims.size(); i++)
			if(claims.get(i).getX() == x && claims.get(i).getZ() == z) { remove = i; break; }
		if(remove > -1) claims.remove(remove);
	}
	
	public boolean isClaimed(int x, int z) {
		x %= 32; z %= 32;
		for(ClaimChunk claim : claims)
			if(claim.getX() == x && claim.getZ() == z) return true;
		return false;
	}
	
}
