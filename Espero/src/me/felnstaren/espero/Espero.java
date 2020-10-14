package me.felnstaren.espero;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.magic.MagicModule;
import me.felnstaren.espero.module.magic.rift.Rift;
import me.felnstaren.espero.module.magic.rift.RiftManager;
import me.felnstaren.espero.module.magic.rift.RiftPortal;
import me.felnstaren.espero.module.magic.rift.TemporaryRift;
import me.felnstaren.espero.module.nations.NationsModule;
import me.felnstaren.espero.util.logger.Logger;

public class Espero extends JavaPlugin {

	public void onEnable() {
		Logger.init(this);
		
		MagicModule magic_module = new MagicModule();
		magic_module.onEnable(this);
		
		NationsModule nations_module = new NationsModule();
		nations_module.onEnable(this);
		
		RiftPortal a = new RiftPortal(new Location(Bukkit.getWorld("world"), 0, 100, 0));
		RiftPortal b = new RiftPortal(new Location(Bukkit.getWorld("world"), 100, 100, 100));
		Rift rift = new TemporaryRift(a, b, 20);
		RiftManager.getInstance().register(rift);
	}
	
	public void onDisable() {
		
	}
	
}
