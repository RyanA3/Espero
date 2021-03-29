package me.felnstaren.espero.module.clogger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;

public class CombatLoggerModule implements IModule {

	public void onEnable(JavaPlugin plugin) {
		CombatTimeHandler chandler = new CombatTimeHandler(plugin);
		
		plugin.getCommand("combat").setExecutor(new CombatCommand(chandler));
		
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new CombatListener(chandler), plugin);
		pm.registerEvents(new CombatLimiter(chandler), plugin);
	}

	public void onDisable(JavaPlugin plugin) {
		
	}

}
