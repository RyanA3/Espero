package me.felnstaren.espero.module.nations.town.siege;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.felib.util.data.BinarySearchable;
import me.felnstaren.felib.util.data.SearchObject;

public class SiegeRegistry extends BinarySearchable<Siege> {
	
	private static SiegeRegistry INSTANCE;
	
	public static SiegeRegistry inst() { return INSTANCE; }
	public static void init(JavaPlugin plugin) { INSTANCE = new SiegeRegistry(plugin); }
	
	
	
	public SiegeRegistry(JavaPlugin plugin) {
		Espero.LOGGER.debug("Loading sieges into memory...");
		
		String path = "/siegedata/";
		File folder = Espero.LOADER.load(path);
		File[] datas = folder.listFiles();
		
		for(File data : datas) {
			Espero.LOGGER.stream("Loading siege from file " + data.getPath() + "...");
			Siege siege = new Siege(UUID.fromString(data.getName().split("\\.")[0]));
			super.add(siege);
		}
		
		new BukkitRunnable() { //Every second
			ArrayList<Siege> remove = new ArrayList<Siege>();
			public void run() {
				if(values.isEmpty()) return;
				for(Siege s : values) {
					s.tick();
					if(s.isDead()) remove.add(s);
				}
				
				if(!remove.isEmpty()) {
					values.removeAll(remove);
					remove.clear();
				}
			}
		}.runTaskTimer(plugin, 100, 20);
	}

	public void startSiege(Town attacker, Town defender) {
		Siege siege = new Siege(attacker, defender);
		super.add(siege);
	}
	
	public Siege getSiege(UUID uuid) {
		return super.get(SearchObject.getIndexValue(uuid));
	}
	
	
	
	public void save() {
		Espero.LOGGER.debug("Saving all sieges...");
		for(Siege siege : super.values) {
			Espero.LOGGER.stream("Saving siege on " + siege.getDefender().getName() + "...");
			siege.save();
		}
	}

}
