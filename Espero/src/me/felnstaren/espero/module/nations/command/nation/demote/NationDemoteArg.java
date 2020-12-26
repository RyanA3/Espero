package me.felnstaren.espero.module.nations.command.nation.demote;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.rilib.chat.Messenger;
import me.felnstaren.rilib.command.CommandStub;
import me.felnstaren.rilib.command.SubArgument;

public class NationDemoteArg extends SubArgument {
	
	public NationDemoteArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F55You must be in a nation to use this command!");
					return true;
				}
				
				if(!eplayer.getNationRank().isPermitted("demote")) {
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
					Messenger.send(player, "#F55You can't demote this player!");
					return true;
				}
				
				NationPlayerRank demotion = nation.getNextLowestRank(eother.getNationRank());
				if(demotion == null) {
					Messenger.send(player, "#F55This player is already at the lowest rank possible!");
					return true;
				}
				
				eother.setRank(demotion.getLabel());
				eother.save();
				nation.broadcast("#F5F" + other.getDisplayName() + " #5F5has been demoted to the rank of #999" + demotion.getDisplayName() + " #5F5by " + player.getDisplayName());
				
				return true;
			}
		}, "<player>");
	}

}
