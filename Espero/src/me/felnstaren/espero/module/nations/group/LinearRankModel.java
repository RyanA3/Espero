package me.felnstaren.espero.module.nations.group;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.felib.config.ConfigurationSectionObject;
import me.felnstaren.felib.util.math.Maths;

public class LinearRankModel extends IRankModel {
	
	public static final Rank[] NATIONS_DEFAULT_RANK_HIERARCHY = {
			new Rank("Recruit", 0, Permission.CONTAINER, Permission.BUTTON, Permission.DOOR),
			new Rank("Member", 5, Permission.BUILD, Permission.LEVER, Permission.STONE_BUTTON),
			new Rank("Officer", 10, Permission.INVITE, Permission.KICK, Permission.PROMOTE, Permission.DEMOTE, Permission.CLAIM, Permission.UNCLAIM),
			new Rank("Leader", 15, Permission.COFFERS_WITHDRAW, Permission.NATION_DISBAND, Permission.TOWN_CREATE, Permission.TOWN_DISBAND, Permission.TOWN_SELL)
			};
	public static final Rank[] TOWNS_DEFAULT_RANK_HIERARCHY = {
			new Rank("Citizen", 5, Permission.CONTAINER, Permission.BUTTON, Permission.DOOR, Permission.BUILD, Permission.LEVER, Permission.STONE_BUTTON),
			new Rank("Squire", 10, Permission.INVITE, Permission.CLAIM, Permission.UNCLAIM),
			new Rank("Magistrate", 12, Permission.PROMOTE, Permission.DEMOTE, Permission.INVITE, Permission.KICK),
			new Rank("Mayor", 14, Permission.COFFERS_WITHDRAW, Permission.TOWN_DISBAND, Permission.TOWN_SELL)
	};
	
	
	
	private Rank[] ranks;

	public LinearRankModel(ConfigurationSection data) {
		load(data);
	}
	
	public LinearRankModel(Rank... ranks) {
		this.ranks = ranks;
	}
	
	
	
	public boolean hasPermission(Permission permission, int rank) {
		for(int i = rank; i > 0; i--) 
			if(ranks[i].isPermitted(permission)) return true; 
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


	
	
	public void save(YamlConfiguration config, String path) {
		path = path + ".ranks.";
		for(Rank r : ranks)
			config.set(path + r.display_name, r.strpermissions());
	}
	
	@Override
	public ConfigurationSectionObject load(ConfigurationSection data) {
		ConfigurationSection ranks = data.getConfigurationSection("ranks");
		String[] ranks_cf_secs = (String[]) ranks.getKeys(false).toArray();
		this.ranks = new Rank[ranks_cf_secs.length];
		for(int i = 0; i < ranks_cf_secs.length; i++) {
			this.ranks[i] = new Rank();
			this.ranks[i].load(ranks.getConfigurationSection(ranks_cf_secs[i]));
		}
		return this;
	}

	//UNUSED
	@Override
	public ConfigurationSectionObject template() {
		
		return this;
	}

}
