package me.felnstaren.espero.module.nations.command.nation.players;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class NationKickCommand extends SubCommand {

	public NationKickCommand() {
		super("kick");
		
		arguments.add(new SubArgument("<komrade>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
					return true;
				}
				
				if(nation.hasPermission(eplayer, Permission.KICK)) {
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
					Messenger.send(player, Format.ERROR_CANT_KICK.message());
					return true;
				}
				
				nation.kick(eother);
				nation.broadcast("#5F5" + player.getDisplayName() + " #5F5has kicked " + args[current] + " #5F5from the nation!");

				return true;
			}
		});
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, "#F55Usage: #F77/nation kick <player>");
		return true;
	}
	
}
