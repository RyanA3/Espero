package me.felnstaren.espero.config;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.config.ConfigObjectManager;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.util.data.SearchObject;

public class EsperoPlayerManager extends ConfigObjectManager<EsperoPlayer> implements Listener {

	public EsperoPlayerManager(JavaPlugin plugin) {
		super(Espero.LOADER);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	//Load player into memory on join
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.add(new EsperoPlayer(event.getPlayer()));
	}
	
	//Unload player from memory and save config file on quit
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.remove(SearchObject.getIndexValue(event.getPlayer().getUniqueId()));
	}
	
	
	
	public EsperoPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}
	
	public EsperoPlayer getPlayer(UUID id) {
		Espero.LOGGER.log(Level.DEBUG, "Searching Players for " + id);
		EsperoPlayer ep = get(SearchObject.getIndexValue(id));
		if(ep != null) { Espero.LOGGER.log(Level.DEBUG, "Found Loaded Player"); return (EsperoPlayer) ep;  }
		Espero.LOGGER.log(Level.DEBUG, "Player Not Found, loading... ");
		ep = new EsperoPlayer(id);
		add(ep);
		return ep;
	}

}
