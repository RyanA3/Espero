package me.felnstaren.espero.module.nations.command.nation.join;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubArgument;

public class NationJoinArg extends SubArgument {

	public NationJoinArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
				
				if(eplayer.getNation() != null) {
					Messenger.send(player, "#F55You must leave your nation first!");
					return true;
				}
				
				String name = "";
				for(int i = 1; i < args.length && i < 4; i++) {
					if(i > 1) name += " ";
					name += args[i];
				}
				
				Nation to_join = Nations.inst().getNation(name);
				if(to_join == null) {
					Messenger.send(player, "#F55Couldn't find a nation called " + name);
					return true;
				}
				
				if(!to_join.getInvites().contains(player.getUniqueId())) {
					Messenger.send(player, "#F55This nation hasn't invited you to join them!");
					return true;
				}
				
				eplayer.setNation(to_join);
				//eplayer.save();
				to_join.save();
				
				to_join.broadcast("5F5" + to_join.getDisplayName() + " has joined the nation!");
				return true;
			}
		}, "<nation>");
	}
	
}
