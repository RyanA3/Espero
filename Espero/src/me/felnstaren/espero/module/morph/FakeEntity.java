package me.felnstaren.espero.module.morph;

import java.util.Random;
import java.util.UUID;

import me.felnstaren.felib.packet.enums.PacketEntityType;

public class FakeEntity {

	private static final Random RANDOM = new Random();
	
	private PacketEntityType type;
	private int id;
	private UUID uuid;
	
	public FakeEntity(PacketEntityType type) {
		this.id = -1000 + RANDOM.nextInt(1000);
		this.type = type;
		uuid = UUID.randomUUID();
	}
	
	
	
	public int getEntityId() {
		return id;
	}
	
	public UUID getUniqueId() {
		return uuid;
	}
	
	public PacketEntityType getType() {
		return type;
	}
	
}
