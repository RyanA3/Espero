package me.felnstaren.espero.module.morph;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.packet.enums.PacketEntityType;
import me.felnstaren.felib.packet.listener.PacketEvent;
import me.felnstaren.felib.packet.listener.PacketListener;
import me.felnstaren.felib.packet.wrapper.data.DataWatcherWrapper;
import me.felnstaren.felib.packet.wrapper.meta.EntityMetaPacket;
import me.felnstaren.felib.reflect.Packeteer;
import me.felnstaren.felib.reflect.Reflector;

public class MorphManager implements PacketListener {

	private ArrayList<MorphedPlayer> morphs = new ArrayList<MorphedPlayer>();
	private static final String[] SPOOF = {
		"PacketPlayOutEntityHeadRotation",
		"PacketPlayOutEntityTeleport",
		"PacketPlayOutRelEntityMove",
		"PacketPlayOutRelEntityMoveLook",
		"PacketPlayOutEntityLook",
		"PacketPlayOutEntity",
		"PacketPlayOutEntityVelocity",
		"PacketPlayOutEntityStatus"
	};
	
	private static final String[] UNSPOOF = {
		"PacketPlayInUseEntity"
	};
	
	private static final String[] REPLACE = { "PacketPlayOutNamedEntitySpawn" }; 

	public MorphManager() {
		
	}
	
	

	public void onEvent(PacketEvent event) {
		if(event.getPacket().getClass().getSimpleName().contains("Entity")) Espero.LOGGER.log(Level.DEBUG, event.getPacket().getClass().getSimpleName());;
		
		if(event.isPacket("PacketPlayOutNamedEntitySpawn")) {
			int entity_id = (int) Reflector.getDeclaredFieldValue(event.getPacket(), "a");
			MorphedPlayer morph = getMorphByPlayer(entity_id);
			if(morph == null) return;
			event.setCancelled(true);
			Packeteer.sendClientPacket(event.getPlayer(), morph.spawnEntityPacket());
			return;
		}
		
		if(event.isPacket(SPOOF)) {
			
			int entity_id = (int) Reflector.getDeclaredFieldValue(event.getPacket(), "a");
			MorphedPlayer morph = getMorphByPlayer(entity_id);
			if(morph == null) return;
			//if(event.getPlayer().equals(morph.getPlayer())) return;
			Espero.LOGGER.log(Level.DEBUG, "Spoof " + event.getPacket().getClass().getSimpleName());
			Reflector.setDeclaredFieldValue(event.getPacket(), "a", morph.getFakeEntityId());
			return;
		}
		
		if(event.isPacket(UNSPOOF)) {
			int entity_id = (int) Reflector.getDeclaredFieldValue(event.getPacket(), "a");
			Espero.LOGGER.log(Level.DEBUG, "Check should unspoof: " + entity_id);
			MorphedPlayer morph = getMorphByFaked(entity_id);
			if(morph == null) return;
			Espero.LOGGER.log(Level.DEBUG, "Unspoof " + event.getPacket().getClass().getSimpleName());
			Reflector.setDeclaredFieldValue(event.getPacket(), "a", morph.getPlayer().getEntityId());
		}
	}
	
	public void addMorph(Player player, PacketEntityType type) {
		MorphedPlayer morph = new MorphedPlayer(player, type);
		Object spawn_packet = morph.spawnEntityPacket();
		Object destroy_packet = morph.destroyPlayerPacket();
		Object meta_packet = morph.metadataPacket();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
		//	if(player.getEntityId() == p.getEntityId()) continue;
		//	Packeteer.sendClientPacket(p, destroy_packet);
			Packeteer.sendClientPacket(p, spawn_packet);
			Packeteer.sendClientPacket(p, meta_packet);
		}
		
		Espero.LOGGER.log(Level.DEBUG, "Morph added for " + player.getName() + " morph id " + morph.getFakeEntityId());
		morphs.add(morph);
	}
	
	public void removeMorph(Player player) {
		MorphedPlayer morph = getMorphByPlayer(player.getEntityId());
		if(morph == null) return;
		
		Object spawn_packet = morph.spawnPlayerPacket();
		Object destroy_packet = morph.destroyEntityPacket();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(player.getEntityId() == p.getEntityId()) continue;
			Packeteer.sendClientPacket(p, destroy_packet);
			Packeteer.sendClientPacket(p, spawn_packet);
		}
		
		Espero.LOGGER.log(Level.DEBUG, "Morph removed for " + player.getName() + " morph id " + morph.getFakeEntityId());
		morphs.remove(morph);
		Espero.LOGGER.log(Level.DEBUG, "Total players morphed " + morphs.size());
	}
	
	public MorphedPlayer getMorphByPlayer(int entity_id) {
		for(MorphedPlayer m : morphs)
			if(m.getPlayer().getEntityId() == entity_id) return m;
		return null;
	}
	
	public MorphedPlayer getMorphByFaked(int entity_id) {
		for(MorphedPlayer m : morphs)
			if(m.getFakeEntityId() == entity_id) return m;
		return null;
	}
	
	public MorphedPlayer getMorph(UUID uuid) {
		for(MorphedPlayer m : morphs)
			if(m.getPlayer().getUniqueId().equals(uuid)) return m;
		return null;
	}
	
	public void shutdown() {
		for(Player p : Bukkit.getOnlinePlayers())
			removeMorph(p);
	}
	
	
	
}
