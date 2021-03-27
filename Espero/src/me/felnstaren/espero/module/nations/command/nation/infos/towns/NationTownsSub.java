package me.felnstaren.espero.module.nations.command.nation.infos.towns;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.command.nation.infos.NationInfosSub;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.felib.chat.Color;

public class NationTownsSub extends NationInfosSub {

	public NationTownsSub() {
		super("towns");
		arguments.add(new NationTownsArg());
	}


	
	public String message(Nation nation) {
		return nation.neatHeader()
				+ Format.LABEL_ARG.message("Towns", nation.getTowns().size() + "") + "\n"
				+ constructTownsList(nation) + "\n";
	}
	
	
	
	public static String constructTownsList(Nation nation) {
		String towns = "" + Color.TURQUOISE;
		for(Town t : nation.getTowns())
			towns += " " + t.name;
		
		return towns;
	}

}
