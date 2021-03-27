package me.felnstaren.espero.module.nations.command.nation.infos.help;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;

public class NationHelpArg extends SubArgument {

	public NationHelpArg() {
		super("<page>");
	}

	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		
		int page = 0;
		try { page = Math.abs(Integer.parseInt(args[current])); }
		catch (Exception e) { Messenger.send(player, Format.ERROR_INVALID_ARGUMENT.message().replaceAll("%argument%", args[current])); return true; }
		
		if(page >= NationHelpSub.PAGES.length) {
			Messenger.send(player, Format.ERROR_INVALID_ARGUMENT.message().replaceAll("%argument%", args[current]));
			return true;
		}
		
		Messenger.send(player, NationHelpSub.PAGES[page]);
		
		return true;
	}

}
