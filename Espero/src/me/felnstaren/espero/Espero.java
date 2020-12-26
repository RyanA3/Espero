package me.felnstaren.espero;

import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.command.standalone.TestCommand;
import me.felnstaren.espero.config.Loader;
import me.felnstaren.espero.module.magic.MagicModule;
import me.felnstaren.espero.module.nations.NationsModule;
import me.felnstaren.rilib.logger.Logger;

public class Espero extends JavaPlugin {

	private MagicModule magic_module;
	private NationsModule nations_module;
	public static Logger LOGGER;
	
	public void onEnable() {
		LOGGER = new Logger(this.getServer().getConsoleSender(), this.getName());
		Loader.mkDirs();
		
		//magic_module = new MagicModule();
		//magic_module.onEnable(this);
		
		nations_module = new NationsModule();
		nations_module.onEnable(this);
		
		this.getCommand("test").setExecutor(new TestCommand());
	}
	
	public void onDisable() {
		//magic_module.onDisable(this);
		nations_module.onDisable(this);
	}
	
}