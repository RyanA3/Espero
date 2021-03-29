package me.felnstaren.espero.module.nations.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.infoout.InfoMessage;
import me.felnstaren.espero.module.nations.infoout.InfoMessageController;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRelation;

public class NationRelationCombatListener implements Listener {
	
	private InfoMessageController imc;
	
	

	@EventHandler
	public void onWombat(EntityDamageByEntityEvent event) {
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
		
		EsperoPlayer evictim = Espero.PLAYERS.getPlayer(victim);
		EsperoPlayer edamager = Espero.PLAYERS.getPlayer(damager);
		Nation victim_nation = evictim.getNation();
		Nation damager_nation = edamager.getNation();
		if(victim_nation == null || damager_nation == null) return;
		
		if(victim_nation.getID().equals(damager_nation.getID())) {
			imc.send(damager, InfoMessage.CANT_COMBAT_NATION);
			event.setCancelled(true); return;
		}
		
		if(victim_nation.getRelation(damager_nation.getID()) == NationRelation.ALLY) {
			imc.send(damager, InfoMessage.CANT_COMBAT_ALLY);
			event.setCancelled(true); return;
		}
	}
	
}
