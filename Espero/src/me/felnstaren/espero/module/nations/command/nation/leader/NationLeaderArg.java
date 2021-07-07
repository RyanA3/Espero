package me.felnstaren.espero.module.nations.command.nation.leader;

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

public class NationLeaderArg extends SubArgument {

	public NationLeaderArg() {
		super("<player>");
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		if(!nation.hasPermission(eplayer, Permission.NATION_TRANSFER_LEADERSHIP)) {
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
			Messenger.send(player, "#F55You can't transfer the leadership role to yourself!");
			return true;
		}

		if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
			Messenger.send(player, Format.ERROR_PLAYER_IN_SEPERATE_NATION.message());
			return true;
		}
		
		int leader = nation.getGroup().relRank(eplayer);
		nation.getGroup().demote(eplayer);
		nation.getGroup().setRank(eother, leader);
		nation.broadcast(Color.GREEN + player.getName() + " has transferred their leadership to " + args[current]);
		
		return true;
	}

}
