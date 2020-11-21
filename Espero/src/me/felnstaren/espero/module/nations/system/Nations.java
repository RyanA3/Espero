package me.felnstaren.espero.module.nations.system;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.config.Loader;
import me.felnstaren.espero.module.nations.nation.Nation;

public class Nations {
	
	private static Nations INSTANCE;
	
	public static Nations getInstance() {
		if(INSTANCE == null) INSTANCE = new Nations();
		return INSTANCE;
	}
	

	private ArrayList<Nation> nations;
	private ArrayList<String> nations_names;
	private ArrayList<UUID>   nations_ids;
	
	public Nations() {
		String path = "/nationdata/";
		File folder = Loader.load(path);
		File[] datas = folder.listFiles();
		
		for(File data : datas) {
			Nation nation = new Nation(UUID.fromString(data.getName().replace(".txt", "")));
			nations.add(nation);
			nations_names.add(nation.getDisplayName());
			nations_ids.add(nation.id());
		}
	}
	
	
	public Nation getNation(UUID id) {
		return nations.get(nations_ids.indexOf(id));
	}
	
	public Nation getNation(String name) {
		return nations.get(nations_names.indexOf(name));
	}
	
	public void registerNewNation(Nation nation) {
		nations.add(nation);
		nations_names.add(nation.getDisplayName());
		nations_ids.add(nation.id());
	}
	
}
