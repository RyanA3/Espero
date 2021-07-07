package me.felnstaren.espero.module.nations.command.nation.infos.members;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.command.nation.infos.NationInfosSub;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Color;

public class NationMembersSub extends NationInfosSub {

	public NationMembersSub() {
		super("members");
		
		arguments.add(new NationMembersArg());
	}
	
	
	
	public String message(Nation nation) {
		return nation.neatHeader() 
				+ Format.LABEL_ARG.message("Members", nation.getMembers().size() + "") + "\n"
				+ constructMembersList(nation) + "\n";
	}
	
	
	
	public static String constructMembersList(Nation nation) {
		String members = "" + Color.TURQUOISE;
		for(EsperoPlayer player : nation.getMembers())
			members += " " + Espero.OFFLINE_PLAYERS.getName(player.getUniqueId());
		
		return members;
	}

}
