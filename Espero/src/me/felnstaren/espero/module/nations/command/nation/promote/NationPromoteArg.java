package me.felnstaren.espero.module.nations.command.nation.promote;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubArgument;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.util.message.Messenger;

public class NationPromoteArg extends SubArgument {

	public NationPromoteArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F55You must be in a nation to use this command!");
					return true;
				}
				
				if(!eplayer.getNationRank().isPermitted("promote")) {
					Messenger.send(player, "#F55You do not have permission to do this in your nation!");
					return true;
				}
				
				Player other = Bukkit.getPlayerExact(args[current]);
				if(other == null) {
					Messenger.send(player, "#F55This player is not online at the moment!");
					return true;
				}
				
				if(other.equals(player)) {
					Messenger.send(player, "#F55You can't promote yourself!");
					return true;
				}
				
				EsperoPlayer eother = new EsperoPlayer(other);
				if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
					Messenger.send(player, "#F55This player is not in your nation!");
					return true;
				}
				
				NationPlayerRank promotion = nation.getNextHighestRank(eother.getNationRank());
				if(promotion.getWeight() >= eplayer.getNationRank().getWeight()) {
					Messenger.send(player, "#F55You can't promote this player above your own rank!");
					return true;
				}
				
				eother.setRank(promotion.getLabel());
				nation.broadcast("#F5F" + other.getDisplayName() + "#5F5has been promoted to the rank of #999" + promotion.getDisplayName() + " #5f5by " + player.getDisplayName());
				
				return true;
			}
		}, "<player>");
	}

}
