package me.felnstaren.espero.module.nations.command.nation.demote;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationDemoteSub extends SubCommand {

	public NationDemoteSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Messenger.send((Player) sender, "#F55Usage: #F77/nation demote <player>");
				return true;
			}
		}, "demote");
		
		arguments.add(new NationDemoteArg());
	}

}
