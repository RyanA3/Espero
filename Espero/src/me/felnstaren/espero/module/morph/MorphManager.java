package me.felnstaren.espero.module.morph;

import java.util.ArrayList;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.morph.morph.EntityMorph;
import me.felnstaren.felib.logger.Level;
import me.felnstaren.felib.packet.listener.PacketEvent;
import me.felnstaren.felib.packet.listener.PacketListener;
import me.felnstaren.felib.reflect.Reflector;
import me.felnstaren.felib.util.PrimitiveUtil;

public class MorphManager implements PacketListener {
	
	//The a field in these packets are the player's entity id
	private static final String[] USES_PLAYER_ID = {
			"PacketPlayOutEntityHeadRotation",
			"PacketPlayOutEntityTeleport",
			"PacketPlayOutRelEntityMove",
			"PacketPlayOutRelEntityMoveLook",
			"PacketPlayOutEntityLook",
			"PacketPlayOutEntity",
			"PacketPlayOutEntityVelocity",
			"PacketPlayOutEntityStatus",
			"PacketPlayOutNamedEntitySpawn",
			"PacketPlayOutEntityMetadata",
			"PacketPlayOutMount"
	};
	
	//The a field in these packets are the fake entity's entity id
	private static final String[] USES_ENTITY_ID = {
		"PacketPlayInUseEntity"
	};
	
	private ArrayList<EntityMorph> morphs = new ArrayList<EntityMorph>();
	
	public MorphManager() {
		
	}


	public void onEvent(PacketEvent event) {
		EntityMorph morph = getMorphByPacket(event);
		if(morph == null) return;
		
		morph.handlePacket(event);
	}
	
	
	
	public void morph(EntityMorph morph) {
		morph.doMorph();
		morphs.add(morph);
	}
	
	public void unmorph(EntityMorph morph) {
		morph.doUnmorph();
		morphs.remove(morph);
	}
	
	
	
	public void shutdown() {
		for(EntityMorph m : morphs)
			m.doUnmorph();
		morphs.clear();
	}
	
	public EntityMorph getMorphByEntity(int entity_id) {
		if(entity_id >= 0) return null;
		for(EntityMorph m : morphs)
			if(m.getFakeEntity().getEntityId() == entity_id) return m;
		return null;
	}
	
	public EntityMorph getMorphByPlayer(int player_id) {
		for(EntityMorph m : morphs)
			if(m.getPlayer().getEntityId() == player_id) return m;
		return null;
	}
	
	public EntityMorph getMorphByPacket(PacketEvent event) {
		//Espero.LOGGER.log(Level.DEBUG, "Finding morph by packet " + event.getPacket().getClass().getSimpleName());
		EntityMorph morph = null;
		if(event.isPacket(USES_ENTITY_ID)) {
			//FeLib.LOGGER.log(Level.DEBUG, "Finding by entity id");
			Object value = Reflector.getDeclaredFieldValue(event.getPacket(), "a");
			if(value == null || PrimitiveUtil.getPrimitiveVersion(value.getClass()) != int.class) return null;
			//FeLib.LOGGER.log(Level.DEBUG, "got entity id " + (int) value);
			morph = getMorphByEntity((int) value);
		}
		else if(event.isPacket(USES_PLAYER_ID)) {
			//FeLib.LOGGER.log(Level.DEBUG, "Finding by player id");
			Object value = Reflector.getDeclaredFieldValue(event.getPacket(), "a");
			if(value == null || PrimitiveUtil.getPrimitiveVersion(value.getClass()) != int.class) return null;
			//FeLib.LOGGER.log(Level.DEBUG, "got player id " + (int) value);
			morph = getMorphByPlayer((int) value);
		}
		return morph;
	}

}
