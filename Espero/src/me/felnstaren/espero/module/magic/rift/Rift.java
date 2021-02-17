package me.felnstaren.espero.module.magic.rift;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.util.WorldUtil;

public class Rift {
	
	protected RiftPortal a;
	protected RiftPortal b;
	protected int cooldown = 10;
	protected boolean destroy = false;
	protected Player owner = null;
	
	public Rift(RiftPortal a, RiftPortal b) {
		this.a = a;
		this.b = b;
	}
	
	public Rift(RiftPortal a, RiftPortal b, Player owner) {
		this.a = a;
		this.b = b;
		this.owner = owner;
	}
	
	public void update() {
		cooldown--;
		if(cooldown > 0) return;
		a.update();
		b.update();
		
		Player traveller;
		if(a.getTraveller() != null) {
			traveller = a.getTraveller();
			traveller.teleport(b.getLocation());
			a.clearTraveller();
		}
		else if(b.getTraveller() != null) {
			traveller = b.getTraveller();
			traveller.teleport(a.getLocation());
			b.clearTraveller();
		}
		else return;
		
		cooldown = 10;
	}
	
	public void display() {
		a.display();
		b.display();
	}
	
	public void init() {
		
	}
	
	
	
	public Player getOwner() {
		return owner;
	}
	
	
	
	public boolean isDestroy() {
		return destroy;
	}
	
	public void destroy() {
		this.destroy = true;
		if(owner != null) Espero.PLAYERS.getPlayer(owner).delRift();
	}
	
	
	
	public static void cast(Player owner, int time) {
		if(!owner.getWorld().getName().equals("world")) return;
		
		EsperoPlayer dp = Espero.PLAYERS.getPlayer(owner); //new EsperoPlayer(owner.getUniqueId());
		if(dp.getConfig().getInt("sanity.max-sanity") < 40) return;
		
		Location loc1 = owner.getLocation();
		Location loc2 = WorldUtil.findHighestBlock(Bukkit.getWorld("world_the_end"), loc1.getBlockX() / 10, loc1.getBlockZ() / 10);
		
		RiftPortal a = new RiftPortal(loc1.clone().add(0, 1, 0));
		RiftPortal b = new RiftPortal(loc2.clone().add(0, 1, 0));
		
		Rift rift;
		if(time > 0) rift = new TemporaryRift(a, b, owner, time);
		else rift = new Rift(a, b, owner);
		
		RiftManager.getInstance().register(rift);
		
		dp.addRift();
	}
	
}
