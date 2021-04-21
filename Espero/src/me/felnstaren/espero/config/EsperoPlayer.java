package me.felnstaren.espero.config;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.config.DataPlayer;
import me.felnstaren.felib.config.Loader;
import me.felnstaren.felib.logger.Level;

public class EsperoPlayer extends DataPlayer {
	
	private Nation nation = null;
	private NationPlayerRank rank = null;
	
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
	
	
	
	@Deprecated
	public void setNation(Nation nation) { this.nation = nation; }
	public Nation getNation()            { return nation;        }
	
	public void setNationRank(NationPlayerRank rank) { this.rank = rank; }
	public NationPlayerRank getNationRank()          { return rank;      }
	
	
	
	public void addRift(int count) {
		rifts += count;
		max_sanity = 100 - (20 * rifts);
		if(sanity > max_sanity) sanity = max_sanity;
	}
	
	
	@Override
	protected void save(Loader loader) {
		if(nation != null) this.config.set("nation", nation.getID().toString());
		else               this.config.set("nation", "");
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

		//TODO: Check if config#getString() can return empty string for null strings
		if(config.getString("nation") != null) nation = NationRegistry.inst().getNation(UUID.fromString(config.getString("nation")));
		if(nation != null && !nation.getMembers().contains(uuid)) nation = null;  //Just... in... case...
		if(nation != null) rank = config.getString("nation-rank") == null ? nation.getLowestRank() : nation.getRank(config.getString("nation-rank"));

		rifts = config.getInt("rift-count");
		sanity = config.getInt("sanity.cur-sanity");
		max_sanity = config.getInt("sanity.max_sanity");
	}
	
	
	
	/*
	 * Check if a player file has generated
	 */
	public static boolean hasGenerated(UUID uuid) {
		return Espero.LOADER.datafile("playerdata/" + uuid + ".yml").exists();
	}
	
}
