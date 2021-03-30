package me.felnstaren.espero.module.nations.nation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.config.ConfigReader;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.util.data.SearchObject;

public class Nation implements SearchObject {

	private ArrayList<Town> towns;
	private ArrayList<NationPlayerRank> ranks;
	private ArrayList<UUID> members;
	private ArrayList<UUID> invites;
	private HashMap<UUID, NationRelation> relations;
	private int balance;
	private int area, perimeter, town_area;
	
	private UUID id;
	private String display_name;
	
	private YamlConfiguration config;
	private String path;
	
	public Nation(UUID id) {
		try {
			this.id = id;
			this.path = "nationdata/" + id + ".yml";
			this.config = Espero.LOADER.readConfig(path, "resources/default_nation.yml");
			
			this.display_name = config.getString("display_name", "Default Nation");
			this.balance = config.getInt("balance", 0);
			this.area = config.getInt("area", 0);
			this.perimeter = config.getInt("perimeter", 0);
			this.town_area = config.getInt("town_area", 0);

			loadTowns();
			loadRanks();
			loadUsers();
			loadRelations();
		} catch (Exception e) {
			e.printStackTrace();
			Espero.LOGGER.log(Level.SEVERE, "Corruption presence at unsafe levels");
		}
	}
	
	public Nation(String name, EsperoPlayer owner) {
		try {
			this.id = UUID.randomUUID();
			this.path = "nationdata/" + id.toString() + ".yml";
			this.config = Espero.LOADER.readConfig(path, "resources/default_nation.yml");
			this.display_name = name;
			this.balance = 0;
		
			loadRanks();
		
			towns = new ArrayList<Town>();
			members = new ArrayList<UUID>();
			//members.add(owner.getUniqueId());
			invites = new ArrayList<UUID>();
			relations = new HashMap<UUID, NationRelation>();
		
			owner.setNation(this);
			owner.setNationRank(this.getRank("leader"));
			//owner.save();
			this.area = 0;
			this.perimeter = 0;
			this.town_area = 0;
			
			save();
		} catch (Exception e) {
			e.printStackTrace();
			Espero.LOGGER.log(Level.SEVERE, "Error creating nation");
			Espero.LOADER.delete(Espero.LOADER.datafile(path));
		}
	}
	
	
	
	public ArrayList<UUID> getMembers() { return members; }
	public ArrayList<Town> getTowns() { return towns; }
	public ArrayList<UUID> getInvites() { return invites; }
	public UUID getID() { return id; }
	public String getDisplayName() { return display_name; }
	
	
	public ArrayList<Player> getOnlineMembers() {
		ArrayList<Player> players = new ArrayList<Player>();
		
		for(UUID id : members) {
			Player player = Bukkit.getPlayer(id);
			if(player != null && player.isOnline()) players.add(player);
		}
		
		return players;
	}
	
	public Town getTown(int id) {
		for(Town t : towns)
			if(t.getID() == id) return t;
		return null;
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
	
	
	
	
	public int getBalance() { return balance; }
	public void setBalance(int value) { this.balance = value; }
	public void addBalance(int value) { this.balance += value; }
	public int getArea() { return area; }
	public void setArea(int value) { this.area = value; }
	public void addArea(int value) { this.area += value; }
	public int getPerimeter() { return perimeter; }
	public void setPerimeter(int value) { this.perimeter = value; }
	public void addPerimeter(int value) { this.perimeter += value; }
	public int getTownArea() { return town_area; }
	public void setTownArea(int value) { this.town_area = value; }
	public void addTownArea(int value) { this.town_area += value; }
	
	public NationRelation getRelation(UUID other) {
		NationRelation relation = relations.get(other);
		if(relation == null) return NationRelation.NEUTRAL;
		else return relation;
	}
	
	public void setRelation(UUID other, NationRelation relation) {
		if(relations.containsKey(other)) relations.remove(other);
		if(relation == null) return;
		relations.put(other, relation);
	}
	
	
	private void loadTowns() {
		if(config.getConfigurationSection("towns") == null) towns = new ArrayList<Town>();
		else towns = ConfigReader.readSectionInSectionObjects(config, "towns", Town.class);
	}
	
	private void loadRanks() {
		ranks = ConfigReader.readSectionInSectionObjects(config, "ranks", NationPlayerRank.class);

		//Init rank inheretances
		for(NationPlayerRank rank : ranks) 
			if(rank.getInheretanceName() != null)
				rank.setInheretance(getRank(rank.getInheretanceName()));
	}
	
	private void loadUsers() {
		members = ConfigReader.readUUIDList(config, "members");
		invites = ConfigReader.readUUIDList(config, "invites");
	}
	
	private void loadRelations() {
		relations = new HashMap<UUID, NationRelation>();
		List<String> srelations = config.getStringList("relations");
		for(String value : srelations) {
			String[] values = value.split(":");
			relations.put(UUID.fromString(values[0]), NationRelation.valueOf(values[1]));
		}
	}
	
	
	
	public void save() {
		config.set("display_name", display_name);
		config.set("balance", balance);
		config.set("area", area);
		config.set("perimeter", perimeter);
		config.set("town_area", town_area);
		
		String[] smembers = new String[members.size()];
		for(int i = 0; i < smembers.length; i++)
			smembers[i] = members.get(i).toString();
		config.set("members", smembers);
		
		String[] sinvites = new String[invites.size()];
		for(int i = 0; i < sinvites.length; i++)
			sinvites[i] = invites.get(i).toString();
		config.set("invites", sinvites);
		
		ArrayList<String> srelations = new ArrayList<String>();
		for(UUID uuid : relations.keySet()) 
			srelations.add(uuid.toString() + ":" + relations.get(uuid).name());
		config.set("relations", srelations);
		
		for(NationPlayerRank rank : ranks)
			rank.save(config);
		
		for(Town town : towns)
			town.save(config);
		
		Espero.LOADER.save(path, config);
	}
	
	public void disband() {
		for(UUID member : members) {
			EsperoPlayer emember = new EsperoPlayer(member);
			emember.setNation(null);
			emember.setNationRank("recruit");
			//emember.save();
		}
		
		Espero.LOADER.delete(path);
	}

	public void broadcast(String message) {
		ArrayList<Player> players = getOnlineMembers();
		for(Player player : players) 
			Messenger.send(player, message);
	}

	
	
	public int searchValue() {
		return SearchObject.getIndexValue(id);
	}
	
	public String neatHeader() {
		return Format.HEADER.message(display_name);
	}
	
}
