package me.felnstaren.espero.module.nations.command.nation.infos.invites;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.command.nation.infos.NationInfosArg;
import me.felnstaren.espero.module.nations.nation.Nation;

public class NationInvitesArg extends NationInfosArg {

	public NationInvitesArg() {
		super("<nation>");
	}

	

	public String message(Nation nation) {
		return nation.neatHeader() 
				+ Format.LABEL_ARG.message("Invites", nation.getInvites().size() + "") + "\n"
				+ NationInvitesSub.constructInvitesList(nation) + "\n";
	}
	
}
