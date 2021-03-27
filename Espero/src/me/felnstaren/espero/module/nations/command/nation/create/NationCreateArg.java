package me.felnstaren.espero.module.nations.command.nation.create;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.PlayerMessage;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;

public class NationCreateArg extends SubArgument {

	public NationCreateArg() {
		super("<name>");
	}


	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
		
		if(eplayer.getNation() != null) {
			Messenger.send(player, PlayerMessage.ERROR_IN_NATION.message());
			return true;
		}
		
		String name = "";
		for(int i = 1; i < args.length && i < 4; i++) {
			if(i > 1) name += " ";
			name += args[i];
		}				
		
		if(Nations.inst().getNation(name) != null) {
			Messenger.send(player, PlayerMessage.ERROR_NAME_TAKEN.message());
			return true;
		}
		
		if(args[current].length() > 16) {
			Messenger.send(player, PlayerMessage.ERROR_TOO_LONG.message().replaceAll("%length%", "16"));
			return true;
		}
		
		
		Nation nation = new Nation(name, eplayer);
		Nations.inst().registerNewNation(nation);
		
		Messenger.broadcast("#5F5The nation of #2F2" + nation.getDisplayName() + " #5F5has risen from the ashes!");
		Messenger.send(player, "#5F5Successfully create a new nation called #7F7" + name);
		return true;
	}

}
