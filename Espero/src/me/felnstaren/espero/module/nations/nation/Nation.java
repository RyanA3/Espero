package me.felnstaren.espero.module.nations.nation;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Loader;
import me.felnstaren.espero.util.ArrayUtil;
import me.felnstaren.espero.util.logger.Level;
import me.felnstaren.espero.util.logger.Logger;

public class Nation {

	private ArrayList<Town> towns;
	private NationPlayerRank[] ranks;
	private ArrayList<UUID> members;
	private ArrayList<UUID> invites;
	
	private UUID id;
	private String display_name;
	
	private YamlConfiguration data;
	private String path;
	
	public Nation(UUID id) {
		try {
			this.id = id;
			this.path = "nationdata/" + id + ".yml";
			this.data = Loader.readConfig(path, "default_nation.yml");
			
			this.display_name = data.getString("display_name");
			
			ConfigurationSection town_section = data.getConfigurationSection("towns");
			String[] town_paths = ArrayUtil.stringver(town_section.getKeys(false).toArray());
			towns = new ArrayList<Town>();
			for(int i = 0; i < town_paths.length; i++) {
				Logger.log(Level.DEBUG, "Loading town " + town_paths[i]);
				towns.add(new Town(town_section.getConfigurationSection(town_paths[i])));
			}
			
			ConfigurationSection rank_section = data.getConfigurationSection("ranks");
			String[] rank_paths = ArrayUtil.stringver(rank_section.getKeys(false).toArray());
			ranks = new NationPlayerRank[rank_paths.length];
			for(int i = 0; i < ranks.length; i++)
				ranks[i] = new NationPlayerRank(rank_section.getConfigurationSection(rank_paths[i]));
			
			String[] str_members = ArrayUtil.stringver(data.getStringList("members").toArray());
			this.members = new ArrayList<UUID>();
			for(int i = 0; i < str_members.length; i++)
				members.add(UUID.fromString(str_members[i]));
			
			String[] str_invites = ArrayUtil.stringver(data.getStringList("invites").toArray());
			this.invites = new ArrayList<UUID>();
			for(int i = 0; i < str_members.length; i++)
				invites.add(UUID.fromString(str_invites[i]));
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log(Level.SEVERE, "Corruption presence at unsafe levels");
		}
	}
	
	public Nation(String name, Town capital, EsperoPlayer owner) {
		this.id = UUID.randomUUID();
		this.path = "nationdata/" + id + ".yml";
		this.data = Loader.readConfig(path, "default_nation.yml");
		this.display_name = name;
		
		towns = new ArrayList<Town>();
		towns.add(capital);
		
		ConfigurationSection rank_section = data.getConfigurationSection("ranks");
		String[] rank_paths = ArrayUtil.stringver(rank_section.getKeys(false).toArray());
		
		ranks = new NationPlayerRank[rank_paths.length];
		for(int i = 0; i < ranks.length; i++)
			ranks[i] = new NationPlayerRank(rank_section.getConfigurationSection(rank_paths[i]));
		
		members = new ArrayList<UUID>();
		members.add(owner.getUUID());
		
		invites = new ArrayList<UUID>();
		
		owner.set("nation", id.toString());
		owner.set("nation-rank", "leader");
		owner.save();
		
		save();
	}
	
	
	
	public ArrayList<UUID> getMembers() {
		return members;
	}
	
	public ArrayList<UUID> getInvites() {
		return invites;
	}
	
	public NationPlayerRank getRank(String label) {
		for(NationPlayerRank rank : ranks)
			if(rank.getLabel().equals(label)) return rank;
		return null;
	}
	
	public void save() {
		data.set("display_name", display_name);
		
		String[] smembers = new String[members.size()];
		for(int i = 0; i < smembers.length; i++)
			smembers[i] = members.get(i).toString();
		data.set("members", smembers);
		
		String[] sinvites = new String[invites.size()];
		for(int i = 0; i < smembers.length; i++)
			smembers[i] = members.get(i).toString();
		data.set("invites", sinvites);
		
		for(NationPlayerRank rank : ranks)
			rank.save(data);
		
		for(Town town : towns)
			town.save(data);
		
		Loader.save(data, path);
	}
	
	
	
	public void delete() {
		File file = Loader.load(path);
		file.delete();
	}
	
	
	
	public UUID id() {
		return id;
	}
	
	public String getDisplayName() {
		return display_name;
	}
	
}
