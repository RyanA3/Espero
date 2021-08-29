package me.felnstaren.espero.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.config.ConfigReader;
import me.felnstaren.felib.config.DataPlayer;
import me.felnstaren.felib.config.Loader;
import me.felnstaren.felib.logger.Level;

public class EsperoPlayer extends DataPlayer {
	
	private Player player;
	private UUID nation;
	private ArrayList<UUID> groups;
	private ArrayList<String> missed_messages;
	private ArrayList<ItemStack> respawn_inventory;
	private UUID group_chat;
	
	private int rifts;
	private int sanity;
	private int max_sanity;
	
	public EsperoPlayer(UUID uuid) {
		super(uuid, "resources/default_player.yml");
		Espero.LOGGER.log(Level.DEBUG, "Loading player " + uuid.toString() + "...");
		Player gplayer = Bukkit.getPlayer(uuid);
		if(gplayer != null) player = gplayer;
	}
	
	public EsperoPlayer(Player player) {
		super(player.getUniqueId(), "resources/default_player.yml");
		Espero.LOGGER.debug("Loading player " + player.getName() + "...");
		this.player = player;
	}
	
	
	
	
	public void setNation(Nation nation) { this.nation = (nation == null ? null : nation.getID()); }
	public Nation getNation()            { return nation == null ? null : NationRegistry.inst().getNation(nation);         }
	public boolean isOnline()			 { player = Bukkit.getPlayer(this.uuid); return (player != null && player.isOnline()); }
	public void addGroup(UUID group)	 { if(!this.groups.contains(group)) this.groups.add(group);    }
	public void leaveGroup(UUID group)	 { this.groups.remove(group); }
	public ArrayList<Group> getGroups()  { return GroupRegistry.inst().getExistingGroups(groups); }
	public ArrayList<String> getGroupsNames() { return GroupRegistry.inst().getGroupsNames(groups); }
	public void setActiveGroupChat(UUID group) { this.group_chat = group; }
	public UUID getActiveGroupChat() { return group_chat; }
	public String getName() { 
		if(player != null) return player.getName();
		else return Espero.OFFLINE_PLAYERS.getName(uuid);
	}
	public ArrayList<ItemStack> getRespawnInventory() { return respawn_inventory; }
	public void clearRespawnInventory() { respawn_inventory.clear(); }
	public void addToRespawnInventory(ItemStack item) { respawn_inventory.add(item); }
	public void addToRespawnInventory(Collection<? extends ItemStack> items) { respawn_inventory.addAll(respawn_inventory); }
	public void setRespawnInventory(ArrayList<ItemStack> items) { this.respawn_inventory = items; }
	
	public void message(String message, boolean save) {
		if(player != null) Messenger.send(player, message);
		else if(save) missed_messages.add(message);
	}
	
	public void brief() {
		if(player == null || missed_messages.isEmpty()) return;
		Messenger.send(player, Color.GRAY + "-=" + Color.WHEAT + " You've Missed " + Format.SUBHEADER_VALUE.message(missed_messages.size() + "") + " Messages " + Color.GRAY + "=-");
		for(String message : missed_messages)
			Messenger.send(player, message);
		missed_messages.clear();
	}
	
	
	public void addRift(int count) {
		rifts += count;
		max_sanity = 100 - (20 * rifts);
		if(sanity > max_sanity) sanity = max_sanity;
	}
	
	
	@Override
	protected void save(Loader loader) {
		if(nation != null) this.config.set("nation", nation.toString());
		else               this.config.set("nation", "");
		this.config.set("rift-count", rifts);
		this.config.set("sanity.cur-sanity", sanity);
		this.config.set("sanity.max-sanity", max_sanity);
		
		
		int prev_size = config.getInt("respawn_inventory.size");
		for(int i = 0; i < prev_size; i++)
			this.config.set("respawn_inventory." + i, null);
		this.config.set("respawn_inventory.size", respawn_inventory.size());
		int i = 0; for(ItemStack item : respawn_inventory) {
			this.config.set("respawn_inventory." + i, item);
		i++;	   }		
		
		ArrayList<String> sgroups = new ArrayList<String>();
		for(UUID group : groups) sgroups.add(group.toString());
		this.config.set("groups", sgroups);
		
		this.config.set("missed-messages", missed_messages);
		
		super.save(loader);
	}
	
	@Override
	protected void load(Loader loader) {
		super.load(loader);

		//TODO: Check if config#getString() can return empty string for null strings
		if(config.getString("nation") != null && config.getString("nation").length() > 0) nation = UUID.fromString(config.getString("nation"));

		respawn_inventory = new ArrayList<ItemStack>();
		int respawn_inventory_size = config.getInt("respawn_inventory.size");
		for(int i = 0; i < respawn_inventory_size; i++)
			respawn_inventory.add(config.getItemStack("respawn_inventory." + i));
		
		rifts = config.getInt("rift-count");
		sanity = config.getInt("sanity.cur-sanity");
		max_sanity = config.getInt("sanity.max-sanity");
		groups = ConfigReader.readUUIDList(config, "groups");
		missed_messages = new ArrayList<String>(config.getStringList("missed-messages"));
	}
	
	
	
	/*
	 * Check if a player file has generated
	 */
	public static boolean hasGenerated(UUID uuid) {
		return Espero.LOADER.datafile("playerdata/" + uuid + ".yml").exists();
	}
	
}
