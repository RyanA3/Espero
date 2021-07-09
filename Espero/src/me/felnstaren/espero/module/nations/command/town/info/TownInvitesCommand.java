package me.felnstaren.espero.module.nations.command.town.info;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class TownInvitesCommand extends SubCommand {
	
	public static String constructInvitesList(Town town) {
		ArrayList<String> invites = Espero.OFFLINE_PLAYERS.getNames(town.getInvites());
		String combined = Color.TURQUOISE + "";
		for(String invite : invites) combined += " " + invite;
		return combined;
	}
	
	public static String message(Town town) {
		return town.neatHeader() 
				+ Format.LABEL_ARG.message("Invites", town.getInvites().size() + "") + "\n"
				+ constructInvitesList(town) + "\n";
	}
	
	

	public TownInvitesCommand() {
		super("invites");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				String town_name = "";
				for(int i = 1; i < args.length; i++) {
					town_name += args[i];
					if(i < args.length - 1) town_name += " ";
				}
				
				Town town = TownRegistry.inst().getTown(town_name);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + town_name + " is not a valid town");
					return true;
				}
				
				Messenger.send(sender, message(town));
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town invites <town>");
		return true;
	}

}
