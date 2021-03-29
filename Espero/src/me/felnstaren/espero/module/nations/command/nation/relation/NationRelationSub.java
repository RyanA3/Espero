package me.felnstaren.espero.module.nations.command.nation.relation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationRelationSub extends SubCommand {

	public NationRelationSub() {
		super("relation");
		arguments.add(new NationRelationArg());
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, Color.RED + "Usage: /nation relation <nation>");
		return true;
	}

}
