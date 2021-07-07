package me.felnstaren.espero.module.morph.morph;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.morph.FakeEntity;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.packet.enums.ByteInfo;
import me.felnstaren.felib.packet.enums.MetadataValue;
import me.felnstaren.felib.packet.enums.PacketEntityType;
import me.felnstaren.felib.packet.listener.PacketEvent;
import me.felnstaren.felib.packet.wrapper.EntityDestroyPacket;
import me.felnstaren.felib.packet.wrapper.EntityMetaPacket;
import me.felnstaren.felib.packet.wrapper.EntityNamedSpawnPacket;
import me.felnstaren.felib.packet.wrapper.EntitySpawnPacket;
import me.felnstaren.felib.packet.wrapper.PacketWrapper;
import me.felnstaren.felib.packet.wrapper.data.DataWatcherWrapper;
import me.felnstaren.felib.reflect.Packeteer;
import me.felnstaren.felib.util.ArrayUtil;

public class EntityMorph {

	protected static final String[] MOVEMENT_PACKETS = {
			"PacketPlayOutEntityHeadRotation",
			"PacketPlayOutEntityTeleport",
			"PacketPlayOutRelEntityMove",
			"PacketPlayOutRelEntityMoveLook",
			"PacketPlayOutEntityLook",
			"PacketPlayOutEntity",
			"PacketPlayOutEntityVelocity",
			"PacketPlayOutEntityStatus",
			"PacketPlayOutVehicleMove"
	};
	
	
	
	protected Player player;
	protected FakeEntity entity;
	protected HashMap<MetadataValue, Object> properties = new HashMap<MetadataValue, Object>();
	
	public EntityMorph(Player player, PacketEntityType type) {
		this.player = player;
		this.entity = new FakeEntity(type);
	}
	
	
	
	public void handlePacket(PacketEvent event) {
		if(event.getPlayer().getEntityId() == player.getEntityId()) {
			Espero.LOGGER.log(Level.DEBUG, "IGNORE ->self-> " + event.getPacket().getClass().getSimpleName());
			return;
		}
		Espero.LOGGER.log(Level.DEBUG, "HANDLE " + event.getPacket().getClass().getSimpleName());
		if(event.isPacket(MOVEMENT_PACKETS)) handleMovementPacket(event);
		else if(event.isPacket("PacketPlayOutEntityMetadata")) handleMetadataPacket(event);
		else if(event.isPacket("PacketPlayOutNamedEntitySpawn")) handleSpawnPacket(event);
		else if(event.isPacket("PacketPlayInUseEntity")) handleInteractPacket(event);
	}
	
