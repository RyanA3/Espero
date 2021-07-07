package me.felnstaren.espero.module.nations.nation;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.Nations;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.config.ConfigReader;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.util.data.SearchObject;

public class Nation implements SearchObject {

	private UUID group;
	private ArrayList<UUID> towns;
	private ArrayList<UUID> invites;
	
	public int balance, area, perimeter, town_area;
	
	private UUID uuid;
	public String name;
	
	private YamlConfiguration config;
	private String path;
	
	public Nation(UUID uuid) {
		try {
			this.uuid = uuid;
			this.path = "nationdata/" + uuid + ".yml";
			this.config = Espero.LOADER.readConfig(path, "resources/default_nation.yml");
			
			this.name = config.getString("display_name", "Default Nation");
			this.balance = config.getInt("balance", 0);
			this.area = config.getInt("area", 0);
			this.perimeter = config.getInt("perimeter", 0);
			this.town_area = config.getInt("town_area", 0);

			this.towns = ConfigReader.readUUIDList(config, "towns");
			this.invites = ConfigReader.readUUIDList(config, "invites");
		} catch (Exception e) {
			e.printStackTrace();
			Espero.LOGGER.log(Level.SEVERE, "Corruption presence at unsafe levels");
		}
	}
	
	public Nation(String name, EsperoPlayer owner) {
		try {
			this.uuid = UUID.randomUUID();
			this.path = "nationdata/" + uuid.toString() + ".yml";
			this.config = Espero.LOADER.readConfig(path, "resources/default_nation.yml");
			this.name = name;
			this.balance = Option.NATION_STARTING_BALANCE;

			towns = new ArrayList<UUID>();
			invites = new ArrayList<UUID>();
		
			Nations.setNation(owner, this);
			//Nations.setNationRank(owner, getHighestRank());

			this.area = 0;
			this.perimeter = 0;
			this.town_area = 0;
			
			save();
		} catch (Exception e) {
			e.printStackTrace();
			Espero.LOGGER.log(Level.SEVERE, "<!> Severe error creating nation! <!>");
			Espero.LOGGER.log(Level.SEVERE, "\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣤⣤⣤⣤⣤⣶⣦⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⡿⠛⠉⠙⠛⠛⠛⠛⠻⢿⣿⣷⣤⡀⠀⠀⠀⠀⠀\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⠋⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠈⢻⣿⣿⡄⠀⠀⠀⠀\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⣸⣿⡏⠀⠀⠀⣠⣶⣾⣿⣿⣿⠿⠿⠿⢿⣿⣿⣿⣄⠀⠀⠀\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠁⠀⠀⢰⣿⣿⣯⠁⠀⠀⠀⠀⠀⠀⠀⠈⠙⢿⣷⡄⠀\n"
					+ "⠀⠀⣀⣤⣴⣶⣶⣿⡟⠀⠀⠀⢸⣿⣿⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣷⠀\n"
					+ "⠀⢰⣿⡟⠋⠉⣹⣿⡇⠀⠀⠀⠘⣿⣿⣿⣿⣷⣦⣤⣤⣤⣶⣶⣶⣶⣿⣿⣿⠀\n"
					+ "⠀⢸⣿⡇⠀⠀⣿⣿⡇⠀⠀⠀⠀⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠀\n"
					+ "⠀⣸⣿⡇⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠉⠻⠿⣿⣿⣿⣿⡿⠿⠿⠛⢻⣿⡇⠀⠀\n"
					+ "⠀⣿⣿⠁⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣧⠀⠀\n"
					+ "⠀⣿⣿⠀⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⠀⠀\n"
					+ "⠀⣿⣿⠀⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⠀⠀\n"
					+ "⠀⢿⣿⡆⠀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡇⠀⠀\n"
					+ "⠀⠸⣿⣧⡀⠀⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⠃⠀⠀\n"
					+ "⠀⠀⠛⢿⣿⣿⣿⣿⣇⠀⠀⠀⠀⠀⣰⣿⣿⣷⣶⣶⣶⣶⠶⠀⢠⣿⣿⠀⠀⠀\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⣿⣿⡇⠀⣽⣿⡏⠁⠀⠀⢸⣿⡇⠀⠀⠀\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⣿⣿⠀⠀⠀⠀⠀⣿⣿⡇⠀⢹⣿⡆⠀⠀⠀⣸⣿⠇⠀⠀⠀\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⢿⣿⣦⣄⣀⣠⣴⣿⣿⠁⠀⠈⠻⣿⣿⣿⣿⡿⠏⠀⠀⠀⠀\n"
					+ "⠀⠀⠀⠀⠀⠀⠀⠈⠛⠻⠿⠿⠿⠿⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n"
					+ "     WHEN THE DATA FILE IS SUSSY\n");
			Espero.LOADER.delete(Espero.LOADER.datafile(path));
			disband();
		}
	}
	
	
	public String                  getPath() 		    { return path; 								   } 
	public ArrayList<EsperoPlayer> getMembers() 	    { return getGroup().getMembers();			   }
	public Group				   getGroup()			{ return GroupRegistry.inst().getGroup(group); }
	public ArrayList<Town>         getTowns() 			{ return TownRegistry.inst().getTowns(towns);  }
	public ArrayList<String>       getTownsNames() 	    { 
		ArrayList<String> names = new ArrayList<String>(); 
		ArrayList<Town> towns = getTowns();
		for(Town t : towns) names.add(t.name); 
		return names; 
	}
	public ArrayList<UUID>         getInvites()		    { return invites; 							   }
	public ArrayList<EsperoPlayer> getLoadedInvites()   { return Espero.PLAYERS.getPlayers(invites);   }
	public UUID 				   getID() 				{ return uuid; 								   }
	public String 				   getDisplayName() 	{ return name; 								   }
	public ArrayList<Player>       getOnlineMembers()   {
		ArrayList<Player> players = new ArrayList<Player>();
		for(EsperoPlayer p : getGroup().getMembers()) {
			Player player = Bukkit.getPlayer(p.getUniqueId());
			if(player != null && player.isOnline()) players.add(player);
		} return players;
	}
	public Town 				   getTown(int index)   { return TownRegistry.inst().getTown(towns.get(index)); }
	public void					   join(EsperoPlayer player)    {
		if(getGroup().contains(player)) return;
		getGroup().setRank(player, 0);
		invites.remove(player.getUniqueId());
		player.setNation(this);
	}
	public boolean				   hasPermission(EsperoPlayer player, Permission permission) {
		return getGroup().hasPermission(player, permission);
	}
	public boolean  outranks (EsperoPlayer superior, EsperoPlayer inferior) { return getGroup().outranks(superior, inferior); }
	public boolean  isInvited(EsperoPlayer player) { return isInvited(player.getUniqueId()); }
	public boolean  isInvited(UUID player)         { return invites.contains(player); }
	public boolean  isMember(EsperoPlayer player)  { return getGroup().contains(player); }
	public boolean  isLeader(EsperoPlayer player)  { return getGroup().isTopRank(player); }
	public void     swapLeader(EsperoPlayer next, EsperoPlayer prev) { demote(prev); getGroup().setRank(next, getGroup().toprank()); }
	public void		promote(EsperoPlayer player)  { getGroup().promote(player); }
	public void		demote (EsperoPlayer player)  { getGroup().demote(player);  }
	public void		kick   (EsperoPlayer player)  { getGroup().remove(player); player.setNation(null); }
	public void     invite (EsperoPlayer player)  { if(!invites.contains(player.getUniqueId())) invites.add(player.getUniqueId()); }
	public void     uninvite(EsperoPlayer player) { invites.remove(player.getUniqueId()); }
	public void     disband() { //Yikes
		Espero.LOGGER.debug("NATION[" + name + "].SELF.DISBAND_AND_DELETE");
		broadcast("Your nation has disbanded");
		NationRegistry.inst().unregister(uuid);
		Espero.LOADER.delete(path);
	}
	
