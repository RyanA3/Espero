package me.felnstaren.espero.module.nations.claim;

import java.util.UUID;

import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;

/**
 * Modifying this class wont do anything so
 * it doesn't matter if shits public lol
 * @author ryan
 */

public class ClaimChunk {
	
	public int x;
	public int z;
	public UUID owner;
	public OwnerType owner_type;
	
	public ClaimChunk(int x, int z, UUID owner, OwnerType owner_type) {
		this.x = x;
		this.z = z;
		this.owner = owner;
		this.owner_type = owner_type;
	}
	
	
	
	//UNPROTECTED: Owner could be Town OR Nation, but NOT BOTH
	public Nation getNation() { return NationRegistry.inst().getNation(owner); }
	public Town   getTown()   { return TownRegistry.inst().getTown(owner);     }
	public String getNationName() { 
		Nation n = getNation(); 
		if(n == null) return Color.LIGHT_GRAY + "Ruins"; 
		else return n.getDisplayName(); 
	}
	public String getTownName() {
		Town t = getTown();
		if(t == null) return Color.LIGHT_GRAY + "Ruins";
		else return t.getDisplayName();
	}
	public Group  getGroup()  { 
		if(owner_type == OwnerType.TOWN) return getTown().getGroup();
		else if(owner_type == OwnerType.NATION) return getNation().getGroup();
		else return GroupRegistry.inst().getGroup(owner);
	}
	public String getOwnerName() {  
		if(owner_type == null || owner == null) return Color.LIGHT_GRAY + "Ruins";
		if(owner_type == OwnerType.NATION) return getNationName();
		else if(owner_type == OwnerType.TOWN) return getTownName();
		else return Color.LIGHT_GRAY + "Ruins";
	}
	public String getOwnerDisplayName() {
		if(owner_type == OwnerType.NATION) return getNation().getDisplayName();
		else return getTown().getDisplayName();
	}
	
	//Get a claim relative to this one, if there is one
	public ClaimChunk getRelative(int offX, int offZ) { return ClaimBoard.inst().getClaim(x + offX, z + offZ); }
	
	
	//Check a chunk is owned by the specified owner
	public static boolean isOwnedBy(ClaimChunk chunk, UUID owner) {
		if(chunk == null || chunk.owner == null) return false;
		return chunk.owner.equals(owner);
	}

}
