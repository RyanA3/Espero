package me.felnstaren.espero.config;

import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.felib.config.DataPlayer;
import me.felnstaren.felib.logger.Level;

public class EsperoPlayer extends DataPlayer {

	private YamlConfiguration data;
	private String path;
	private UUID uuid;
	
	public EsperoPlayer(UUID uuid) {
		super(uuid);
		Espero.LOGGER.log(Level.DEBUG, "Loading player " + uuid.toString());
	}
	
	public EsperoPlayer(Player player) {
		super(player);
		Espero.LOGGER.log(Level.DEBUG, "Loading player " + player.getName());
	}
	

	
	public void save() {
		Espero.LOADER.save(Espero.LOADER.mark(Espero.LOADER.datafile(path)), data);
	}
	
	protected void load() {
		this.data = Espero.LOADER.readConfig(path, "resources/default_player.yml");
	}
	

	
	public Nation getNation() {
		String nation_id = data.getString("nation", "");
		if(nation_id.equals("")) return null;
		
		Nation nation = Nations.getInstance().getNation(UUID.fromString(nation_id));
		if(nation == null) return null;
		if(nation.getMembers().contains(uuid)) return nation;
		
		data.set("nation", "");
		return null;
	}
	
	public NationPlayerRank getNationRank() {
		Nation nation = getNation();
		if(nation == null) return null;
		String nation_rank = data.getString("nation-rank", "recruit");
		return nation.getRank(nation_rank);
	}
	
	/**
	 * Set the player's nation
	 * @param nation
	 */
	public void setNation(Nation nation) {
		if(nation == null) {
			getNation().getMembers().remove(uuid);
			data.set("nation", "");
		}
		else {
			nation.getMembers().add(uuid);
			nation.getInvites().remove(uuid);
			data.set("nation", nation.getID().toString());
			setRank("recruit");
		}
	}
	
	/**
	 * Set the player's nation rank
	 * @param rank
	 */
	public void setRank(String rank) {
		data.set("nation-rank", rank);
	}
	
	/**
	 * Check if a player has permission to do something within a nation's borders
	 * @param nation_permission
	 * @param nation
	 * @return
	 */
	public boolean hasPermission(String nation_permission, Nation nation) {
		NationPlayerRank rank = getNationRank();
		if(rank == null) return false;
		return rank.isPermitted(nation_permission) && getNation().getID().equals(nation.getID());
	}
	
	
	
	public void addRift() {
		int rifts = data.getInt("rift-count");
		rifts++;
		updateRifts(rifts);
	}
	
	public void delRift() {
		int rifts = data.getInt("rift-count");
		rifts--;
		updateRifts(rifts);
	}
	
	private void updateRifts(int rifts) {
		int max_sanity = 100 - (20 * rifts);
		
		data.set("rift-count", rifts);
		data.set("sanity.max-sanity", max_sanity);
		
		int sanity = data.getInt("sanity.cur-sanity");
		if(sanity > max_sanity) sanity = max_sanity;
		data.set("sanity.cur-sanity", sanity);
		
		save();
	}
	
	
	
	public static boolean hasGenerated(UUID uuid) {
		return Espero.LOADER.datafile("playerdata/" + uuid + ".yml").exists();
	}
	
}
