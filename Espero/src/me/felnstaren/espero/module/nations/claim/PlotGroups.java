package me.felnstaren.espero.module.nations.claim;

import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.felib.util.data.BinarySearchable;

public class PlotGroups extends BinarySearchable<Group> {

	public Group getGroup(int id) {
		return super.get(id);
	}
	
	public void delGroup(int id) {
		super.remove(id);
	}
	
	
	
	public static int getGroupId(Group group) {
		return group.searchValue();
	}
	
}
