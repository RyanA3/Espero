package me.felnstaren.espero.module.nations.command.nation.create;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationCreateSub extends SubCommand {

	public NationCreateSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Messenger.send((Player) sender, "#F55Usage: #F77/nation create <name>"
						+ "\n#999Costs emeralds...");
				return true;
			}
		}, "create");
		
		arguments.add(new NationCreateArg());
	}

}
