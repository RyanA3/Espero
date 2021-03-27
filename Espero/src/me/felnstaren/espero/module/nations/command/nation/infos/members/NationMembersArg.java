package me.felnstaren.espero.module.nations.command.nation.infos.members;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.command.nation.infos.NationInfosArg;
import me.felnstaren.espero.module.nations.nation.Nation;

public class NationMembersArg extends NationInfosArg {

	public NationMembersArg() {
		super("<nation>");
	}
	
	
	
	public String message(Nation nation) {
		return nation.neatHeader()
				+ Format.LABEL_ARG.message("Members", nation.getMembers().size() + "") + "\n"
				+ NationMembersSub.constructMembersList(nation) + "\n";
	}

}
