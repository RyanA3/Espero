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

public class TownInviteCommand extends SubCommand {

	public TownInviteCommand() {
		super("invite");
		arguments.add(new SubArgument("<player>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Messenger.send(sender, Color.RED + "Usage: /town invite <player> <town>");
				return true;
			}
		});
		arguments.get(0).addArgument(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				EsperoPlayer inviter = Espero.PLAYERS.getPlayer((Player) sender);
				EsperoPlayer invitee = Espero.PLAYERS.getPlayer(args[1]);
				
				if(invitee == null) {
					Messenger.send(sender, Color.RED + args[1] + " does not exist or has never joined this server");
					return true;
				}
				
				Town town = TownRegistry.inst().getTown(args[current]);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + args[current] + " is not a valid town");
					return true;
				}
				
				if(!town.hasPermission(inviter, Permission.INVITE)) {
					Messenger.send(sender, Color.RED + "You do not have permission to invite players to " + town.getDisplayName());
					return true;
				}
				
				if(town.isInvited(invitee)) {
					Messenger.send(sender, Color.RED + args[1] + " has already been invited to " + town.getDisplayName());
					return true;
				}
				
				if(town.isMember(invitee)) {
					Messenger.send(sender, Color.RED + args[1] + " is already a member of " + town.getDisplayName());
					return true;
				}
				
				town.broadcast(Color.GREEN + sender.getName() + " has invited " + args[1] + " to " + town.getDisplayName());
				if(invitee.isOnline()) Messenger.send(Bukkit.getPlayer(args[1]), Color.GREEN + sender.getName() + " has invited you to the town of " + town.getDisplayName());
				town.invite(invitee);
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town invite <player> <town>");
		return true;
	}

}
