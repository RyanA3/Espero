package me.felnstaren.espero.module.nations.command.nation.infos.info;

import me.felnstaren.espero.module.nations.command.nation.infos.NationInfosArg;
import me.felnstaren.espero.module.nations.nation.Nation;

public class NationInfoArg extends NationInfosArg {

	public NationInfoArg() {
		super("<nation>");
	}

	
	
	public String message(Nation nation) {
		return NationInfoSub.constructNationInfo(nation);
	}
	
}
