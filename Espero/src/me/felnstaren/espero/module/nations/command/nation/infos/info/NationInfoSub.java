package me.felnstaren.espero.module.nations.command.nation.infos.info;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.command.nation.infos.NationInfosSub;
import me.felnstaren.espero.module.nations.command.nation.infos.members.NationMembersSub;
import me.felnstaren.espero.module.nations.command.nation.infos.towns.NationTownsSub;
import me.felnstaren.espero.module.nations.nation.Nation;

public class NationInfoSub extends NationInfosSub {

	public NationInfoSub() {
		super("info");
		arguments.add(new NationInfoArg());
	}
	
	
	
	public String message(Nation nation) {
		return constructNationInfo(nation);
	}
	
	
	
	public static String constructNationInfo(Nation nation) {
		String message = nation.neatHeader() + "\n";
		message += "  " + Format.SUBHEADER.message("General") + "\n";
		message += "    " + Format.LABEL_ARG.message("Claims", String.valueOf(nation.getArea())) + ":";
		message += Format.LABEL_ARG.message("Perimeter", String.valueOf(nation.getPerimeter())) + "\n";
		message += "    " + Format.LABEL_ARG.message("Balance", String.valueOf(nation.getBalance())) + "\n";
		message += "  " + Format.SUBHEADER_VALUE.message("Members", nation.getMembers().size() + "") + "\n";
		message += "   " + NationMembersSub.constructMembersList(nation) + "\n";
		message += "  " + Format.SUBHEADER_VALUE.message("Towns", nation.getTowns().size() + "") + "\n";
		message += "   " + NationTownsSub.constructTownsList(nation) + "\n";
		
		
		return message;
	}

}
