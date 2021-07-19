package me.felnstaren.espero.module.nations.command.nation.players;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationChatCommand extends SubCommand {

	public NationChatCommand() {
		super("chat");
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		
		if(eplayer.getNation() == null) {
			if(eplayer.getActiveGroupChat() != null) {
				eplayer.setActiveGroupChat(null);
				Messenger.send(sender, Color.GREEN + "Disabled Group Chat");
				return true;
			}
			
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		if(eplayer.getActiveGroupChat() == null || !eplayer.getActiveGroupChat().equals(eplayer.getNation().getGroup().getID())) {
			eplayer.setActiveGroupChat(eplayer.getNation().getGroup().getID());
			Messenger.send(player, Color.GREEN + "Selected Nation Chat");
		} else {
			eplayer.setActiveGroupChat(null);
			Messenger.send(player, Color.GREEN + "Disabled Group Chat");
		}
		return true;
	}

}
