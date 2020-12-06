package me.felnstaren.espero.module.nations.command.nation.promote;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubCommand;
import me.felnstaren.espero.util.message.Messenger;

public class NationPromoteSub extends SubCommand {

	public NationPromoteSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				sender.sendMessage(Messenger.color("&cUsage: /nation promote <player>"));
				return true;
			}
		}, "promote");
		
		arguments.add(new NationPromoteArg());
	}

}
