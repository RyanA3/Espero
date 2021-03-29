package me.felnstaren.espero.module.clogger;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;

public class CombatLimiter implements Listener {

	private static final ArrayList<String> BLACKLIST = new ArrayList<String>();
	static {
		BLACKLIST.add("tp");
		BLACKLIST.add("home");
	}
	
	private CombatTimeHandler chandler;
	
	public CombatLimiter(CombatTimeHandler chandler) {
		this.chandler = chandler;
	}
	
	
	
	@EventHandler
	public void beforeCommand(PlayerCommandPreprocessEvent event) {
		String label = event.getMessage().split(" ")[0].toLowerCase();
		if(!BLACKLIST.contains(label)) return;
		if(!chandler.isInCombat(event.getPlayer())) return;
		Messenger.send(event.getPlayer(), Color.RED + "You cannot use this command while in combat! " + Format.ARG.message(chandler.getCombatTime(event.getPlayer()) + ""));
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onLog(PlayerQuitEvent event) {
		if(!chandler.isInCombat(event.getPlayer())) return;
		//event.getPlayer().damage(100.0);
		Messenger.broadcast(Color.RED + event.getPlayer().getDisplayName() + Color.RED + " logged out in combat! What a baby!");
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if(!chandler.isInCombat(event.getPlayer())) return;
		event.setCancelled(true);
		Messenger.send(event.getPlayer(), Color.RED + "You cannot teleport while in combat! " + Format.ARG.message(chandler.getCombatTime(event.getPlayer()) + ""));
	}
	
}
