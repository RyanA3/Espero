package me.felnstaren.espero.module.clogger;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;

public class CombatListener implements Listener {

	private static final int COMBAT_TIME = 15;
	
	private CombatTimeHandler chandler;
	
	public CombatListener(CombatTimeHandler chandler) {
		this.chandler = chandler;
	}
	
	
	
	@EventHandler
	public void combatEvent(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player victim = (Player) event.getEntity();
		
		Player damager = null;
		if(event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			if(projectile.getShooter() instanceof Player) damager = (Player) projectile.getShooter();
		} else if(event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
		} else return;
		if(damager == null) return;
		
		if(!chandler.isInCombat(victim)) {
			Messenger.send(victim, Color.RED + "You are now in combat for " + Color.LIGHT_GRAY + COMBAT_TIME + Color.RED + "seconds");
			chandler.register(victim, COMBAT_TIME);
		} else chandler.set(victim, COMBAT_TIME);
		if(!chandler.isInCombat(damager)) {
			Messenger.send(damager, Color.RED + "You are now in combat for " + Color.LIGHT_GRAY + COMBAT_TIME + Color.RED + "seconds");
			chandler.register(damager, COMBAT_TIME);
		} else chandler.set(damager, COMBAT_TIME);
	}
	
}
