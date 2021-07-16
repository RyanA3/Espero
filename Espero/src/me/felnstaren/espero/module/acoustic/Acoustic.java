package me.felnstaren.espero.module.acoustic;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.acoustic.item.Banjo;
import me.felnstaren.espero.module.acoustic.item.BassDrum;
import me.felnstaren.espero.module.acoustic.item.Bellkit;
import me.felnstaren.espero.module.acoustic.item.Flute;
import me.felnstaren.espero.module.acoustic.item.Guitar;
import me.felnstaren.espero.module.acoustic.item.SnareDrum;
import me.felnstaren.espero.module.acoustic.item.TuningFork;
import me.felnstaren.espero.module.acoustic.listener.InstrumentListener;
import me.felnstaren.felib.item.custom.CustomMaterial;

public class Acoustic implements IModule {

	public void onEnable(JavaPlugin plugin) {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new InstrumentListener(), plugin);
		CustomMaterial.inst().register(new Banjo());
		CustomMaterial.inst().register(new BassDrum());
		CustomMaterial.inst().register(new Bellkit());
		CustomMaterial.inst().register(new Flute());
		CustomMaterial.inst().register(new Guitar());
		CustomMaterial.inst().register(new SnareDrum());
		CustomMaterial.inst().register(new TuningFork());
	}

	public void onDisable(JavaPlugin plugin) {

	}

}
