package me.felnstaren.espero.module.nations.command.nation.promote;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.PlayerMessage;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;

public class NationPromoteArg extends SubArgument {

	public NationPromoteArg() {
		super("<player>");
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, PlayerMessage.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		if(!eplayer.getNationRank().isPermitted("promote")) {
			Messenger.send(player, PlayerMessage.ERROR_NATION_PERMISSION.message());
			return true;
		}
		
		Player other = Bukkit.getPlayerExact(args[current]);
		EsperoPlayer eother;
		if(other == null)
			eother = Espero.PLAYERS.getPlayer(Espero.OFFLINE_PLAYERS.getID(args[current]));
		else 
			eother = Espero.PLAYERS.getPlayer(other);
		if(eother == null) {
			Messenger.send(player, PlayerMessage.ERROR_PLAYER_NOT_ONLINE.message());
			return true;
		}
		
		if(eother.getUniqueId().equals(player.getUniqueId())) {
			Messenger.send(player, "#F55You can't promote yourself!");
			return true;
		}

		if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
			Messenger.send(player, PlayerMessage.ERROR_PLAYER_IN_SEPERATE_NATION.message());
			return true;
		}
		
		NationPlayerRank promotion = nation.getNextHighestRank(eother.getNationRank());
		if(promotion == null) {
			Messenger.send(player, "#F55This player already has the highest rank possible!");
			return true;
		}
		
		if(promotion.getWeight() >= eplayer.getNationRank().getWeight()) {
			Messenger.send(player, "#F55You can't promote this player above or to your own rank!");
			return true;
		}
		
		eother.setNationRank(promotion.getLabel());
		//eother.save();
		nation.broadcast("#F5F" + args[current] + " #5F5has been promoted to the rank of #999" + promotion.getDisplayName() + " #5F5by " + player.getDisplayName());
		
		return true;
	}

}
