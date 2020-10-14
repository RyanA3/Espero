package me.felnstaren.espero.module;

import org.bukkit.plugin.java.JavaPlugin;

public interface IModule {

	public void onEnable(JavaPlugin plugin);
	public void onDisable(JavaPlugin plugin);
	
}
