package me.felnstaren.espero.module.horsia;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.horsia.listener.HorseDamageListener;

public class Horsia implements IModule {

	public void onEnable(JavaPlugin plugin) {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new HorseDamageListener(), plugin);
	}

	public void onDisable(JavaPlugin plugin) {
		
	}

}
