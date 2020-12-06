package me.felnstaren.espero.module.nations.command.nation.leader;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubCommand;
import me.felnstaren.espero.util.message.Messenger;

public class NationLeaderSub extends SubCommand {

	public NationLeaderSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				sender.sendMessage(Messenger.color("&cUsage: /nation leader <player>"));
				return true;
			}
		}, "leader");
		
		arguments.add(new NationLeaderArg());
	}

}
