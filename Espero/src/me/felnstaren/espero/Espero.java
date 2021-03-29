package me.felnstaren.espero;

import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.command.standalone.TestCommand;
import me.felnstaren.espero.config.EsperoPlayerManager;
import me.felnstaren.espero.module.clogger.CombatLoggerModule;
import me.felnstaren.espero.module.magic.MagicModule;
import me.felnstaren.espero.module.morph.MorphModule;
import me.felnstaren.espero.module.nations.NationsModule;
import me.felnstaren.espero.module.nations.infoout.InfoMessageController;
import me.felnstaren.felib.config.Loader;
import me.felnstaren.felib.logger.Logger;
import me.felnstaren.felib.player.PlayerNameIDTransposer;

public class Espero extends JavaPlugin {

	@SuppressWarnings("unused")
	private MagicModule magic_module;
	private NationsModule nations_module;
	private MorphModule morph_module;
	private CombatLoggerModule combat_logger_module;
	public static Logger LOGGER;
	public static Loader LOADER;
	public static EsperoPlayerManager PLAYERS;
	public static PlayerNameIDTransposer OFFLINE_PLAYERS;
	public static InfoMessageController IMC;
	
	public void onEnable() {
		LOGGER = new Logger(this.getServer().getConsoleSender(), this.getName());
		LOADER = new Loader(this, LOGGER);
		LOADER.mkDirs("/nationdata/", "/playerdata/", "/chunkdata/");
		PLAYERS = new EsperoPlayerManager(this);
		OFFLINE_PLAYERS = new PlayerNameIDTransposer(this, LOADER, "nameids.txt");
		IMC = new InfoMessageController(this);
		
		//magic_module = new MagicModule();
		//magic_module.onEnable(this);
		morph_module = new MorphModule();
		morph_module.onEnable(this);
		
		nations_module = new NationsModule();
		nations_module.onEnable(this);
		
		combat_logger_module = new CombatLoggerModule();
		combat_logger_module.onEnable(this);
		
		this.getCommand("test").setExecutor(new TestCommand());
	}
	
	public void onDisable() {
		//magic_module.onDisable(this);
		nations_module.onDisable(this);
		morph_module.onDisable(this);
		combat_logger_module.onDisable(this);
		PLAYERS.saveAll();
		OFFLINE_PLAYERS.save();
	}
	
}