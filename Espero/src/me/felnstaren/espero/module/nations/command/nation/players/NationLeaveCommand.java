package me.felnstaren.espero.module.nations.command.nation.players;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationLeaveCommand extends SubCommand {

	public NationLeaveCommand() {
		super("leave");
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		if(nation.isLeader(eplayer)) {
			Messenger.send(player, "#F55You must delegate a new leader before you leave!");
			return true;
		}

		if(nation.getMembers().size() == 0) {
			Messenger.broadcast("#F22" + nation.getDisplayName() + " #F55has been disbanded!");
			nation.disband();
		}
		
		nation.broadcast("#5F5" + player.getDisplayName() + " has left the nation!");
		Messenger.send(player, "#5F5You've left the nation of #F22" + nation.getDisplayName() + " #5F5behind, good luck on your travels");
		
		return true;
	}

	
	
}
