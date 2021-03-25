package me.felnstaren.espero.config;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.felib.config.DataPlayer;
import me.felnstaren.felib.config.Loader;
import me.felnstaren.felib.logger.Level;

public class EsperoPlayer extends DataPlayer {
	
	private Nation nation;
	private NationPlayerRank rank;
	
	private int rifts;
	private int sanity;
	private int max_sanity;
	
	public EsperoPlayer(UUID uuid) {
		super(uuid, "resources/default_player.yml");
		Espero.LOGGER.log(Level.DEBUG, "Loading player " + uuid.toString());
	}
	
	public EsperoPlayer(Player player) {
		this(player.getUniqueId());
	}
	
	
	
	private Nation loadNation() {
		String nation_id = config.getString("nation", "");
		if(nation_id.length() == 0) return null;
		
		Nation nation = Nations.inst().getNation(UUID.fromString(nation_id));
		if(nation == null) return null;
		
		if(nation.getMembers().contains(uuid)) return nation;
		else return null;
	}
	
	public Nation getNation() {
		return nation;
	}
	
	public void setNation(Nation join) {
		if(join == null && nation != null) 
			nation.getMembers().remove(uuid);
		else {
			join.getMembers().add(uuid);
			join.getInvites().remove(uuid);
			setNationRank(join.getRank("recruit"));
		}
		
		this.nation = join;
	}
	
	
	
	private NationPlayerRank loadNationRank() {
		if(nation == null) return null;
		String nation_rank = config.getString("nation-rank", "recruit");
		return nation.getRank(nation_rank);
	}
	
	public NationPlayerRank getNationRank() {
		return rank;
	}
	
	public void setNationRank(NationPlayerRank rank) {
		this.rank = rank;
	}
	
	public void setNationRank(String rank) {
		if(rank == null || nation == null) this.rank = null; 
		this.rank = nation.getRank(rank);
	}
	
	
	
	/**
	 * Check if a player has permission to do something within a nation's borders
	 * @param nation_permission
	 * @param nation
	 * @return
	 */
	public boolean hasPermission(String nation_permission, Nation check) {
		if(rank == null) return false;
		return this.nation.getID().equals(check.getID()) && rank.isPermitted(nation_permission);
	}
	
	
	
	public void addRift() {
		rifts++;
		updateRifts(rifts);
	}
	
	public void delRift() {
		rifts--;
		updateRifts(rifts);
	}
	
	private void updateRifts(int rifts) {
		max_sanity = 100 - (20 * rifts);
		if(sanity > max_sanity) sanity = max_sanity;
	}
	
	
	
	public static boolean hasGenerated(UUID uuid) {
		return Espero.LOADER.datafile("playerdata/" + uuid + ".yml").exists();
	}
	
	
	
	@Override
	protected void save(Loader loader) {
		if(nation != null) this.config.set("nation", nation.getID().toString());
		if(rank != null)   this.config.set("nation-rank", rank.getLabel());
		else               this.config.set("nation-rank", "recruit");
		this.config.set("rift-count", rifts);
		this.config.set("sanity.cur-sanity", sanity);
		this.config.set("sanity.max-sanity", max_sanity);
		super.save(loader);
	}
	
	@Override
	protected void load(Loader loader) {
		super.load(loader);
		
		nation = loadNation();
		rank = loadNationRank();
		
		rifts = config.getInt("rift-count");
		sanity = config.getInt("sanity.cur-sanity");
		max_sanity = config.getInt("sanity.max_sanity");
	}
	
}
