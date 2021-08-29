package me.felnstaren.espero.module.itemmodifiers;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.itemmodifiers.listener.DeathListener;
import me.felnstaren.espero.module.itemmodifiers.listener.DropListener;
import me.felnstaren.espero.module.itemmodifiers.listener.InventorySwapListener;
import me.felnstaren.espero.module.itemmodifiers.listener.RespawnListener;

public class ItemModifiers implements IModule {

	public void onEnable(JavaPlugin plugin) {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new DeathListener(), plugin);
		pm.registerEvents(new DropListener(), plugin);
		pm.registerEvents(new RespawnListener(), plugin);
		pm.registerEvents(new InventorySwapListener(), plugin);
	}

	public void onDisable(JavaPlugin plugin) {

	}

}
