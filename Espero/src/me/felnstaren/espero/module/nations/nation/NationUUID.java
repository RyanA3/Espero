package me.felnstaren.espero.module.nations.nation;

import java.util.UUID;

public class NationUUID {

	private UUID uuid;
	private Nation nation;
	
	public NationUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	public NationUUID(String string) {
		try { this.uuid = UUID.fromString(string); }
		catch (Exception e) {}
	}
	
	
	
	public Nation getNation() {
		if(nation == null && uuid != null) nation = NationRegistry.inst().getNation(uuid);
		return nation;
	}
	
	public UUID getID() {
		return uuid;
	}
	
}
