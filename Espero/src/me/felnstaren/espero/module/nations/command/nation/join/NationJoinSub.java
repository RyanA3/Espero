package me.felnstaren.espero.module.nations.command.nation.join;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationJoinSub extends SubCommand {

	public NationJoinSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Messenger.send((Player) sender, "#F55Usage: #F77/nation join <nation>");
				return true;
			}
		}, "join");
		
		arguments.add(new NationJoinArg());
	}
	
}
