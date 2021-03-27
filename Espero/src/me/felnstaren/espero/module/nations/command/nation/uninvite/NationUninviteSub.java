package me.felnstaren.espero.module.nations.command.nation.uninvite;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationUninviteSub extends SubCommand {

	public NationUninviteSub() {
		super("uninvite");
		arguments.add(new NationUninviteArg());
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, "#F55Usage: #F77/nation uninvite <player>");
		return true;
	}

}
