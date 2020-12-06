package me.felnstaren.espero.module.nations.command.nation.demote;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubCommand;
import me.felnstaren.espero.util.message.Messenger;

public class NationDemoteSub extends SubCommand {

	public NationDemoteSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				sender.sendMessage(Messenger.color("&cUsage: /nation demote <player>"));
				return true;
			}
		}, "demote");
		
		arguments.add(new NationDemoteArg());
	}

}
