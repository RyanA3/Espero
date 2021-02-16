package me.felnstaren.espero.module.morph.morph;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.packet.enums.PacketEntityType;
import me.felnstaren.felib.packet.listener.PacketEvent;
import me.felnstaren.felib.packet.wrapper.PacketWrapper;
import me.felnstaren.felib.reflect.Packeteer;
import me.felnstaren.felib.reflect.Reflector;

public class RideableMorph extends EntityMorph {

	protected Player rider;
	protected boolean is_notif_mount_packet = true;
	
	public RideableMorph(Player player, PacketEntityType type) {
		super(player, type);
	}
	
	
	
	@Override
	public void doUnmorph() {
		super.doUnmorph();
		if(rider != null) player.removePassenger(rider);
		rider = null;
	}
	
	
	
	@Override
	public void handlePacket(PacketEvent event) {
		super.handlePacket(event);
		if(event.getPlayer().getEntityId() == player.getEntityId()) return;
		if(event.isPacket("PacketPlayOutMount")) handleMountPacket(event);
	}
	
	@Override
	protected void handleMovementPacket(PacketEvent event) {
		super.handleMovementPacket(event);
		if(rider != null && event.getPlayer().getEntityId() == rider.getEntityId()) {
			Location location = player.getLocation();
			PacketWrapper packet = new PacketWrapper(Reflector.newInstanceOf("PacketPlayOutVehicleMove"));
			packet.set("a", location.getX());
			packet.set("b", location.getY());
			packet.set("c", location.getZ());
			packet.set("d", location.getYaw());
			packet.set("e", location.getPitch());
			
			Packeteer.sendClientPacket(rider, packet.getNMSPacket());
		}
	}
	
	@Override
	protected void handleInteractPacket(PacketEvent event) {
		PacketWrapper packet = new PacketWrapper(event.getPacket());
		packet.set("a", player.getEntityId());
		if(packet.get("action") != Reflector.getNMSClass("PacketPlayInUseEntity$EnumEntityUseAction").getEnumConstants()[0]) return;
		
		if(rider == null) {
			is_notif_mount_packet = true;
			rider = event.getPlayer();
			if(rider == null) return;
			doMount(rider);
		}
	}
	
	protected void handleMountPacket(PacketEvent event) {
		Reflector.setDeclaredFieldValue(event.getPacket(), "a", entity.getEntityId());
		if(is_notif_mount_packet) { is_notif_mount_packet = false; return; }
		if(rider == null) return;
		doDismount(rider);
		rider = null;
	}
	
	protected void doMount(Player toadd) {
		new BukkitRunnable() {
			public void run() {
				player.addPassenger(toadd);
				Packeteer.sendClientPacket(player, generateRiderMountPacket());
				Espero.LOGGER.log(Level.DEBUG, "Added rider " + toadd.getName());
			}
		}.runTask(Bukkit.getPluginManager().getPlugin("Espero"));
	}
	
	protected void doDismount(Player toremove) {
		new BukkitRunnable() {
			public void run() {
				player.removePassenger(toremove);
				Packeteer.sendClientPacket(player, generateRiderMountPacket());
				Espero.LOGGER.log(Level.DEBUG, "Removed rider " + toremove.getName());
			}
		}.runTask(Bukkit.getPluginManager().getPlugin("Espero"));
	}
	
	protected Object generateRiderMountPacket() {
		PacketWrapper packet = new PacketWrapper(Reflector.newInstanceOf("PacketPlayOutMount"));
		packet.set("a", player.getEntityId());
		if(rider != null) packet.set("b", new int[] { rider.getEntityId() });
		else packet.set("b", new int[0]);
		return packet.getNMSPacket();
	}
	
	
	
	public Player getRider() {
		return rider;
	}

}
