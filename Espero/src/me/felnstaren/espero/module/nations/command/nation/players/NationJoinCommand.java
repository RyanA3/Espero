package me.felnstaren.espero.module.nations.command.nation.players;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class NationJoinCommand extends SubCommand {

	public NationJoinCommand() {
		super("join");
		
		arguments.add(new SubArgument("<nation>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				
				if(eplayer.getNation() != null) {
					Messenger.send(player, Format.ERROR_IN_NATION.message());
					return true;
				}
				
				Nation to_join = NationRegistry.inst().getNation(args[current]);
				if(to_join == null) {
					Messenger.send(player, Format.ERROR_INVALID_ARGUMENT.message().replace("%argument%", args[current]));
					return true;
				}
				
				if(!to_join.getInvites().contains(player.getUniqueId())) {
					Messenger.send(player, Format.ERROR_NOT_INVITED.message());
					return true;
				}
				to_join.join(eplayer);
				to_join.broadcast("#5F5" + player.getName() + " has joined the nation!");
				return true;
			}
		});
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, "#F55Usage: #F77/nation join <nation>");
		return true;
	}
	
}
