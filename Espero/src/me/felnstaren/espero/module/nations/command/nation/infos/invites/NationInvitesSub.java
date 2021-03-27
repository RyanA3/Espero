package me.felnstaren.espero.module.nations.command.nation.infos.invites;

import java.util.UUID;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.command.nation.infos.NationInfosSub;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Color;

public class NationInvitesSub extends NationInfosSub {

	public NationInvitesSub() {
		super("invites");
		
		arguments.add(new NationInvitesArg());
	}

	
	
	public String message(Nation nation) {
		return nation.neatHeader() 
				+ Format.LABEL_ARG.message("Invites", nation.getInvites().size() + "") + "\n"
				+ constructInvitesList(nation) + "\n";
	}
	
	
	
	public static String constructInvitesList(Nation nation) {
		String invites = Color.TURQUOISE + "";
		for(UUID id : nation.getInvites())
			invites += " " + Espero.OFFLINE_PLAYERS.getName(id);
		
		return invites;
	}





	
}
