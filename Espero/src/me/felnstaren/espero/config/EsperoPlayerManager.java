package me.felnstaren.espero.config;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.config.ConfigObject;
import me.felnstaren.felib.config.ConfigObjectManager;
import me.felnstaren.felib.logger.Level;

public class EsperoPlayerManager extends ConfigObjectManager implements Listener {

	public EsperoPlayerManager(JavaPlugin plugin) {
		super(Espero.LOADER);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.add(new EsperoPlayer(event.getPlayer()));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.remove(event.getPlayer().getUniqueId().clockSequence());
	}
	
	
	
	public EsperoPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}
	
	public EsperoPlayer getPlayer(UUID id) {
		Espero.LOGGER.log(Level.DEBUG, "Searching Players for " + id);
		ConfigObject ep = get(id.clockSequence());
		if(ep != null) { Espero.LOGGER.log(Level.DEBUG, "Found Loaded Player"); return (EsperoPlayer) ep;  }
		Espero.LOGGER.log(Level.DEBUG, "Player Not Found, loading... ");
		ep = new EsperoPlayer(id);
		add(ep);
		return (EsperoPlayer) ep;
	}

}
