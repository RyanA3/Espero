package me.felnstaren.espero.module.nations.command.nation.players;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.chat.NationPlayerChatManager;
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
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		NationPlayerChatManager.inst().toggle(eplayer);
		Messenger.send(player, "#AFAToggled Nation Chat");
		return true;
	}

}
