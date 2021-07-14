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

public class TownLeaveCommand extends SubCommand {

	public TownLeaveCommand() {
		super("leave");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Town town = TownRegistry.inst().getTown(args[current]);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + args[current] + " is not a valid town");
					return true;
				}
				
				EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
				
				if(!town.isMember(player)) {
					Messenger.send(sender, Color.RED + "You are not a member of " + town.getDisplayName());
					return true;
				}
				
				town.kick(player);
				town.broadcast(Color.GREEN + sender.getName() + " has left " + town.getDisplayName());
				return true;
			}
		});
	}
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town leave <town>");
		return true;
	}
	
}
