package me.felnstaren.espero.module.nations.command.nation.demote;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.PlayerMessage;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubArgument;

public class NationDemoteArg extends SubArgument {
	
	public NationDemoteArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, PlayerMessage.ERROR_NOT_IN_NATION.message());
					return true;
				}
				
				if(!eplayer.getNationRank().isPermitted("demote")) {
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
				
				if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
					Messenger.send(player, PlayerMessage.ERROR_PLAYER_IN_SEPERATE_NATION.message());
					return true;
				}
				
				if(eother.getNationRank().getWeight() >= eplayer.getNationRank().getWeight()) {
					Messenger.send(player, PlayerMessage.ERROR_CANT_DEMOTE.message());
					return true;
				}
				
				NationPlayerRank demotion = nation.getNextLowestRank(eother.getNationRank());
				if(demotion == null) {
					Messenger.send(player, PlayerMessage.ERROR_CANT_DEMOTE.message());
					return true;
				}
				
				eother.setNationRank(demotion);
				//eother.save();
				nation.broadcast("#F5F" + other.getDisplayName() + " #5F5has been demoted to the rank of #999" + demotion.getDisplayName() + " #5F5by " + player.getDisplayName());
				
				return true;
			}
		}, "<player>");
	}

}
