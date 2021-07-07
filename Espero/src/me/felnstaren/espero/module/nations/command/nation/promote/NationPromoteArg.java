package me.felnstaren.espero.module.nations.command.nation.promote;

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

public class NationPromoteArg extends SubArgument {

	public NationPromoteArg() {
		super("<player>");
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		if(!nation.hasPermission(eplayer, Permission.PROMOTE)) {
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
		
		if(eother.getUniqueId().equals(player.getUniqueId())) {
			Messenger.send(player, "#F55You can't promote yourself!");
			return true;
		}

		if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
			Messenger.send(player, Format.ERROR_PLAYER_IN_SEPERATE_NATION.message());
			return true;
		}
		
		if(nation.getGroup().relRank(eother) == nation.getGroup().toprank() - 1) {
			Messenger.send(player, "#F55This player already has the highest rank possible!");
			return true;
		}
		
		if(!nation.outranks(eplayer, eother)) {
			Messenger.send(player, "#F55You can't promote this player above or to your own rank!");
			return true;
		}
		
		nation.promote(eother);
		nation.broadcast(Color.GREEN + args[current] + Color.GREEN + " has been promoted by " + player.getDisplayName());
		return true;
	}

}
