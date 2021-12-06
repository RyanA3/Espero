package me.felnstaren.espero.module.nations.group;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.config.ConfigurationSectionObject;
import me.felnstaren.felib.util.math.Maths;

public class LinearRankModel extends IRankModel {
	
	public static final Rank[] NATIONS_DEFAULT_RANK_HIERARCHY = {
			new Rank("Recruit", 0, Permission.CONTAINER, Permission.BUTTON, Permission.DOOR),
			new Rank("Member", 5, Permission.BUILD, Permission.LEVER, Permission.STONE_BUTTON),
			new Rank("Officer", 10, Permission.INVITE, Permission.KICK, Permission.PROMOTE, Permission.DEMOTE, Permission.CLAIM, Permission.UNCLAIM),
			new Rank("Leader", 15, Permission.CLAIM, Permission.COFFERS_WITHDRAW, Permission.NATION_DISBAND, Permission.TOWN_CREATE, Permission.TOWN_DISBAND, Permission.TOWN_SELL)
			};
	public static final Rank[] TOWNS_DEFAULT_RANK_HIERARCHY = {
			new Rank("Citizen", 5, Permission.CONTAINER, Permission.BUTTON, Permission.DOOR, Permission.BUILD, Permission.LEVER, Permission.STONE_BUTTON),
			new Rank("Squire", 10, Permission.INVITE, Permission.CLAIM, Permission.UNCLAIM),
			new Rank("Magistrate", 12, Permission.PROMOTE, Permission.DEMOTE, Permission.INVITE, Permission.KICK),
			new Rank("Mayor", 14, Permission.COFFERS_WITHDRAW, Permission.TOWN_DISBAND, Permission.TOWN_SELL, Permission.START_SIEGE, Permission.RELIC)
	};
	
	
	
	private Rank[] ranks;

	public LinearRankModel(ConfigurationSection data) {
		load(data);
	}
	
	public LinearRankModel(Rank... ranks) {
		this.ranks = ranks;
	}
	
	
	
	public boolean hasPermission(Permission permission, int rank) {
		if(rank < 0 || rank > ranks.length) return false;
		Espero.LOGGER.stream("Check <<" + permission.name() + "::" + rank);
		for(int i = rank; i > 0; i--) {
			Espero.LOGGER.stream("Check " + permission.name() + "::" + rank);
			if(ranks[i].isPermitted(permission)) return true; 
		}
		Espero.LOGGER.stream("false");
		return false;
	}
	
	public int    nextRank (int rank)    { return (int) Maths.clamp(rank + 1, 0, ranks.length-1); }
	public int    prevRank (int rank)    { return (int) Maths.clamp(rank - 1, 0, ranks.length-1); }
	public Rank   getRank  (int rank)    { return ranks[rank]; }
	public Rank[] getRanks ()            { return ranks; 	   }
	public Rank   getRank  (String name) {
		for(Rank r : ranks) 
			if(r.gaming_name.equals(name)) 
				return r; 
		return null;
	}
	public ArrayList<String> getRanksNames() {
		ArrayList<String> names = new ArrayList<String>();
		for(Rank r : ranks) names.add(r.gaming_name);
		return names;
	}


	
	
	public void save(YamlConfiguration config) {
		for(Rank r : ranks)
			r.save(config.getConfigurationSection("ranks." + r.gaming_name));
	}
	
	@Override
	public ConfigurationSectionObject load(ConfigurationSection data) {
		Set<String> ranks_cf_secs = data.getKeys(false);
		this.ranks = new Rank[ranks_cf_secs.size()];
		int i = 0;
		for(String rank_key : ranks_cf_secs) {
			Espero.LOGGER.stream("Load rank " + data.getCurrentPath() + "." + rank_key);
			this.ranks[i] = new Rank(rank_key);
			this.ranks[i].load(data.getConfigurationSection(rank_key));
			i++;
		}
		return this;
	}

	//UNUSED
	@Override
	public ConfigurationSectionObject template() {
		
		return this;
	}

}
