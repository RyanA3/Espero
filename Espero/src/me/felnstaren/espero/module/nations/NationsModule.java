package me.felnstaren.espero.module.nations;

import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.nations.command.nation.NationCommandMaster;
import me.felnstaren.espero.module.nations.system.Nations;

public class NationsModule implements IModule {

	public void onEnable(JavaPlugin plugin) {
		Nations.getInstance();
		
		plugin.getCommand("nation").setExecutor(new NationCommandMaster());
	}

	public void onDisable(JavaPlugin plugin) {
		Nations.getInstance().save();
	}

}
