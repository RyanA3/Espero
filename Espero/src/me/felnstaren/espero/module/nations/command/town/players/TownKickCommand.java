package me.felnstaren.espero.module.nations.command.town.players;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class TownKickCommand extends SubCommand {

	public TownKickCommand() {
		super("kick");
		arguments.add(new SubArgument("<komrade>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Messenger.send(sender, Color.RED + "Usage: /town kick <player> <town>");
				return true;
			}
		});
		arguments.get(0).addArgument(new SubArgument("<komradetown>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
				EsperoPlayer other = Espero.PLAYERS.getPlayer(args[1]);
				
				if(other == null) {
					Messenger.send(sender, Color.RED + args[1] + " does not exist or has never joined this server");
					return true;
				}
				
				String town_name = "";
				for(int i = 2; i < args.length; i++) {
					town_name += args[i];
					if(i < args.length - 1) town_name += " ";
				}
				
				Town town = TownRegistry.inst().getTown(town_name);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + town_name + " is not a valid town");
					return true;
				}
				
				if(!town.hasPermission(player, Permission.KICK)) {
					Messenger.send(sender, Color.RED + "You do not have permission to kick players in " + town_name);
					return true;
				}
				
				if(!town.isMember(other)) {
					Messenger.send(sender, Color.RED + args[1] + " is not a member of " + town_name);
					return true;
				}
				
				town.broadcast(Color.GREEN + sender.getName() + " has kicked " + args[1] + " out of " + town_name);
				if(other.isOnline()) Messenger.send(Bukkit.getPlayer(args[1]), Color.GREEN + sender.getName() + " has kicked you out of " + town_name);
				town.kick(other);
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town kick <player> <town>");
		return true;
	}
	
}
