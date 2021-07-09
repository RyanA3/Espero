package me.felnstaren.espero.module.nations.command.town.info;

import org.bukkit.command.CommandSender;
import org.fusesource.jansi.Ansi.Color;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class TownInfoCommand extends SubCommand {
	
	public static String constructTownInfo(Town town) {
		String message = town.neatHeader() + "\n";
		message += "  " + Format.SUBHEADER.message("General") + "\n";
		message += "  " + Format.SUBHEADER_VALUE.message("Nation", town.getNation().getDisplayName()) + "\n";
		message += "    " + Format.LABEL_ARG.message("Claims", String.valueOf(town.getArea())) + ":";
		//message += Format.LABEL_ARG.message("Perimeter", String.valueOf(town.getPerimeter())) + "\n";
		//message += "    " + Format.LABEL_ARG.message("Balance", String.valueOf(town.getBalance())) + "\n";
		message += "  " + Format.SUBHEADER_VALUE.message("Members", town.getGroup().getMembers().size() + "") + "\n";
		message += "   " + TownMembersCommand.constructMembersList(town) + "\n";
		return message;
	}
	
	

	public TownInfoCommand() {
		super("info");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {

				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town info <town>");
		return true;
	}

}
