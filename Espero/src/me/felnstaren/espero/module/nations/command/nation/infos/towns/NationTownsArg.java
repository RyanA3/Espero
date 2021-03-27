package me.felnstaren.espero.module.nations.command.nation.infos.towns;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.command.nation.infos.NationInfosArg;
import me.felnstaren.espero.module.nations.nation.Nation;

public class NationTownsArg extends NationInfosArg {

	public NationTownsArg() {
		super("<nation>");
	}

	
	
	public String message(Nation nation) {
		return nation.neatHeader()
				+ Format.LABEL_ARG.message("Towns", nation.getTowns().size() + "") + "\n"
				+ NationTownsSub.constructTownsList(nation) + "\n";
	}
	
}
