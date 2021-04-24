package me.felnstaren.espero.module.nations.command.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.command.town.info.TownHelpCommand;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.MasterCommand;
import me.felnstaren.felib.command.TabSuggestor;

public class TownCommandMaster extends MasterCommand {

	protected TownCommandMaster(String label, String permission, TabSuggestor[] tab_suggestors) {
		super(label, permission, tab_suggestors);
		commands.add(new TownHelpCommand());
		commands.add(new TownClaimCommand());
		commands.add(new TownUnclaimCommand());
		commands.add(new TownFoundCommand());
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, TownHelpCommand.PAGES[0]);
		return false;
	}

}
