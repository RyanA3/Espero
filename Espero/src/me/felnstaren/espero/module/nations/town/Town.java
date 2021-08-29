package me.felnstaren.espero.module.nations.town;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
import me.felnstaren.espero.module.nations.town.siege.Siege;
import me.felnstaren.espero.module.nations.town.siege.SiegeRegistry;
import me.felnstaren.espero.module.nations.town.siege.SiegeStage;
import me.felnstaren.espero.util.WorldUtil;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.config.ConfigReader;
import me.felnstaren.felib.item.util.ItemBuild;
import me.felnstaren.felib.util.StringUtil;
import me.felnstaren.felib.util.data.SearchObject;

public class Town implements SearchObject {

	private UUID group;
	private UUID nation;
	private UUID siege;
	private UUID uuid; //Replace id with uuid in claim system
	
	private int x;
	private int z;
	private String name;
	private int area, perimeter;
	private int balance;
	private ArrayList<UUID> invites;
	private TownRelic relic;
	
	private YamlConfiguration config;
	private String path;
	
	public Town(UUID uuid) {
		this.uuid = uuid;
		this.path = "towndata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_town.yml");
		
		this.group = UUID.fromString(config.getString("group"));
		this.name = config.getString("name");
		this.x = config.getInt("cx");
		this.z = config.getInt("cz");
		this.area = config.getInt("area");
		this.perimeter = config.getInt("perimeter");
		this.balance = config.getInt("balance");
		this.invites = ConfigReader.readUUIDList(config, "invites");
		
		this.relic = new TownRelic(this, config.getString("relic"));
		
		
		String nation = config.getString("nation");
		if(nation != null && nation.length() > 1) this.nation = UUID.fromString(nation);
		String siege = config.getString("siege");
		if(siege != null && siege.length() > 1) this.siege = UUID.fromString(siege); 
	}
	
	public Town(UUID nation, String display_name, int x, int z, EsperoPlayer founder) {
		this.uuid = UUID.randomUUID();
		this.path = "towndata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_town.yml");
		
		this.name = display_name.toLowerCase().replaceAll(" ", "_");
		this.x = x;
		this.z = z;
		this.area = 0;
		this.perimeter = 0;
		this.balance = Option.TOWN_FOUND_COST / 2;
		this.nation = nation;
		this.invites = new ArrayList<UUID>();
		
		this.relic = new TownRelic(this);
		relic.setLocation(x*16 + 8, WorldUtil.findHighestBlock(Bukkit.getWorlds().get(0), x*16 + 8, z*16 + 8).getY() + 1, z*16 + 8);
		new BukkitRunnable() {
			public void run() {
				relic.spawn();
			}
		}.runTask(Bukkit.getPluginManager().getPlugin("Espero"));
		
		Group tgroup = new Group(new LinearRankModel(LinearRankModel.TOWNS_DEFAULT_RANK_HIERARCHY), name);
		GroupRegistry.inst().register(tgroup);
		if(nation != null) NationRegistry.inst().getNation(nation).addTown(this);
		this.group = tgroup.getID();
		join(founder, tgroup.toprank());
	}
	
	public Town(String display_name, int x, int z, EsperoPlayer founder) {
		this(null, display_name, x, z, founder);
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
	public String  getName() { return name; }
	public String  getDisplayName() { return StringUtil.title(name.replaceAll("_", " ")); }
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
	public void		kick   (EsperoPlayer player)  { getGroup().remove(player); player.leaveGroup(group); }
	public Nation  getNation()         { return NationRegistry.inst().getNation(nation); }
	public void    setNation(Nation nation) { this.nation = nation.getID(); }
	public void    setNation(UUID nation)   { this.nation = nation; }
	public boolean isInNation() 			{ return nation != null; }
	public Group   getGroup()          { return GroupRegistry.inst().getGroup(group);    }
	public Siege   getSiege() { return siege != null ? SiegeRegistry.inst().getSiege(siege) : null; }
	public boolean isInSiege() { return siege != null && getSiege().getStage() != SiegeStage.COMPLETE; }
	public void    setSiege(UUID siege) { this.siege = siege; }
	public String  neatHeader ()       { return Format.HEADER.message(getDisplayName()); 			 }
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
		if(relic.exists()) relic.destroy();
		getGroup().disband();
		TownRegistry.inst().unregister(uuid);
		Espero.LOADER.delete(path);
	}
	public void					   join(EsperoPlayer player)    {
		join(player, 0);
	}
	public void					   join(EsperoPlayer player, int rank)	{
		Espero.LOGGER.stream("PLAYER[" + Espero.OFFLINE_PLAYERS.getName(player.getUniqueId()) + "].TOWN[" + name + "].JOIN");
		getGroup().setRank(player, rank);
		player.addGroup(group);
	}
	public boolean hasPermission(EsperoPlayer player, Permission permission)			 {
		Siege siege = getSiege();
		if(siege == null || siege.getStage() == SiegeStage.STARTING) return getGroup().hasPermission(player, permission);
		return siege.getStage().isPermitted(permission);
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
	public ItemStack generateRelicItem() {
		return new ItemBuild(Material.END_CRYSTAL)
				.setFlag("modifier0", "SOULBOUND")
				.setFlag("town_relic", name)
				.setLore("&e&oSoulbound")
				.setName("&6" + getDisplayName() + "'s Relic")
				.construct();
	}
	public TownRelic getRelic() { return relic; }
	
	public void save() {
		config.set("cx", x);
		config.set("cz", z);
		config.set("name", name);
		config.set("area", area);
		config.set("perimeter", perimeter);
		if(nation != null) config.set("nation", nation.toString());
		if(siege != null) config.set("siege", siege.toString());
		config.set("group", group.toString());
		config.set("balance", balance);
		
		config.set("relic", relic.data());
		
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
