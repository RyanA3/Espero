package me.felnstaren.espero.module.morph;

import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.morph.command.MorphCommand;
import me.felnstaren.espero.module.morph.listener.MorphPlayerListener;
import me.felnstaren.felib.FeLib;

public class MorphModule implements IModule {

	private MorphManager mman;
	
	@Override
	public void onEnable(JavaPlugin plugin) {
		mman = new MorphManager();
		FeLib.INJECTOR.getManager().registerInOut(mman);
		plugin.getCommand("morph").setExecutor(new MorphCommand(mman));
		plugin.getServer().getPluginManager().registerEvents(new MorphPlayerListener(mman), plugin);
	}

	@Override
	public void onDisable(JavaPlugin plugin) {
		// TODO Auto-generated method stub
		mman.shutdown();
	}

}
