package me.felnstaren.espero.module.nations.command.nation.leader;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.config.EsperoPlayer;
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
				EsperoPlayer eplayer = new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F55You must be in a nation to use this command!");
					return true;
				}
				
				NationPlayerRank rank = eplayer.getNationRank();
				if(!rank.getLabel().equals("leader")) {
					Messenger.send(player, "#F55You do not have permission to do this!");
					return true;
				}
				
				Player other = Bukkit.getPlayerExact(args[current]);
				if(other == null) {
					Messenger.send(player, "#F55This player is not online at the moment!");
					return true;
				}
				
				if(other.equals(player)) {
					Messenger.send(player, "#F55You can't transfer the leadership role to yourself!");
					return true;
				}
				
				EsperoPlayer eother = new EsperoPlayer(other);
				if(eother.getNation() == null || !eother.getNation().getID().equals(nation.getID())) {
					Messenger.send(player, "#F55This player is not in your nation!");
					return true;
				}
				
				eother.setRank("leader");
				eother.save();
				eplayer.setRank("officer");
				eplayer.save();
				
				nation.broadcast("#5F5" + player.getName() + " has transferred their leadership to " + other.getName());
				
				return true;
			}
		}, "<player>");
	}

}
