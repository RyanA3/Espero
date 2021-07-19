package me.felnstaren.espero.module.nations.command.group.players;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class GroupRemoveCommand extends SubCommand {

	public GroupRemoveCommand() {
		super("remove");
		arguments.add(new SubArgument("<player>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Messenger.send(sender, Color.RED + "Usage: /group remove <player> <group>");
				return true;
			}
		});
		arguments.get(0).addArgument(new SubArgument("<group>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				UUID playerid = Espero.OFFLINE_PLAYERS.getID(args[1]);
				
				if(playerid == null) {
					Messenger.send(sender, Color.RED + "Invalid Player: " + args[1]);
					return true;
				}
				
				EsperoPlayer player = Espero.PLAYERS.getPlayer(playerid);
				Group group = GroupRegistry.inst().getGroup(args[current]);
				
				if(group == null) {
					Messenger.send(sender, Color.RED + "Invalid Group: " + args[current]);
					return true;
				}
				
				if(!group.contains(player)) {
					Messenger.send(sender, Color.RED + "Player not in group");
					return true;
				}
				
				group.kick(player);
				Messenger.send(sender, Color.GREEN + "Removed " + args[1] + " from " + args[current]);
				if(args.length <= current) player.message(Color.RED + "Force removed from " + args[current], true);  
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /group remove <player> <group>");
		return true;
	}
	
}
