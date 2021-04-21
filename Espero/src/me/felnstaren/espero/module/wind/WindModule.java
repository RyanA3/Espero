package me.felnstaren.espero.module.wind;

import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;

public class WindModule implements IModule {

	public void onEnable(JavaPlugin plugin) {
		new WindTaskHandler(plugin);
	}

	public void onDisable(JavaPlugin plugin) {

	}

}
