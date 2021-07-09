package me.felnstaren.espero.module.nations.town;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.espero.module.nations.group.LinearRankModel;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.config.ConfigReader;
import me.felnstaren.felib.util.data.SearchObject;

public class Town implements SearchObject {

	private UUID group;
	private UUID nation;
	private UUID uuid; //Replace id with uuid in claim system
	
	private int x;
	private int z;
	public String name;
	private int area, perimeter;
	private int balance;
	private ArrayList<UUID> invites;
	
	private YamlConfiguration config;
	private String path;
	
	public Town(UUID uuid) {
		this.uuid = uuid;
		this.path = "towndata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_town.yml");
		
		this.group = UUID.fromString(config.getString("group"));
		this.name = config.getString("display_name");
		this.x = config.getInt("cx");
		this.z = config.getInt("cz");
		this.area = config.getInt("area");
		this.perimeter = config.getInt("perimeter");
		this.balance = config.getInt("balance");
		this.nation = UUID.fromString(config.getString("nation"));
		this.invites = ConfigReader.readUUIDList(config, "invites");
	}
	
	public Town(UUID nation, String name, int x, int z, EsperoPlayer founder) {
		this.uuid = UUID.randomUUID();
		this.path = "towndata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_town.yml");
		
		this.name = name;
		this.x = x;
		this.z = z;
		this.area = 0;
		this.perimeter = 0;
		this.balance = Option.TOWN_FOUND_COST / 2;
		this.nation = nation;
		this.invites = new ArrayList<UUID>();
		
		Group tgroup = new Group(new LinearRankModel(LinearRankModel.TOWNS_DEFAULT_RANK_HIERARCHY));
		GroupRegistry.inst().register(tgroup);
		NationRegistry.inst().getNation(nation).addTown(this);
		this.group = tgroup.getID();
		join(founder, tgroup.toprank());
	}
	
	
	
	public UUID    getID()             { return uuid; 		  }
	public int     getArea()           { return area;	      }
	public void    setArea(int value)  { this.area = value;   }
	public void    addArea(int value)  { this.area += value;  }
	public int	   getBalance()		   { return balance;	  }
	public void	   setBalance(int value) { this.balance = value;  }
	public void	   addBalance(int value) { this.balance += value; }
	public int	   getPerimeter()	       { return perimeter; 		  }
	public void	   setPerimeter(int value) { this.perimeter = value;  }
	public void	   addPerimeter(int value) { this.perimeter += value; }
	public boolean  outranks (EsperoPlayer superior, EsperoPlayer inferior) { return getGroup().outranks(superior, inferior); }
	public boolean  isInvited(EsperoPlayer player) { return isInvited(player.getUniqueId()); }
	public boolean  isInvited(UUID player)         { return invites.contains(player); }
	public void     invite (EsperoPlayer player)  { if(!invites.contains(player.getUniqueId())) invites.add(player.getUniqueId()); }
	public void     uninvite(EsperoPlayer player) { invites.remove(player.getUniqueId()); }
	public boolean  isMember(EsperoPlayer player)  { return getGroup().contains(player); }
	public boolean  isLeader(EsperoPlayer player)  { return getGroup().isTopRank(player); }
	public void     swapLeader(EsperoPlayer next, EsperoPlayer prev) { demote(prev); getGroup().setRank(next, getGroup().toprank()); }
	public void		promote(EsperoPlayer player)  { getGroup().promote(player); }
	public void		demote (EsperoPlayer player)  { getGroup().demote(player);  }
	public void		kick   (EsperoPlayer player)  { getGroup().remove(player);  }
	public Nation  getNation()         { return NationRegistry.inst().getNation(nation); }
	public Group   getGroup()          { return GroupRegistry.inst().getGroup(group);    }
	public String  neatHeader ()       { return Format.HEADER.message(name); 			 }
	public ArrayList<UUID>         getInvites()		    { return invites; 							   }
	public ArrayList<EsperoPlayer> getLoadedInvites()   { return Espero.PLAYERS.getPlayers(invites);   }
	public ArrayList<EsperoPlayer> getMembers() { return getGroup().getMembers(); }
	public ArrayList<Player>       getOnlineMembers()   {
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<EsperoPlayer> members = getMembers();
		for(EsperoPlayer p : members) {
			Player player = Bukkit.getPlayer(p.getUniqueId());
			if(player != null && player.isOnline()) players.add(player);
		} return players;
	}
	public void broadcast(String message) {
		ArrayList<Player> players = getOnlineMembers();
		for(Player player : players) 
			Messenger.send(player, message);
	}
	public void disband() {
		Espero.LOGGER.debug("TOWN[" + name + "].SELF.DISBAND_AND_DELETE");
		getGroup().disband();
		broadcast(name + " has disbanded");
		TownRegistry.inst().unregister(uuid);
		Espero.LOADER.delete(path);
	}
	public void					   join(EsperoPlayer player)    {
		Espero.LOGGER.stream("PLAYER[" + Espero.OFFLINE_PLAYERS.getName(player.getUniqueId()) + "].TOWN[" + name + "].JOIN");
		if(getGroup().contains(player)) return;
		getGroup().setRank(player, 0);
	}
	public void					   join(EsperoPlayer player, int rank)	{
		Espero.LOGGER.stream("PLAYER[" + Espero.OFFLINE_PLAYERS.getName(player.getUniqueId()) + "].TOWN[" + name + "].JOIN");
		getGroup().setRank(player, rank);
	}
	public boolean hasPermission(EsperoPlayer player, Permission permission)			 {
		return getGroup().hasPermission(player, permission);
	}
	public void claim(int cx, int cz) {
		area++;
		perimeter -= ClaimBoard.inst().countAdjacent(cx, cz, uuid);
		ClaimBoard.inst().claim(cx, cz, uuid);
		balance -= Option.TOWN_CLAIM_PURCHASE_COST;
	}
	public void unclaim(int cx, int cz) {
		area--;
		perimeter += ClaimBoard.inst().countAdjacent(cx, cz, uuid);
		ClaimBoard.inst().unclaim(cx, cz);
		balance += Option.TOWN_CLAIM_SELL_COST;
	}
	
	public void save() {
		config.set("cx", x);
		config.set("cz", z);
		config.set("display_name", name);
		config.set("area", area);
		config.set("perimeter", perimeter);
		config.set("nation", nation.toString());
		config.set("group", group.toString());
		config.set("balance", balance);
		
		String[] sinvites = new String[invites.size()];
		int i = 0; for(UUID invite : invites) { 
			sinvites[i] = invite.toString();
		i++; 	   }
		config.set("invites", sinvites);
		Espero.LOADER.save(path, config);
	}


	
	public int searchValue() {
		return SearchObject.getIndexValue(uuid);
	}

}
