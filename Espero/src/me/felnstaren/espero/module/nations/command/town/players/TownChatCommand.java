package me.felnstaren.espero.module.nations.command.town.players;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class TownChatCommand extends SubCommand {

	public TownChatCommand() {
		super("chat");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
				Town town = TownRegistry.inst().getTown(args[current]);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + "Invalid Town: " + args[current]);
					return true;
				}
				
				if(!town.isMember(player)) {
					Messenger.send(sender, Color.RED + "You must be in this town to do this");
					return true;
				}
				
				
				
				if(player.getActiveGroupChat() == null || !player.getActiveGroupChat().equals(town.getGroup().getID())) {
					player.setActiveGroupChat(town.getGroup().getID());
					Messenger.send(sender, Color.GREEN + "Selected Town Chat");
				} else {
					player.setActiveGroupChat(null);
					Messenger.send(sender, Color.GREEN + "Disabled Group Chat");
				}
				
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
		
		if(player.getActiveGroupChat() != null) {
			player.setActiveGroupChat(null);
			Messenger.send(sender, Color.GREEN + "Disabled Group Chat");
			return true;
		}
		
		Messenger.send(sender, Color.RED + "Usage: /town chat <town>");
		return true;
	}
	
}
