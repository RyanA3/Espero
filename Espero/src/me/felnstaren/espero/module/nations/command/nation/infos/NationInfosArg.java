package me.felnstaren.espero.module.nations.command.nation.infos;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;

public abstract class NationInfosArg extends SubArgument {

	public NationInfosArg(String label) {
		super(label);
	}

	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		String nname = "";
		for(int i = 1; i < args.length; i++) {
			nname += args[i];
			if(i < args.length - 1) nname += " ";
		}
		
		Nation nation = NationRegistry.inst().getNation(nname);
		if(nation == null) {
			Messenger.send((Player) sender, Format.ERROR_INVALID_ARGUMENT.message().replaceAll("%argument%", nname));
			return true;
		}
		
		Messenger.send((Player) sender, message(nation));
		return true;
	}
	
	
	
	public abstract String message(Nation nation);
	
}