	public void doMorph() {
		Object destroy_packet = generatePlayerDestroyPacket();
		Object spawn_packet = generateEntitySpawnPacket();
		Object meta_packet = generateEntityMetadataPacket();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getEntityId() == player.getEntityId()) continue;
			Packeteer.sendClientPacket(p, destroy_packet);
			Packeteer.sendClientPacket(p, spawn_packet);
			Packeteer.sendClientPacket(p, meta_packet);
		}
	}
	
	public void doUnmorph() {
		Object destroy_packet = generateEntityDestroyPacket();
		Object spawn_packet = generatePlayerSpawnPacket();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getEntityId() == player.getEntityId()) continue;
			Packeteer.sendClientPacket(p, destroy_packet);
			Packeteer.sendClientPacket(p, spawn_packet);
		}
	}

	
	
	//Handle outgoing movement packets
	protected void handleMovementPacket(PacketEvent event) {
		PacketWrapper packet = new PacketWrapper(event.getPacket());
		packet.set("a", entity.getEntityId());
		//Espero.LOGGER.log(Level.DEBUG, "Spoofed OUT move " + entity.getEntityId());
	}
	
	//Handle outgoing spawning packets
	protected void handleSpawnPacket(PacketEvent event) {
		event.setCancelled(true);
		Packeteer.sendClientPacket(event.getPlayer(), generateEntitySpawnPacket());
		Packeteer.sendClientPacket(event.getPlayer(), generateEntityMetadataPacket());
		Espero.LOGGER.log(Level.DEBUG, "Replaced OUT spawn " + entity.getEntityId());
	}
	
	//Handle outgoing metadata packets
	protected void handleMetadataPacket(PacketEvent event) {
		//event.setCancelled(true);
		//Packeteer.sendClientPacket(event.getPlayer(), generateEntityMetadataPacket());
		//Espero.LOGGER.log(Level.DEBUG, "Replaced OUT meta " + entity.getEntityId());
		EntityMetaPacket packet = new EntityMetaPacket(event.getPacket());
		packet.set("a", entity.getEntityId());
	}
	
	//Handle incoming interact packets
	protected void handleInteractPacket(PacketEvent event) {
		PacketWrapper packet = new PacketWrapper(event.getPacket());
		packet.set("a", player.getEntityId());
		//Espero.LOGGER.log(Level.DEBUG, "Unspoofed IN interact " + player.getEntityId());
	}
	
	
	
	//Generate a new spawn packet for the morph with all the player's current data
	protected Object generateEntitySpawnPacket() {
		return new EntitySpawnPacket(entity.getEntityId(), entity.getUniqueId(), entity.getType(), player.getLocation(), player.getVelocity()).getNMSPacket();
	}
	
	//Generate a new spawn packet for the player with all the player's current data
	protected Object generatePlayerSpawnPacket() {
		return new EntityNamedSpawnPacket(true, player).getNMSPacket();
	}
	
	//Generate a new metadata packet with all the player's current data
	protected Object generateEntityMetadataPacket() {
		EntityMetaPacket packet = new EntityMetaPacket(entity.getEntityId(), getCurrentWatcher());
		return packet.getNMSPacket();
	}
	
	//Generate a new destroy packet for the player entity
	protected Object generatePlayerDestroyPacket() {
		return new EntityDestroyPacket(true, player.getEntityId()).getNMSPacket();
	}
	
	//Generate a new destroy packet for the fake entity
	protected Object generateEntityDestroyPacket() {
		return new EntityDestroyPacket(true, entity.getEntityId()).getNMSPacket();
	}
	
	
	
	//Generate a new data watcher with values from the player's data watcher
	protected DataWatcherWrapper getCurrentWatcher() {
		DataWatcherWrapper entity_watcher = new DataWatcherWrapper(null, true);
		entity_watcher.register(MetadataValue.ENTITY_INFO_BYTE, genEntityInfoByte());
		//entity_watcher.register(MetadataValue.ENTITY_CUSTOM_NAME, Optional.of(Reflector.newInstanceOf("ChatComponentText", player.getDisplayName())));
		//entity_watcher.register(MetadataValue.ENTITY_IS_CUSTOM_NAME_VISIBLE, true);
		entity_watcher.register(MetadataValue.LIVING_ENTITY_HEALTH, (float) player.getHealth());
		for(MetadataValue value_type : properties.keySet()) entity_watcher.register(value_type, properties.get(value_type));
		//entity_watcher.register(MetadataValue.LIVING_ENTITY_INFO_BYTE, ByteInfo.createWithSet(ByteInfo.LIVING_ENTITY_IS_RIPTIDING));
		return entity_watcher;
	}
	
	protected byte genEntityInfoByte() {
		ArrayList<ByteInfo> infos = new ArrayList<ByteInfo>();
		if(player.getFireTicks() > 0) infos.add(ByteInfo.ENTITY_IS_ON_FIRE);
		if(player.isGlowing()) infos.add(ByteInfo.ENTITY_IS_GLOWING);
		if(player.isSneaking()) infos.add(ByteInfo.ENTITY_IS_CROUDHING);
		if(player.isInvisible()) infos.add(ByteInfo.ENTITY_IS_INVISIBLE);
		if(player.isSprinting()) infos.add(ByteInfo.ENTITY_IS_SPRINTING);
		if(player.isGliding()) infos.add(ByteInfo.ENTITY_IS_GLIDING);
		if(player.isSwimming()) infos.add(ByteInfo.ENTITY_IS_SWIMMING);
		return ByteInfo.createWithSet(ArrayUtil.arrayver(infos, ByteInfo.class));
	}
	
	
	
	public HashMap<MetadataValue, Object> getProperties() {
		return properties;
	}
	
	public FakeEntity getFakeEntity() {
		return entity;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isReceiverForInfos(Player player) {
		return false;
	}
	
}
