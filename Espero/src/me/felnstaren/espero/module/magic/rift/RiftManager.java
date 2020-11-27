package me.felnstaren.espero.module.magic.rift;

import java.util.ArrayList;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RiftManager {

	public static RiftManager INSTANCE;
	
	public static RiftManager getInstance() {
		return INSTANCE;
	}
	
	public static void init(Plugin plugin) {
		INSTANCE = new RiftManager(plugin);
	}
	
	
	
	private ArrayList<Rift> rifts = new ArrayList<Rift>();
	private ArrayList<Rift> queue_remove = new ArrayList<Rift>();
	private ArrayList<Rift> queue_add = new ArrayList<Rift>();
	
	private boolean updating = false;
	private boolean displaying = false;
	
	public RiftManager(Plugin plugin) {
		new BukkitRunnable() {
			boolean update = true;
			public void run() {
				update = !update;
				if(update) { update(); dumpl(); }
				display();
			}
		}.runTaskTimer(plugin, 50, 5);
	}
	
	public void update() {
		updating = true;
		
		for(Rift rift : rifts)
			rift.update();
		
		updating = false;
	}
	
	public void display() {
		displaying = true;
		
		for(Rift rift : rifts)
			rift.display();
		
		displaying = false;
	}
	
	
	
	public Rift register(Rift rift) {
		if(!displaying && !updating) {
			rift.init();
			rifts.add(rift);
		} else {
			queue_add.add(rift);
		}
		
		return rift;
	}
	
	public Rift unregister(Rift rift) {
		if(!displaying && !updating) {
			rifts.remove(rift);
		} else {
			queue_remove.add(rift);
		}
		
		return rift;
	}
	
	private void dumpl() {
		if(!displaying && !updating) {
			for(Rift rift : rifts) if(rift.isDestroy()) queue_remove.add(rift);
			for(Rift rift : queue_add) rifts.add(rift);
			for(Rift rift : queue_remove) rifts.remove(rift);
			queue_add.clear();
			queue_remove.clear();
		}
	}
	
}
