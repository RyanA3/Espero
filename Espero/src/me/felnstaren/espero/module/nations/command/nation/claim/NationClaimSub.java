package me.felnstaren.espero.module.nations.command.nation.claim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationClaimSub extends SubCommand {

	public NationClaimSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Messenger.send((Player) sender, "#F55Usage: /nation claim <type>");
				return true;
			}
		}, "claim");
		
		arguments.add(new NationClaimArg());
	}

}
