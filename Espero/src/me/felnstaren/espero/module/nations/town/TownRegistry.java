package me.felnstaren.espero.module.nations.town;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.util.data.BinarySearchable;
import me.felnstaren.felib.util.data.SearchObject;

public class TownRegistry extends BinarySearchable<Town> {
	
	private static TownRegistry INSTANCE;
	
	public static void init() {
		INSTANCE = new TownRegistry();
	}

	public static TownRegistry inst() {
		return INSTANCE;
	}
	
	
	
	public TownRegistry() {
		Espero.LOGGER.debug("Loading towns into memory...");
		
		String path = "/towndata/";
		File folder = Espero.LOADER.load(path);
		File[] datas = folder.listFiles();
		
		for(File data : datas) {
			Espero.LOGGER.stream("Loading town from file " + data.getPath() + "...");
			Town town = new Town(UUID.fromString(data.getName().split("\\.")[0]));
			super.add(town);
		}
	}
	
	
	
	public void 		     register   (Town town )    { super.add(town); 								   }
	public void 		     unregister (UUID town_id)  { super.remove(SearchObject.getIndexValue(town_id)); }
	public void			     unregister (Town town )    { super.remove(town);                                }
	public ArrayList<Town>   getTowns   ()              { return super.values;							   }
	public Town 		     getTown    (UUID id)       { return super.get(SearchObject.getIndexValue(id));  }
	public ArrayList<Town>   getTowns   (UUID... uuids) {			//Gets a list of towns from a list of town ids
		ArrayList<Town> ts = new ArrayList<Town>();
		for(int i = 0; i < uuids.length; i++)
			ts.add(getTown(uuids[i]));
		return ts;
	}
	public ArrayList<Town>   getTowns   (ArrayList<UUID> uuids) {	//Gets a list of towns from a list of town ids
		ArrayList<Town> ts = new ArrayList<Town>();
		for(UUID uuid : uuids)
			ts.add(getTown(uuid));
		return ts;
	}
	public Town 		     getTown    (String name)      	{		//Linear Search, finds a town by its name
		for(Town t : super.values) 
			if(t.getName().equals(name)) 
				return t;
		return null;
	}	
	public Town		getTownByDisplay    (String name)		{
		return getTown(name.toLowerCase().replace(" ", "_"));
	}
	public ArrayList<String> getTownsNames() 		    {			//Compiles a List of Town Names from the main town list
		ArrayList<String> town_names = new ArrayList<String>();
		for(Town t : super.values)
			town_names.add(t.getName());
		return town_names;
	}
	public ArrayList<String> getTownsDisplayNames() {
		ArrayList<String> town_names = new ArrayList<String>();
		for(Town t : super.values)
			town_names.add(t.getDisplayName());
		return town_names;
	}
	

	
	public void save() {
		Espero.LOGGER.debug("Saving all towns...");
		for(Town town : super.values) {
			Espero.LOGGER.stream("Saving town " + town.getName() + "...");
			town.save();
		}
	}

}
