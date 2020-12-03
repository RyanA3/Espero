package me.felnstaren.espero.module.nations.command.nation.join;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubArgument;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.espero.util.message.Messenger;

public class NationJoinArg extends SubArgument {

	public NationJoinArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(Messenger.color("&cOnly players can use this command!"));
					return true;
				}
				
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				
				if(eplayer.getNation() != null) {
					Messenger.send(player, "#F55You must leave your nation first!");
					return true;
				}
				
				Nation to_join = Nations.getInstance().getNation(args[current]);
				if(to_join == null) {
					Messenger.send(player, "#F55Couldn't find a nation called " + args[current]);
					return true;
				}
				
				if(!to_join.getInvites().contains(player.getUniqueId())) {
					Messenger.send(player, "#F55This nation hasn't invited you to join them!");
					return true;
				}
				
				eplayer.setNation(to_join);
				eplayer.save();
				to_join.save();
				
				Messenger.send(player, "#5F5You've joined " + to_join.getDisplayName());
				return true;
			}
		}, "<nation>");
	}
	
}