	public void broadcast(String message) {
		ArrayList<Player> players = getOnlineMembers();
		for(Player player : players) 
			Messenger.send(player, message);
	}
	
	
	
	
	public int  getBalance   () 		   { return balance; 		    }
	public void setBalance   (int value)   {   this.balance   =  value; }
	public void addBalance   (int value)   {   this.balance   += value; }
	public int  getArea      () 		   { return area; 			    }
	public void setArea      (int value)   {   this.area      =  value; }
	public void addArea      (int value)   {   this.area      += value; }
	public int  getPerimeter () 		   { return perimeter;		    }
	public void setPerimeter (int value)   {   this.perimeter =  value; }
	public void addPerimeter (int value)   {   this.perimeter += value; }
	public int  getTownArea  () 		   { return town_area; 		    }
	public void setTownArea  (int value)   {   this.town_area =  value; }
	public void addTownArea  (int value)   {   this.town_area += value; }
	
	
	
	public void save() {
		config.set("display_name", name);
		config.set("balance", balance);
		config.set("area", area);
		config.set("perimeter", perimeter);
		config.set("town_area", town_area);
		
		String[] sinvites = new String[invites.size()];
		for(int i = 0; i < sinvites.length; i++)
			sinvites[i] = invites.get(i).toString();
		config.set("invites", sinvites);
		
		Espero.LOADER.save(path, config);
	}

	

	public int     searchValue() 		 { return SearchObject.getIndexValue(uuid); }
	public String  neatHeader ()         { return Format.HEADER.message(name); 		}
	@Override 
	public boolean equals     (Object o) {
		if(!(o instanceof Nation)) return false;
		return ((Nation) o).searchValue() == searchValue();
	}
	
}
