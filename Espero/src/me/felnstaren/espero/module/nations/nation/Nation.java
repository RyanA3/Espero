package me.felnstaren.espero.module.nations.nation;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.rilib.chat.Messenger;
import me.felnstaren.rilib.logger.Level;
import me.felnstaren.rilib.util.ArrayUtil;

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
			this.data = Espero.LOADER.readConfig(path, "default_nation.yml");
			
			this.display_name = data.getString("display_name");

			loadTowns();
			loadRanks();
			loadUsers();
		} catch (Exception e) {
			e.printStackTrace();
			Espero.LOGGER.log(Level.SEVERE, "Corruption presence at unsafe levels");
		}
	}
	
	public Nation(String name, Town capital, EsperoPlayer owner) {
		try {
			this.id = UUID.randomUUID();
			this.path = "nationdata/" + id + ".yml";
			this.data = Espero.LOADER.readConfig(path, "default_nation.yml");
			this.display_name = name;
		
			loadRanks();
		
			towns = new ArrayList<Town>();
			towns.add(capital);

			members = new ArrayList<UUID>();
			members.add(owner.getUUID());
		
			invites = new ArrayList<UUID>();
		
			owner.set("nation", id.toString());
			owner.set("nation-rank", "leader");
			owner.save();
			save();
		} catch (Exception e) {
			e.printStackTrace();
			Espero.LOGGER.log(Level.SEVERE, "Error creating nation");
			Espero.LOADER.delete(path);
		}
	}
	
	
	
	public ArrayList<UUID> getMembers() {
		return members;
	}
	
	public ArrayList<Player> getOnlineMembers() {
		ArrayList<Player> players = new ArrayList<Player>();
		
		for(UUID id : members) {
			Player player = Bukkit.getPlayer(id);
			if(player != null && player.isOnline()) players.add(player);
		}
		
		return players;
	}
	
	public ArrayList<UUID> getInvites() {
		return invites;
	}
	
	public NationPlayerRank getRank(String label) {
		for(NationPlayerRank rank : ranks)
			if(rank.getLabel().equals(label)) return rank;
		return null;
	}
	
	public NationPlayerRank getNextHighestRank(NationPlayerRank rank) {
		int floor = rank.getWeight();
		NationPlayerRank closest_high = null;
		for(NationPlayerRank test : ranks) {
			if(test.getWeight() <= floor) continue;
			if(closest_high == null) closest_high = test;
			else if(test.getWeight() < closest_high.getWeight()) closest_high = test;
		}
		return closest_high;
	}
	
	public NationPlayerRank getNextLowestRank(NationPlayerRank rank) {
		int cieling = rank.getWeight();
		NationPlayerRank closest_low = null;
		for(NationPlayerRank test : ranks) {
			if(test.getWeight() >= cieling) continue;
			if(closest_low == null) closest_low = test;
			else if(test.getWeight() > closest_low.getWeight()) closest_low = test;
		}
		return closest_low;
	}
	
	
	
	public UUID getID() {
		return id;
	}
	
	public String getDisplayName() {
		return display_name;
	}
	
	
	
	private void loadTowns() {
		ConfigurationSection town_section = data.getConfigurationSection("towns");
		String[] town_paths = ArrayUtil.stringver(town_section.getKeys(false).toArray());
		towns = new ArrayList<Town>();
		for(int i = 0; i < town_paths.length; i++) {
			Espero.LOGGER.log(Level.DEBUG, "Loading town " + town_paths[i]);
			towns.add(new Town(town_section.getConfigurationSection(town_paths[i])));
		}
	}
	
	private void loadRanks() {
		Espero.LOGGER.log(Level.DEBUG, "Loading ranks, data:" + (data.toString()));
		ConfigurationSection rank_section = data.getConfigurationSection("ranks");
		Espero.LOGGER.log(Level.DEBUG, "From rank section: " + rank_section);
		String[] rank_paths = ArrayUtil.stringver(rank_section.getKeys(false).toArray());
		ranks = new NationPlayerRank[rank_paths.length];
		for(int i = 0; i < ranks.length; i++) 
			ranks[i] = new NationPlayerRank(rank_section.getConfigurationSection(rank_paths[i]));
		
		//Init rank inheretances
		for(NationPlayerRank rank : ranks) 
			if(rank.getInheretanceName() != null)
				rank.setInheretance(getRank(rank.getInheretanceName()));
	}
	
	private void loadUsers() {
		//Load members
		String[] str_members = ArrayUtil.stringver(data.getStringList("members").toArray());
		this.members = new ArrayList<UUID>();
		for(int i = 0; i < str_members.length; i++)
			members.add(UUID.fromString(str_members[i]));
		
		//Load invites
		String[] str_invites = ArrayUtil.stringver(data.getStringList("invites").toArray());
		this.invites = new ArrayList<UUID>();
		for(int i = 0; i < str_invites.length; i++)
			invites.add(UUID.fromString(str_invites[i]));
	}
	
	
	
	public void save() {
		data.set("display_name", display_name);
		
		String[] smembers = new String[members.size()];
		for(int i = 0; i < smembers.length; i++)
			smembers[i] = members.get(i).toString();
		data.set("members", smembers);
		
		String[] sinvites = new String[invites.size()];
		for(int i = 0; i < sinvites.length; i++)
			sinvites[i] = invites.get(i).toString();
		data.set("invites", sinvites);
		
		for(NationPlayerRank rank : ranks)
			rank.save(data);
		
		for(Town town : towns)
			town.save(data);
		
		Espero.LOADER.save(data, path);
	}
	
	public void disband() {
		for(UUID member : members) {
			EsperoPlayer emember = new EsperoPlayer(member);
			emember.setNation(null);
			emember.setRank("recruit");
			emember.save();
		}
		
		File file = Espero.LOADER.load(path);
		file.delete();
	}

	public void broadcast(String message) {
		ArrayList<Player> players = getOnlineMembers();
		for(Player player : players) 
			Messenger.send(player, message);
	}
	
}
