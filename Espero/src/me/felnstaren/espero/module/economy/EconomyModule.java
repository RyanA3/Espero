package me.felnstaren.espero.module.economy;

import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;

public class EconomyModule implements IModule {

	public void onEnable(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(new CraftListener(), plugin);
	}

	public void onDisable(JavaPlugin plugin) {

	}

}
