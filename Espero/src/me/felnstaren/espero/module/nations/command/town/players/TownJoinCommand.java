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

public class TownJoinCommand extends SubCommand {

	public TownJoinCommand() {
		super("join");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				String town_name = "";
				for(int i = 1; i < args.length; i++) {
					town_name += args[i];
					if(i < args.length - 1) town_name += " ";
				}
				
				Town town = TownRegistry.inst().getTown(town_name);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + town_name + " is not a valid town");
					return true;
				}
				
				EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
				
				if(!town.isInvited(player)) {
					Messenger.send(sender, Color.RED + "You have not been invited to join " + town_name);
					return true;
				}
				
				town.join(player);
				town.broadcast(Color.GREEN + sender.getName() + " has joined " + town_name);
				return true;
			}
		});
	}
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town join <town>");
		return true;
	}

}
