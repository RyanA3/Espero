package me.felnstaren.espero.module.nations.command.nation.create;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationCreateSub extends SubCommand {

	public NationCreateSub() {
		super("create");
		
		arguments.add(new NationCreateArg());
	}

	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, "#F55Usage: #F77/nation create <name>"
				+ "\n#999Costs emeralds...");
		return true;
	}

}
