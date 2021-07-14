package me.felnstaren.espero.module.nations.command.town.info;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class TownListCommand extends SubCommand {

	public TownListCommand() {
		super("list");
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		String message =  Format.HEADER.message("Espero's Towns") + "\n" + Color.TURQUOISE;
		ArrayList<String> names = TownRegistry.inst().getTownsDisplayNames();
		for(String name : names) message += "   " + name;
		Messenger.send(player, Messenger.color(message));
		
		return true;
	}

}
