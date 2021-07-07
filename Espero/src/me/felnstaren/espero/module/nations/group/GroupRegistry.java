package me.felnstaren.espero.module.nations.group;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.felib.util.data.BinarySearchable;
import me.felnstaren.felib.util.data.SearchObject;

public class GroupRegistry extends BinarySearchable<Group> {

	private static GroupRegistry INSTANCE;
	
	public static void init() {
		INSTANCE = new GroupRegistry();
	}
	
	public static GroupRegistry inst() {
		return INSTANCE;
	}
	
	
	
	public GroupRegistry() {
		Espero.LOGGER.debug("Loading groups into memory...");
		
		String path = "/groupdata/";
		File folder = Espero.LOADER.load(path);
		File[] datas = folder.listFiles();
		
		for(File data : datas) {
			Espero.LOGGER.stream("Loading group from file " + data.getName() + "...");
			Group group = new Group(UUID.fromString(data.getName().split("\\.")[0]));
			super.add(group);
		}
	}
	
	
	
	public void 		    register   (Group group  )  { super.add(group); 								   }
	public void			    unregister (UUID group_id)  { super.remove(SearchObject.getIndexValue(group_id));  }
	public void			    unregister (Group group  )  { super.remove(group);                                 }
	public ArrayList<Group> getGroups  (             )  { return super.values; 								   }
	public Group            getGroup   (UUID id      )  { return super.get(SearchObject.getIndexValue(id));    }
	public ArrayList<Group> getGroups  (UUID... uuids)  { 
		ArrayList<Group> gs = new ArrayList<Group>();
		for(int i = 0; i < uuids.length; i++)
			gs.add(getGroup(uuids[i]));
		return gs;
	}
	public ArrayList<Group> getGroups  (ArrayList<UUID> uuids) {
		ArrayList<Group> gs = new ArrayList<Group>();
		for(UUID uuid : uuids)
			gs.add(getGroup(uuid));
		return gs;
	}
	

	
	public void save() {
		Espero.LOGGER.debug("Saving all groups...");
		for(Group group : super.values) {
			Espero.LOGGER.stream("Saving group " + group.getID() + "...");
			group.save();
		}
	}
	
}
