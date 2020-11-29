package me.felnstaren.espero.module.magic;

import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.command.standalone.RiftCommand;
import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.magic.rift.RiftManager;

public class MagicModule implements IModule {

	public void onEnable(JavaPlugin plugin) {
		RiftManager.init(plugin);
		
		plugin.getCommand("rift").setExecutor(new RiftCommand());
	}


	public void onDisable(JavaPlugin plugin) {

	}

}
