package me.felnstaren.espero.module.nations.command.siege;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.menu.siege.SiegePrompt;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.espero.module.nations.town.siege.Siege;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.ui.prompt.PromptHandler;

public class SiegeTownArgument extends SubArgument {

	public SiegeTownArgument() {
		super("<town>");
		arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Town town = TownRegistry.inst().getTown(args[current]);
				
				if(town == null) {
					Messenger.send(sender, Color.RED + "Invalid Town: " + args[current]);
					return true;
				}
				
				EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
				
				if(!town.hasPermission(player, Permission.START_SIEGE)) {
					Messenger.send(sender, Color.RED + "You do not have permission to start a siege by this town!");
					return true;
				}
				
				Town defender = TownRegistry.inst().getTown(args[current-1]);
				
				if(defender == null) {
					Messenger.send(sender, Color.RED + "Invalid Town: " + args[current-1]);
					return true;
				}
				
				if(defender.isInSiege()) {
					Messenger.send(sender, Color.RED + "This town is already under siege!");
					return true;
				}
				
				if(!defender.getRelic().exists()) {
					Messenger.send(sender, Color.RED + "This town hasn't placed their relic!");
					return true;
				}
				
				PromptHandler.inst().register(new SiegePrompt((Player) sender, town, defender));
				
				return true;
			}
		});
	}


	public boolean stub(CommandSender sender, String[] args, int current) {
		Town town = TownRegistry.inst().getTown(args[current]);
		if(town == null) {
			Messenger.send(sender, Color.RED + "Invalid Town: " + args[current]);
			return true;
		}
		
		Messenger.send(sender, town.neatHeader() + Color.WHEAT + " Siege Info ");
		
		Siege siege = town.getSiege();
		if(siege == null) {
			Messenger.send(sender, Color.TURQUOISE + "This town is not currently under siege,"
					+ "\nEnter an attacking town to start a siege with   /siege <defender> <attacker=you>");
			return true;
		}
		
		Messenger.send(sender, 
				Format.LABEL_ARG.message("Stage", siege.getStage().name()) + "\n"
				+ Format.LABEL_ARG.message("Runtime", siege.getRuntime() + "") + "\n"
				+ Format.LABEL_ARG.message("Attacker", siege.getAttacker().getDisplayName()));
		return true;
	}

}
