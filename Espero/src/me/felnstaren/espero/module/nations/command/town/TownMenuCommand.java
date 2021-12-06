package me.felnstaren.espero.module.nations.command.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.menu.town.TownMenu;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;
import me.felnstaren.felib.ui.menu.MenuSessionHandler;

public class TownMenuCommand extends SubCommand {

	public TownMenuCommand() {
		super("menu");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer ep = Espero.PLAYERS.getPlayer(player);
				
				Town town = TownRegistry.inst().getTown(args[current]);
				if(town == null) {
					Messenger.send(player, Color.RED + "Invalid Town: " + args[current]);
					return true;
				}
				
				TownMenu menu = new TownMenu(town, ep);
				menu.open(player);
				MenuSessionHandler.inst().startSession(player, menu);
				
				return true;
			}
		});
	}


	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town menu <town>");
		return true;
	}

}
