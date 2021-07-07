package me.felnstaren.espero.module.nations.command.nation.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationListSub extends SubCommand {

	public NationListSub() {
		super("list");
	}

	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		String message =  Format.HEADER.message("Espero's Nations") + "\n" + Color.TURQUOISE;
		for(String name : NationRegistry.inst().getNationNames()) message += "   " + name;
		Messenger.send(player, Messenger.color(message));
		
		return true;
	}
	
}
