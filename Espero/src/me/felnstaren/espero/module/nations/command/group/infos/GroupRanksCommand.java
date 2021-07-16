package me.felnstaren.espero.module.nations.command.group.infos;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.group.Rank;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class GroupRanksCommand extends SubCommand {

	public GroupRanksCommand() {
		super("ranks");
		arguments.add(new SubArgument("<group>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Group group = GroupRegistry.inst().getGroup(args[current]);
				
				if(group == null) {
					Messenger.send(sender, Color.RED + "Invalid Group: " + args[current]);
					return true;
				}
				
				Messenger.send(sender, message(group));
				return true;
			}
		});
	}
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /group ranks <group>");
		return true;
	}
	
	
	
	public static String message(Group group) {
		return group.neatHeader() 
				+ Format.LABEL_ARG.message("Ranks", group.getRanks().length + "") + "\n"
				+ constructRanksList(group) + "\n";
	}
	
	public static String constructRanksList(Group group) {
		Rank[] ranks = group.getRanks();
		String message = Color.TURQUOISE.toString();
		
		for(Rank r : ranks) {
			message += Color.AQUA + r.display_name + ":" + r.weight + "\n ";
			message += Color.TURQUOISE;
			int i = 0; for(Permission p : r.permissions()) {
				if(i > 30) { message += "\n "; i = 0; }
				else if(i != 0) message += ", ";
				message += p.name();
			i += p.name().length(); }
			if(!message.endsWith("\n")) message += "\n";
		}
		
		return message;
	}
	
}
