package me.felnstaren.espero.module.nations.command.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class TownDisbandCommand extends SubCommand {

	public TownDisbandCommand() {
		super("disband");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
				
				String town_name = "";
				for(int i = 2; i < args.length; i++) {
					town_name += args[i];
					if(i < args.length - 1) town_name += " ";
				}
				
				Town town = TownRegistry.inst().getTown(town_name);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + town_name + " is not a valid town");
					return true;
				}
				
				if(!town.hasPermission(player, Permission.TOWN_DISBAND)) {
					Messenger.send(sender, Color.RED + "You do not have permission to disband " + town_name);
					return true;
				}
				
				town.disband();
				Messenger.broadcast(Color.RED + town.name + " has been disbanded!");
				return true;
			}
		});
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town disband <town>"
				+ "\n are you sure you want to do this...");
		return true;
	}

}
