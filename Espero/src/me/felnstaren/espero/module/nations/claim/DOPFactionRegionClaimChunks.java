package me.felnstaren.espero.module.nations.claim;

import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.util.math.Vec2d;

public class DOPFactionRegionClaimChunks {
	
	//Data oriented programming?

	private UUID faction;
	private ArrayList<Vec2d> locations;
	private ArrayList<ClaimType> types;
	
	public DOPFactionRegionClaimChunks(String data) {
		String id = data.split("[")[0];
		faction = UUID.fromString(id);
		data = data.replace(id, "").replace("[", "").replace("]", "");
		String[] chunks = data.split(",");
		
		for(int i = 0; i < chunks.length; i++) {
			String[] values = chunks[i].split(".");
			locations.add(new Vec2d(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
			types.add(ClaimType.from(Integer.parseInt(values[2])));
		}
	}
	
	public UUID getFaction() {
		return faction;
	}
	
	public boolean isClaimed(Vec2d location) {
		for(int i = 0; i < locations.size(); i++)
			if(locations.get(i).x == location.x && locations.get(i).y == location.y) return true;
		return false;
	}
	
	public ClaimChunk getClaimed(Vec2d location) {
		for(int i = 0; i < locations.size(); i++)
			if(locations.get(i).x == location.x && locations.get(i).y == location.y) return new ClaimChunk(location.x, location.y, i);
		return null;
	}
	
}
