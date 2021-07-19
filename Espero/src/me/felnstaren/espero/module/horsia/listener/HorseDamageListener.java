package me.felnstaren.espero.module.horsia.listener;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class HorseDamageListener implements Listener {
	
	private static final HashMap<Material, Float> armor_damage_modifiers = new HashMap<Material, Float>();
	static {
		armor_damage_modifiers.put(Material.LEATHER_HORSE_ARMOR, 0.8f);
		armor_damage_modifiers.put(Material.IRON_HORSE_ARMOR, 0.7f);
		armor_damage_modifiers.put(Material.GOLDEN_HORSE_ARMOR, 0.6f);
		armor_damage_modifiers.put(Material.DIAMOND_HORSE_ARMOR, 0.6f);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Horse)) return;
		Horse horse = (Horse) event.getEntity();
		if(horse.getInventory().getArmor() == null) return;
		Material armor = horse.getInventory().getArmor().getType();
		DamageCause cause = event.getCause();
		
		float mod = armor_damage_modifiers.get(armor);
		if(armor == Material.LEATHER_HORSE_ARMOR) {
			if(cause == DamageCause.PROJECTILE) mod -= 0.5f;
			else if(cause == DamageCause.BLOCK_EXPLOSION) mod -= 0.5f;
			else if(cause == DamageCause.ENTITY_EXPLOSION) mod -= 0.5;
		} else if(armor == Material.IRON_HORSE_ARMOR) {
			if(cause == DamageCause.ENTITY_ATTACK) mod -= 0.2f;
			else if(cause == DamageCause.PROJECTILE) mod -= 0.2f;
			else if(cause == DamageCause.FIRE) mod += 0.3;
			else if(cause == DamageCause.HOT_FLOOR) mod += 0.8;
			else if(cause == DamageCause.LAVA) mod += 0.8;
		} else if(armor == Material.GOLDEN_HORSE_ARMOR) {
			if(cause == DamageCause.FIRE) mod -= 0.35;
			else if(cause == DamageCause.HOT_FLOOR) mod -= 0.6;
			else if(cause == DamageCause.LAVA) mod -= 0.35;
			else if(cause == DamageCause.ENTITY_ATTACK) mod += 0.2;
			else if(cause == DamageCause.PROJECTILE) mod += 0.2;
		} else if(armor == Material.DIAMOND_HORSE_ARMOR) {
			
		}

		if(mod <= 0.01) {
			event.setCancelled(true);
			return;
		}
		
		event.setDamage(event.getDamage() * mod);
	}
	
}
