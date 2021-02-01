package me.felnstaren.espero.module.nations;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.nations.command.nation.NationCommandMaster;
import me.felnstaren.espero.module.nations.listener.ClaimInfoListener;
import me.felnstaren.espero.module.nations.system.Board;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.felib.logger.Level;

public class NationsModule implements IModule {

	BukkitRunnable save_task = new BukkitRunnable() {
		public void run() {
			Nations.getInstance().save();
			Board.getInstance().save();
			Espero.LOGGER.log(Level.INFO, "Saved nations and claims");
		}
	};
	
	public void onEnable(JavaPlugin plugin) {
		Nations.getInstance();
		Board.init();
		
		save_task.runTaskTimer(plugin, 100, 10000);
		
		plugin.getCommand("nation").setExecutor(new NationCommandMaster());
		
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new ClaimInfoListener(), plugin);
	}

	public void onDisable(JavaPlugin plugin) {
		save_task.cancel();
		Nations.getInstance().save();
		Board.getInstance().save();
	}

}
