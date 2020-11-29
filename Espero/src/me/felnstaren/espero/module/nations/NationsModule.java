package me.felnstaren.espero.module.nations;

import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.nations.command.nation.NationCommandMaster;

public class NationsModule implements IModule {

	public void onEnable(JavaPlugin plugin) {
		plugin.getCommand("nation").setExecutor(new NationCommandMaster());
	}

	public void onDisable(JavaPlugin plugin) {

	}

}
