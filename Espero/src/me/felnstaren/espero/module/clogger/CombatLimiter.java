package me.felnstaren.espero.module.clogger;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;

public class CombatLimiter implements Listener {

	private static final ArrayList<String> BLACKLIST = new ArrayList<String>();
	static {
		BLACKLIST.add("tp");
		BLACKLIST.add("home");
	}
	
	private static final String[] INSULTS = { "%player% logged out while in combat! They're probably a Rivian", "%player% is probably slamming their desk right now!", "%player% soiled themself!",
			"%player% couldn't hold it together, sad!", "%player% fled in terror", "%player% gave way to the harsh reality, shame!", "%player% sacraficed their pride!", "%player% felt threatened by cod spell!",
			"%player% loudly proclaimed, \"Cringe!\" Chaos ensued"};
	private static final Random RANDOM = new Random();
	
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
		Messenger.broadcast(Color.RED + INSULTS[RANDOM.nextInt(INSULTS.length)].replaceAll("%player%", event.getPlayer().getName()));
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 0, 2400));
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if(!chandler.isInCombat(event.getPlayer())) return;
		event.setCancelled(true);
		Messenger.send(event.getPlayer(), Color.RED + "You cannot teleport while in combat! " + Format.ARG.message(chandler.getCombatTime(event.getPlayer()) + ""));
	}
	
}
