package me.felnstaren.espero.module.nations.command.nation.create.name;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubArgument;
import me.felnstaren.espero.util.message.Messenger;

public class NationCreateNameArgument extends SubArgument {

	public NationCreateNameArgument() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				Messenger.send(player, "#5F5Successfully create a new nation called #7F7" + args[current]);
				return true;
			}
		}, "<name>");
	}

}
