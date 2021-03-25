package me.felnstaren.espero.module.nations.command.nation.leader;

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

public class NationLeaderArg extends SubArgument {

	public NationLeaderArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, PlayerMessage.ERROR_NOT_IN_NATION.message());
					return true;
				}
				
				NationPlayerRank rank = eplayer.getNationRank();
				if(!rank.getLabel().equals("leader")) {
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
					Messenger.send(player, "#F55You can't transfer the leadership role to yourself!");
					return true;
				}

				if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
					Messenger.send(player, PlayerMessage.ERROR_PLAYER_IN_SEPERATE_NATION.message());
					return true;
				}
				
				eother.setNationRank("leader");
				//eother.save();
				eplayer.setNationRank("officer");
				//eplayer.save();
				
				nation.broadcast("#5F5" + player.getName() + " has transferred their leadership to " + other.getName());
				
				return true;
			}
		}, "<player>");
	}

}
