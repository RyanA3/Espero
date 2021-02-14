package me.felnstaren.espero.module.morph;

import java.util.Random;
import java.util.UUID;

import me.felnstaren.felib.packet.PacketEntityType;

public class FakeEntity {

	private static final Random random = new Random();
	
	private PacketEntityType type;
	private int id;
	private UUID uuid;
	
	public FakeEntity(PacketEntityType type) {
		this.id = -1000 + random.nextInt(1000);
		this.type = type;
		uuid = UUID.randomUUID();
	}
	
	
	
	public int getId() {
		return id;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public PacketEntityType getType() {
		return type;
	}
	
}
