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

public class TownUninviteCommand extends SubCommand {

	public TownUninviteCommand() {
		super("uninvite");
		arguments.add(new SubArgument("<nonkomrade>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Messenger.send(sender, Color.RED + "Usage: /town invite <player> <town>");
				return true;
			}
		});
		arguments.get(0).addArgument(new SubArgument("<komradetown>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				EsperoPlayer inviter = Espero.PLAYERS.getPlayer((Player) sender);
				EsperoPlayer invitee = Espero.PLAYERS.getPlayer(args[1]);
				
				if(invitee == null) {
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
				
				if(!town.hasPermission(inviter, Permission.INVITE)) {
					Messenger.send(sender, Color.RED + "You do not have permission to uninvite players in " + town_name);
					return true;
				}
				
				if(!town.isInvited(invitee)) {
					Messenger.send(sender, Color.RED + args[1] + " has not been invited to " + town_name);
					return true;
				}
				
				town.broadcast(Color.GREEN + sender.getName() + " has revoked " + args[1] + "'s invite to " + town_name);
				if(invitee.isOnline()) Messenger.send(Bukkit.getPlayer(args[1]), Color.GREEN + sender.getName() + " has revoked your invitation to the town of " + town_name);
				town.uninvite(invitee);
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town uninvite <player> <town>");
		return true;
	}
	
}
