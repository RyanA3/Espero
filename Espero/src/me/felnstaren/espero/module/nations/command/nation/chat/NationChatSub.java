package me.felnstaren.espero.module.nations.command.nation.chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.PlayerMessage;
import me.felnstaren.espero.module.nations.chat.NationPlayerChatManager;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationChatSub extends SubCommand {

	public NationChatSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				
				if(eplayer.getNation() == null) {
					Messenger.send(player, PlayerMessage.ERROR_NOT_IN_NATION.message());
					return true;
				}
				
				NationPlayerChatManager.inst().toggle(eplayer);
				Messenger.send(player, "#AFAToggled Nation Chat");
				return true;
			}
		}, "chat");
	}

}
