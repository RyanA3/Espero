package me.felnstaren.espero.module.nations.command.nation.invite;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubCommand;
import me.felnstaren.espero.util.message.Messenger;

public class NationInviteSub extends SubCommand {
 
	public NationInviteSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				sender.sendMessage(Messenger.color("&cUsage: /nation invite <player>"));
				return true;
			}
		}, "invite");
		
		arguments.add(new NationInviteArg());
	}
	
}
