package me.felnstaren.espero.module.nations.command.nation.kick;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubArgument;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.util.message.Messenger;

public class NationKickArg extends SubArgument {

	public NationKickArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F55You must be in a nation to use this command!");
					return true;
				}
				
				if(!eplayer.getNationRank().isPermitted("kick")) {
					Messenger.send(player, "#F55You do not have permission to do this in your nation!");
					return true;
				}
				
				Player other = Bukkit.getPlayerExact(args[current]);
				if(other == null) {
					Messenger.send(player, "#F55This player is not online at the moment!");
					return true;
				}
				
				EsperoPlayer eother = new EsperoPlayer(other);
				if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
					Messenger.send(player, "#F55This player is not in your nation!");
					return true;
				}
				
				if(eother.getNationRank().getWeight() >= eplayer.getNationRank().getWeight()) {
					Messenger.send(player, "#F55You can't kick this player!");
					return true;
				}
				
				nation.broadcast("#5F5" + player.getDisplayName() + " #5F5has kicked " + other.getDisplayName() + " #5F5from the nation!");
				eother.setNation(null);
				eother.setRank("recruit");
				eother.save();
				
				
				return true;
			}
		}, "<player>");
	}
	
}