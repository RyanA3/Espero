package me.felnstaren.espero.module.nations.claim;

import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.LinearRankModel;

public class PlotGroup extends Group {
	
	//How many claim chunks reference this PlotGroup
	private int references = 0;

	public PlotGroup(LinearRankModel model, String name) {
		super(model, name);
	}

	
	
	public int getReferences() {
		return references;
	}
	
	public void setReferences(int count) {
		this.references = count;
	}
	
	public void incReferences(int amount) {
		this.references += amount;
	}
	
}
