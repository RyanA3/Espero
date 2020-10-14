package me.felnstaren.espero.module.magic.rift;

import org.bukkit.entity.Player;

public class TemporaryRift extends Rift {

	protected int updates;
	
	public TemporaryRift(RiftPortal a, RiftPortal b, int updates) {
		super(a, b);
		this.updates = updates;
	}
	
	public TemporaryRift(RiftPortal a, RiftPortal b, Player owner, int updates) {
		super(a, b, owner);
		this.updates = updates;
	}
	
	
	
	public void update() {
		updates--;
		if(updates == 0) destroy();
		if(destroy) return;
		super.update();
	}

}
