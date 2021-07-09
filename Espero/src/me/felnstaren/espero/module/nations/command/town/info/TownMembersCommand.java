package me.felnstaren.espero.module.nations.command.town.info;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class TownMembersCommand extends SubCommand {
	
	public static String constructMembersList(Town town) {
		String members = "" + Color.TURQUOISE;
		for(EsperoPlayer player : town.getGroup().getMembers())
			members += " " + Espero.OFFLINE_PLAYERS.getName(player.getUniqueId()) + ":" + town.getGroup().relRank(player);
		
		return members;
	}
	
	public static String message(Town town) {
		return town.neatHeader() 
				+ Format.LABEL_ARG.message("Members", town.getGroup().getMembers().size() + "") + "\n"
				+ constructMembersList(town) + "\n";
	}
	
	

	public TownMembersCommand() {
		super("members");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				String name = "";
				for(int i = current; i < args.length; i++) {
					name += args[i];
					if(i < args.length - 1) name += " ";
				}
				
				Town town = TownRegistry.inst().getTown(name);
				if(town == null) Messenger.send((Player) sender, Color.RED + "Invalid town: " + name);
				else Messenger.send((Player) sender, message(town));
				return true;
			}
		});
	}


	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, Color.RED + "Usage: /town info <town>");
		return true;
	}

}
