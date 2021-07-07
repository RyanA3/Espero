package me.felnstaren.espero.module.nations.nation;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.util.data.BinarySearchable;
import me.felnstaren.felib.util.data.SearchObject;

public class NationRegistry extends BinarySearchable<Nation> {
	
	private static NationRegistry INSTANCE;
	
	public static void init() {
		INSTANCE = new NationRegistry();
	}
	
	public static NationRegistry inst() {
		if(INSTANCE == null) init();
		return INSTANCE;
	}
	

	
	public NationRegistry() {
		Espero.LOGGER.debug("Loading nations into memory...");
		
		String path = "/nationdata/";
		File folder = Espero.LOADER.load(path);
		File[] datas = folder.listFiles();
		
		for(File data : datas) {
			Espero.LOGGER.stream("Loading nation from file " + data.getPath() + "...");
			Nation nation = new Nation(UUID.fromString(data.getName().split("\\.")[0]));
			super.add(nation);
		}
	}
	
	
	
	public void 			 register   (Nation nation )   { super.add(nation); 								   }
	public void 			 unregister (UUID nation_id)   { super.remove(SearchObject.getIndexValue(nation_id)); }
	public void 		     unregister (Nation nation )   { super.remove(nation);                                }
	public ArrayList<Nation> getNations ()    			   { return super.values; }
	public Nation 			 getNation  (UUID id)          { return super.get(SearchObject.getIndexValue(id)); }
	public Nation 			 getNation  (String name)      {	//Linear Search, finds a nation by its name
		for(Nation n : super.values) 
			if(n.getDisplayName().equals(name)) 
				return n;
		return null;
	}	
	public ArrayList<String> getNationNames() 			   {	//Compiles a List of Nation Names from the main nation list
		ArrayList<String> nation_names = new ArrayList<String>();
		for(Nation n : super.values)
			nation_names.add(n.getDisplayName());
		return nation_names;
	}
	

	
	public void save() {
		Espero.LOGGER.debug("Saving all nations...");
		for(Nation nation : super.values) {
			Espero.LOGGER.stream("Saving nation " + nation.name + "...");
			nation.save();
		}
	}
	
}
