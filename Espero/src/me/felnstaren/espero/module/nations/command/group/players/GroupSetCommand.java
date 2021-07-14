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

public class GroupSetCommand extends SubCommand {

	public GroupSetCommand() {
		super("set");
		arguments.add(new SubArgument("<player>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Messenger.send(sender, Color.RED + "Usage: /group set <player> <group> <rank_index>");
				return true;
			}
		});
		arguments.get(0).addArgument(new SubArgument("<group>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Messenger.send(sender, Color.RED + "Usage: /group set <player> <group> <rank_index>");
				return true;
			}	
		});
		arguments.get(0).getArgument(0).addArgument(new SubArgument("<rank_index>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				UUID playerid = Espero.OFFLINE_PLAYERS.getID(args[1]);
				
				if(playerid == null) {
					Messenger.send(sender, Color.RED + "Invalid Player: " + args[1]);
					return true;
				}
				
				EsperoPlayer player = Espero.PLAYERS.getPlayer(playerid);
				Group group = GroupRegistry.inst().getGroup(args[2]);
				
				if(group == null) {
					Messenger.send(sender, Color.RED + "Invalid Group: " + args[2]);
					return true;
				}
				
				int rank = -1;
				try { rank = Integer.parseInt(args[current]); }
				catch (Exception e) { Messenger.send(sender, Color.RED + "Invalid Integer: " + args[current]); return true; }
				
				if(rank < 0 || rank >= group.getRanks().length) {
					Messenger.send(sender, Color.RED + "Invalid Rank Index: " + rank);
					return true;
				}
				
				group.setRank(player, rank);
				Messenger.send(sender, Color.GREEN + "Set " + args[1] + " to " + args[current] + " in " + args[2]);
				if(args.length <= current) player.message(Color.RED + "Force set rank in " + args[2] + " to " + args[current]);  
				return true;
			}
		});
		
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /group remove <player> <group> <rank_index>");
		return true;
	}
	
}
