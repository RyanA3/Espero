package me.felnstaren.espero.module.nations.nation;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;

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
		File folder = Espero.LOADER.load(path);
		File[] datas = folder.listFiles();
		
		nations = new ArrayList<Nation>();
		nations_names = new ArrayList<String>();
		nations_ids = new ArrayList<UUID>();
		
		for(File data : datas) {
			Nation nation = new Nation(UUID.fromString(data.getName().replace(".yml", "")));
			nations.add(nation);
			nations_names.add(nation.getDisplayName());
			nations_ids.add(nation.getID());
		}
	}
	
	
	public Nation getNation(UUID id) {
		try { return nations.get(nations_ids.indexOf(id)); }
		catch (Exception e) { return null; }
	}
	
	public Nation getNation(String name) {
		try { return nations.get(nations_names.indexOf(name)); }
		catch (Exception e) { return null; } 
	}
	
	public ArrayList<String> getNationNames() {
		return nations_names;
	}
	
	public void registerNewNation(Nation nation) {
		nations.add(nation);
		nations_names.add(nation.getDisplayName());
		nations_ids.add(nation.getID());
	}
	
	public void unregister(UUID nation_id) {
		int to_remove = -1;
		for(int i = 0; i < nations.size(); i++) if(nations.get(i).getID().equals(nation_id)) to_remove = i;
		if(to_remove < 0) return;
		nations.remove(to_remove);
		nations_names.remove(to_remove);
		nations_ids.remove(to_remove);
	}
	
	public void save() {
		for(Nation nation : nations)
			nation.save();
	}
	
}
