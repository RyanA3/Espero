package me.felnstaren.espero.module.nations.command.nation.infos;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public abstract class NationInfosSub extends SubCommand {

	public NationInfosSub(String label) {
		super(label);
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, "#F77Usage: /nation " + label + " <nation>");
			return true;
		}
		
		Messenger.send(player, message(nation));
		return true;
	}
	
	
	
	public abstract String message(Nation nation);

}
