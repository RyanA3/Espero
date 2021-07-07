package me.felnstaren.espero.module.nations.group;

import me.felnstaren.felib.config.ConfigurationSectionObject;

public abstract class IRankModel implements ConfigurationSectionObject {

	public abstract boolean hasPermission(Permission permission, int rank_index);
	
}
