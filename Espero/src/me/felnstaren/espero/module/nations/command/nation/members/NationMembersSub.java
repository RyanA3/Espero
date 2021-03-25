package me.felnstaren.espero.module.nations.command.nation.members;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationMembersSub extends SubCommand {

	public NationMembersSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F77Usage: /nation members <nation>");
					return true;
				}
				
				Messenger.send(player, "#AAA &a-=&8[ &7" + nation.getDisplayName() + " &8]&a=- \n" + constructMembersList(nation));
				return true;
			}
		}, "members");
		
		arguments.add(new NationMembersArg());
	}


	
	public static String constructMembersList(Nation nation) {
		String members = "";
		for(UUID id : nation.getMembers())
			members += " " + Espero.OFFLINE_PLAYERS.getName(id);
		
		return Messenger.color(members);
	}
	
}
