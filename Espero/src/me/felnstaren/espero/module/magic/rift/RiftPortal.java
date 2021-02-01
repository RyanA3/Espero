package me.felnstaren.espero.module.magic.rift;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.logger.Level;

public class RiftPortal {

	private Player traveller;
	private Location location;
	private boolean loaded = false;
	
	public RiftPortal(Location location) {
		this.location = location;
	}
	
	
	public void update() {
		if(loaded && traveller != null) return;
		if(!location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) { loaded = false; return; }
		loaded = true;
		if(traveller != null) return;
		Espero.LOGGER.log(Level.INFO, "updated");
		Entity[] entities = location.getChunk().getEntities();
		for(int i = 0; i < entities.length; i++) {
			if(!(entities[i] instanceof Player)) continue;
			if(entities[i].getLocation().distance(location) > 1) continue;
			traveller = (Player) entities[i];
			break;
		}
	}
	
	public void display() {
		if(!loaded) return;
		Espero.LOGGER.log(Level.INFO, "displayed");
		location.getWorld().spawnParticle(Particle.SQUID_INK, location, 25, 0.15, 0.5, 0.15, 0);
	}
	
	
	
	public Player getTraveller() {
		return traveller;
	}
	
	public void clearTraveller() {
		this.traveller = null;
	}
	
	public Location getLocation() {
		return location;
	}
	
}
