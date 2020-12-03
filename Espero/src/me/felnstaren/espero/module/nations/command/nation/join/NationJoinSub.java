package me.felnstaren.espero.module.nations.command.nation.join;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubCommand;
import me.felnstaren.espero.util.message.Messenger;

public class NationJoinSub extends SubCommand {

	public NationJoinSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				sender.sendMessage(Messenger.color("&cUsage: /nation join <nation>"));
				return true;
			}
		}, "join");
		
		arguments.add(new NationJoinArg());
	}
	
}
