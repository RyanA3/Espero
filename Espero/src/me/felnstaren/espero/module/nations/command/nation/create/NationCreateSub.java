package me.felnstaren.espero.module.nations.command.nation.create;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubCommand;
import me.felnstaren.espero.module.nations.command.nation.create.name.NationCreateNameArgument;
import me.felnstaren.espero.util.message.Messenger;

public class NationCreateSub extends SubCommand {

	public NationCreateSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Messenger.send((Player) sender, "#F55Usage: #F77/nation create <name>"
						+ "\n#999Creates a nation with a town where you're standing"
						+ "\ntowns cannot be moved, choose wisely");
				return true;
			}
		}, "create");
		
		arguments.add(new NationCreateNameArgument());
	}

}
