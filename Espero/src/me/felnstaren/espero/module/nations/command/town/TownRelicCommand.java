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

public class TownRelicCommand extends SubCommand {

	public TownRelicCommand() {
		super("relic");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer dp = Espero.PLAYERS.getPlayer(player);
				Town town = TownRegistry.inst().getTown(args[current]);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + "Invalid Town: " + args[current]);
					return true;
				}
				
				if(!town.hasPermission(dp, Permission.RELIC)) {
					Messenger.send(sender, Color.RED + "You do not have permission to this town's relic!");
					return true;
				}
				
				if(town.getRelic().isHolding(player)) return true;
				
				player.getInventory().addItem(town.generateRelicItem());
				
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /town relic <town>");
		return true;
	}

}
