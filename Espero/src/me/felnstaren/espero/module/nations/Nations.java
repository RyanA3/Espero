package me.felnstaren.espero.module.nations;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.command.group.GroupMasterCommand;
import me.felnstaren.espero.module.nations.command.nation.NationCommandMaster;
import me.felnstaren.espero.module.nations.command.town.TownCommandMaster;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.espero.module.nations.listener.ClaimInfoListener;
import me.felnstaren.espero.module.nations.listener.CofferListener;
import me.felnstaren.espero.module.nations.listener.GroupChatListener;
import me.felnstaren.espero.module.nations.listener.NationRelationCombatListener;
import me.felnstaren.espero.module.nations.listener.PlayerClaimInteractHandler;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.logger.Level;

public class Nations implements IModule {
	
	BukkitRunnable save_task = new BukkitRunnable() {
		public void run() {
			NationRegistry.inst().save();
			ClaimBoard.inst().save();
			Espero.LOGGER.log(Level.INFO, "Saved nations and claims");
		}
	};
	
	public void onEnable(JavaPlugin plugin) {
		GroupRegistry.init();
		NationRegistry.init();
		TownRegistry.init();
		ClaimBoard.init();

		save_task.runTaskTimer(plugin, 100, 10000);
		
		plugin.getCommand("nation").setExecutor(new NationCommandMaster());
		plugin.getCommand("town").setExecutor(new TownCommandMaster());
		plugin.getCommand("group").setExecutor(new GroupMasterCommand());
		
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new ClaimInfoListener(), plugin);
		pm.registerEvents(new PlayerClaimInteractHandler(), plugin);
		pm.registerEvents(new CofferListener(), plugin);
		pm.registerEvents(new NationRelationCombatListener(), plugin);
		pm.registerEvents(new GroupChatListener(), plugin);
	}

	public void onDisable(JavaPlugin plugin) {
		Bukkit.getScheduler().cancelTask(save_task.getTaskId());
		NationRegistry.inst().save();
		ClaimBoard.inst().save();
		TownRegistry.inst().save();
		GroupRegistry.inst().save();
		Espero.LOGGER.info("NATIONS.SAVE_ALL.DONE");
	}
	
	

	/** |----------------------------------------------------------------|
	 *  |     Centralized Nation/Town/Player/Group Utility Commands      |
	 *  |----------------------------------------------------------------| */
}
