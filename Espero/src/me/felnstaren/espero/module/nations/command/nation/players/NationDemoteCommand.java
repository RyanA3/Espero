package me.felnstaren.espero.module.nations.command.nation.players;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class NationDemoteCommand extends SubCommand {

	public NationDemoteCommand() {
		super("demote");
		
		arguments.add(new SubArgument("<player>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
					return true;
				}
				
				if(!nation.hasPermission(eplayer, Permission.DEMOTE)) {
					Messenger.send(player, Format.ERROR_NATION_PERMISSION.message());
					return true;
				}
				
				Player other = Bukkit.getPlayerExact(args[current]);
				EsperoPlayer eother;
				if(other == null)
					eother = Espero.PLAYERS.getPlayer(Espero.OFFLINE_PLAYERS.getID(args[current]));
				else 
					eother = Espero.PLAYERS.getPlayer(other);
				if(eother == null) {
					Messenger.send(player, Format.ERROR_PLAYER_NOT_ONLINE.message());
					return true;
				}
				
				if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
					Messenger.send(player, Format.ERROR_PLAYER_IN_SEPERATE_NATION.message());
					return true;
				}
				
				if(!nation.outranks(eplayer, eother)) {
					Messenger.send(player, Format.ERROR_CANT_DEMOTE.message());
					return true;
				}
				
				nation.demote(eother);
				nation.broadcast(Color.RED + args[current] + Color.RED + " has been demoted by " + player.getDisplayName());
				return true;
			}
		});
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, "#F55Usage: #F77/nation demote <player>");
		return true;
	}

}
