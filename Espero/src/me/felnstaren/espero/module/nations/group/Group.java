package me.felnstaren.espero.module.nations.group;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.util.StringUtil;
import me.felnstaren.felib.util.data.BinarySearchable;
import me.felnstaren.felib.util.data.SearchObject;

public class Group extends BinarySearchable<EsperoPlayer> implements SearchObject {
	
	private UUID uuid;
	
	private String path;
	private YamlConfiguration config;
	
	private LinearRankModel model;
	private ArrayList<Integer> player_ranks = new ArrayList<Integer>();
	private String name;
	
	public Group(UUID uuid) { 
		this.uuid = uuid;
		this.path = "groupdata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_group.yml");
		
		this.name = config.getString("name");
		
		if(model == null) model = new LinearRankModel(config.getConfigurationSection("ranks"));
		List<String> pdata = config.getStringList("members");
		for(int i = 0; i < pdata.size(); i++) {
			String[] spdata = pdata.get(i).split("\\.");
			setRank(Espero.PLAYERS.getPlayer(UUID.fromString(spdata[0])), Integer.parseInt(spdata[1]));
		}
	}
	
	public Group(LinearRankModel model, String name) { 
		this.model = model; 
		this.uuid = UUID.randomUUID();
		this.path = "groupdata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_group.yml");
		this.model = new LinearRankModel(LinearRankModel.NATIONS_DEFAULT_RANK_HIERARCHY);
		this.name = name.toLowerCase().replaceAll(" ", "_");
	}
	
	
	public void setRank(EsperoPlayer player, int rank) {
		int index = indexOf(player);
		if(index == -1) {
			index = add(player);
			player_ranks.add(index, rank);
			player.addGroup(uuid);
			return;
		}
		
		if(rank == -1)  {
			player_ranks.remove(index);
			values.remove(index);
			player.leaveGroup(uuid);
			return;
		}
		
		player_ranks.set(index, rank);
	}
	
	public void kick(EsperoPlayer player) {
		int index = indexOf(player);
		player.leaveGroup(uuid);
		player_ranks.remove(index);
		super.values.remove(index);
	}
	
	
	public String 				   getName() 			{ return name; }
	public String 				   getDisplayName() 	{ return StringUtil.title(name.replaceAll("_", " ")); }
	public String  				   neatHeader ()        { return Format.HEADER.message(name); 			 }
	public  boolean contains(EsperoPlayer player){ return super.indexOf(player) != -1; } 
	public  UUID    getID() 					 { return uuid; 		}
	public  ArrayList<EsperoPlayer> getMembers() { return super.values; }
	public  Rank[]			getRanks()   { return model.getRanks(); }
	public  LinearRankModel getModel()   { return model; }
	public  int     relRank(EsperoPlayer player) { int index = indexOf(player); return index == -1 ? index : player_ranks.get(indexOf(player)); 			}
	public  Rank    getRank(EsperoPlayer player) { return model.getRank(relRank(player));    			}
	public  Rank	getRank(String rank_name)    { return model.getRank(rank_name); 					}
	public  void    promote(EsperoPlayer player) { setRank(player, model.nextRank(relRank(player))); 	}
	public  void    demote (EsperoPlayer player) { setRank(player, model.prevRank(relRank(player)));    }
	public  boolean hasPermission(EsperoPlayer player, Permission permission) { return model.hasPermission(permission, relRank(player)); }
	public  boolean outranks(EsperoPlayer superior, EsperoPlayer inferior)    { return getRank(superior).outranks(getRank(inferior)); }
	public  int     toprank() { return model.getRanks().length - 1; }
	public  boolean isTopRank(EsperoPlayer player) { return relRank(player) == toprank(); }
	public  void    disband() {
		Espero.LOGGER.debug("GROUP[" + uuid + "].SELF.DISBAND_AND_DELETE");
		for(EsperoPlayer player : super.values) player.leaveGroup(uuid);
		GroupRegistry.inst().unregister(uuid);
		Espero.LOADER.delete(path);
	}
	public ArrayList<EsperoPlayer> getOnlineMembers() {
		ArrayList<EsperoPlayer> online = new ArrayList<EsperoPlayer>();
		for(EsperoPlayer p : super.values) if(p.isOnline()) online.add(p);
		return online;
	}
	public void		broadcast(String message) {
		ArrayList<EsperoPlayer> players = getOnlineMembers();
		for(EsperoPlayer p : players) p.message(message, false);
	}
	
	public void save() {
		ArrayList<String> pdata = new ArrayList<String>();
		for(int i = 0; i < values.size(); i++)
			pdata.add(values.get(i).getUniqueId().toString() + "." + player_ranks.get(i));
		
		config.set("members", pdata);
		config.set("name", name);
		model.save(config);
		
		Espero.LOADER.save(path, config);
	}
	
	
	

	public int searchValue() {
		return SearchObject.getIndexValue(uuid);
	}
	
}
