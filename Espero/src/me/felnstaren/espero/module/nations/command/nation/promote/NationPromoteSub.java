package me.felnstaren.espero.module.nations.command.nation.promote;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationPromoteSub extends SubCommand {

	public NationPromoteSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Messenger.send((Player) sender, "#F55Usage: #F77/nation promote <player>");
				return true;
			}
		}, "promote");
		
		arguments.add(new NationPromoteArg());
	}

}
