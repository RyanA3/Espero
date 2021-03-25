package me.felnstaren.espero.module.nations.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.felib.util.data.BinarySearchable;
import me.felnstaren.felib.util.data.SearchObject;

public class NationPlayerChatManager extends BinarySearchable<EsperoPlayer> implements Listener {
	
	private static final String NCHAT_PREFIX = "#777[#FF0N#777]#AFA";

	private static NationPlayerChatManager INSTANCE;
	
	public static void init(JavaPlugin plugin) {
		INSTANCE = new NationPlayerChatManager();
		plugin.getServer().getPluginManager().registerEvents(INSTANCE, plugin);
	}
	
	public static NationPlayerChatManager inst() {
		return INSTANCE;
	}

	
	
	public void register(EsperoPlayer player) {
		super.add(player);
	}
	
	public void register(Player player) {
		super.add(Espero.PLAYERS.getPlayer(player));
	}
	
	public void unregister(EsperoPlayer player) {
		super.remove(player);
	}
	
	public void unregister(Player player) {
		super.remove(Espero.PLAYERS.getPlayer(player));
	}
	
	public void toggle(EsperoPlayer player) {
		if(super.get(player) != null) super.remove(player);
		else super.add(player);
	}
	
	public void toggle(Player player) {
		toggle(Espero.PLAYERS.getPlayer(player));
	}
	
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if(event.isCancelled()) return;
		EsperoPlayer player = super.get(SearchObject.getIndexValue(event.getPlayer().getUniqueId()));
		if(player == null) return;
		event.setCancelled(true);
		
		String message = NCHAT_PREFIX + event.getPlayer().getDisplayName() + "   #AAA" + event.getMessage();
		player.getNation().broadcast(message);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		EsperoPlayer player = super.get(SearchObject.getIndexValue(event.getPlayer().getUniqueId()));
		if(player == null) return;
		super.remove(player);
	}
	
}
