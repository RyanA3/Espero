package me.felnstaren.espero.module.nations.newclaimsystem;

import java.util.UUID;

public class NationRange {

	public int min;
	public int max;
	public UUID nation;
	
	public NationRange(int min, int max, UUID nation) {
		this.min = min;
		this.max = max;
		this.nation = nation;
	}
	
}
