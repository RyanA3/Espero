package me.felnstaren.espero.module.nations.nation;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.config.Loader;
import me.felnstaren.espero.util.logger.Level;
import me.felnstaren.espero.util.logger.Logger;

public class Nation {

	private Town[] towns;
	private NationPlayerRank[] ranks;
	private UUID[] members;
	
	private UUID id;
	private String display_name;
	
	private YamlConfiguration data;
	private String path;
	
	public Nation(UUID id) {
		try {
			this.id = id;
			this.path = "nationdata/" + id + ".yml";
			this.data = Loader.loadOrDefault(path, "default_nation.yml");
			
			this.display_name = data.getString("display_name");
			
			ConfigurationSection town_section = data.getConfigurationSection("towns");
			String[] town_paths = (String[]) town_section.getKeys(false).toArray();
			towns = new Town[town_paths.length];
			for(int i = 0; i < towns.length; i++)
				towns[i] = new Town(town_section.getConfigurationSection(town_paths[i]));
			
			ConfigurationSection rank_section = data.getConfigurationSection("ranks");
			String[] rank_paths = (String[]) rank_section.getKeys(false).toArray();
			ranks = new NationPlayerRank[rank_paths.length];
			for(int i = 0; i < ranks.length; i++)
				ranks[i] = new NationPlayerRank(rank_section.getConfigurationSection(rank_paths[i]));
			
			String[] members = (String[]) data.getStringList("members").toArray();
			this.members = new UUID[members.length];
			for(int i = 0; i < members.length; i++)
				this.members[i] = UUID.fromString(members[i]);
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log(Level.SEVERE, "Corruption presence at unsafe levels");
		}
	}
	
	
	
	public boolean isMember(UUID player) {
		for(int i = 0; i < members.length; i++)
			if(members[i] != null && members[i].equals(player)) return true;
		return false;
	}
	
	public void save() {
		Loader.save(data, path);
	}
	
}
