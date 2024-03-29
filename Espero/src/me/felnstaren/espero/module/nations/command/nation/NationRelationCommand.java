package me.felnstaren.espero.module.nations.command.nation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.menu.relation.NationRelationPrompt;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;
import me.felnstaren.felib.ui.prompt.PromptHandler;

public class NationRelationCommand extends SubCommand {

	public NationRelationCommand() {
		super("relation");
		arguments.add(new SubArgument("<nation>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
					return true;
				}
				
				//if(!Nations.isPermitted(eplayer, nation, "relation")) {
				//	Messenger.send(player, Format.ERROR_NATION_PERMISSION.message());
				//	return true;
				//}
				
				Nation of = NationRegistry.inst().getNation(args[current]);
				if(of == null) {
					Messenger.send(player, Format.ERROR_INVALID_ARGUMENT.message(args[current]));
					return true;
				}
				
				PromptHandler.inst().register(new NationRelationPrompt(player, nation, of));
				
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, Color.RED + "Usage: /nation relation <nation>");
		return true;
	}

}
