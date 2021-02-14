package me.felnstaren.espero.module.morph;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.packet.PacketEntityType;
import me.felnstaren.felib.packet.listener.PacketEvent;
import me.felnstaren.felib.packet.wrapper.PacketWrapper;
import me.felnstaren.felib.reflect.Reflector;
import me.felnstaren.felib.util.Maths;

public class MorphedPlayer {

	private Player player;
	private FakeEntity entity;
	
	public MorphedPlayer(Player player, PacketEntityType type) {
		this.player = player;
		this.entity = new FakeEntity(type);
	}
	
	public void handleMovePacket(PacketEvent event) {
		PacketWrapper packet = new PacketWrapper(event.getPacket());
		packet.set("a", entity.getId());
		Espero.LOGGER.log(Level.DEBUG, "Changed movement to id " + entity.getId());
	}
	
	
	
	public Player getPlayer() {
		return player;
	}
	
	public int getFakeEntityId() {
		return entity.getId();
	}
	
	public Object spawnEntityPacket() {
		Object opack = Reflector.newInstanceOf("PacketPlayOutSpawnEntityLiving");
		if(opack == null) return null;
		
		Vector v = player.getVelocity();
		Location l = player.getLocation();
		PacketWrapper packet = new PacketWrapper(opack);
		packet.set("a", entity.getId());
		packet.set("b", entity.getUUID());
		packet.set("c", entity.getType().ordinal());
		packet.set("d", l.getX());
		packet.set("e", l.getY());
		packet.set("f", l.getZ());
		packet.set("g", (int) Maths.clamp(v.getX(), -3.9D, 3.9D));
		packet.set("h", (int) Maths.clamp(v.getY(), -3.9D, 3.9D));
		packet.set("i", (int) Maths.clamp(v.getZ(), -3.9D, 3.9D));
		packet.set("j", (byte) ((int) l.getYaw() * 256.0F / 360.0F ));
		packet.set("k", (byte) ((int) l.getPitch() * 256.0F / 360.0F));
		packet.set("l", (byte) ((int) l.getYaw() * 256.0F / 360.0F)); // <-- aC seems to just be a duplicate of yaw? for some reason
		
		return packet.getPacket();
	}
	
	public Object destroyEntityPacket() {
		Object opack = Reflector.newInstanceOf("PacketPlayOutEntityDestroy");
		if(opack == null) return null;
		
		
		PacketWrapper packet = new PacketWrapper(opack);
		packet.set("a", new int[] { entity.getId() });
		return packet.getPacket();
	}
	
	public Object spawnPlayerPacket() {
		Object opack = Reflector.newInstanceOf("PacketPlayOutNamedEntitySpawn");
		if(opack == null) return null;
		
		Location l = player.getLocation();
		PacketWrapper packet = new PacketWrapper(opack);
		packet.set("a", player.getEntityId());
		packet.set("b", player.getUniqueId());
		packet.set("c", l.getX());
		packet.set("d", l.getY());
		packet.set("e", l.getZ());
		packet.set("f", (byte) ((int) l.getYaw() * 256.0D / 360.0D));
		packet.set("g", (byte) ((int) l.getPitch() * 256.0D / 360.0D));
		
		return packet.getPacket();
	}
	
	public Object destroyPlayerPacket() {
		Object opack = Reflector.newInstanceOf("PacketPlayOutEntityDestroy");
		if(opack == null) return null;
		
		PacketWrapper packet = new PacketWrapper(opack);
		packet.set("a", new int[] { player.getEntityId() });
		return packet.getPacket();
	}
	
}
