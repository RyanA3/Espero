package me.felnstaren.espero.module.nations.group;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.felib.util.data.BinarySearchable;
import me.felnstaren.felib.util.data.SearchObject;

public class Group extends BinarySearchable<EsperoPlayer> implements SearchObject {
	
	private UUID uuid;
	
	private String path;
	private YamlConfiguration config;
	
	private LinearRankModel model;
	private ArrayList<Integer> player_ranks = new ArrayList<Integer>();
	
	public Group(UUID uuid) { 
		this.uuid = uuid;
		this.path = "groupdata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_group.yml");
		
		if(model == null) model = new LinearRankModel(config.getConfigurationSection("ranks"));
		List<String> pdata = config.getStringList("members");
		for(int i = 0; i < pdata.size(); i++) {
			String[] spdata = pdata.get(i).split("\\.");
			setRank(Espero.PLAYERS.getPlayer(UUID.fromString(spdata[0])), Integer.parseInt(spdata[1]));
		}
	}
	
	public Group(LinearRankModel model) { 
		this.model = model; 
		this.uuid = UUID.randomUUID();
		this.path = "groupdata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_group.yml");
		this.model = new LinearRankModel(LinearRankModel.NATIONS_DEFAULT_RANK_HIERARCHY);
	}
	
	
	public void setRank(EsperoPlayer player, int rank) {
		int index = indexOf(player);
		if(index == -1) {
			index = add(player);
			player_ranks.add(index, rank);
			return;
		}
		
		if(rank == -1)  {
			player_ranks.remove(index);
			values.remove(index);
		}
		else player_ranks.set(index, rank);
	}
	
	public  boolean contains(EsperoPlayer player){ return super.indexOf(player) != -1; } 
	public  UUID    getID() 					 { return uuid; 		}
	public  ArrayList<EsperoPlayer> getMembers() { return super.values; }
	public  int     relRank(EsperoPlayer player) { int index = indexOf(player); return index == -1 ? index : player_ranks.get(indexOf(player)); 			}
	public  Rank    getRank(EsperoPlayer player) { return model.getRank(relRank(player));    			}
	public  void    promote(EsperoPlayer player) { setRank(player, model.nextRank(relRank(player))); 	}
	public  void    demote (EsperoPlayer player) { setRank(player, model.prevRank(relRank(player)));    }
	public  boolean hasPermission(EsperoPlayer player, Permission permission) { return model.hasPermission(permission, relRank(player)); }
	public  boolean outranks(EsperoPlayer superior, EsperoPlayer inferior)    { return getRank(superior).outranks(getRank(inferior)); }
	public  int     toprank() { return model.getRanks().length - 1; }
	public  boolean isTopRank(EsperoPlayer player) { return relRank(player) == toprank(); }
	public  void    disband() {
		Espero.LOGGER.debug("GROUP[" + uuid + "].SELF.DISBAND_AND_DELETE");
		GroupRegistry.inst().unregister(uuid);
		Espero.LOADER.delete(path);
	}
	
	public void save() {
		ArrayList<String> pdata = new ArrayList<String>();
		for(int i = 0; i < values.size(); i++)
			pdata.add(values.get(i).getUniqueId().toString() + "." + player_ranks.get(i));
		
		config.set("members", pdata);
		model.save(config);
		
		Espero.LOADER.save(path, config);
	}
	
	
	

	public int searchValue() {
		return SearchObject.getIndexValue(uuid);
	}
	
}
