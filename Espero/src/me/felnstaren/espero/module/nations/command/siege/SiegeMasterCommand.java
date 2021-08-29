package me.felnstaren.espero.module.nations.command.siege;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.MasterCommand;
import me.felnstaren.felib.command.TabSuggestor;

public class SiegeMasterCommand extends MasterCommand {

	public SiegeMasterCommand() {
		super("siege", "", new TabSuggestor("<player>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				ArrayList<String> players = new ArrayList<String>();
				for(Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());
				return players;
			}
		},
		new TabSuggestor("<nation>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				return NationRegistry.inst().getNationsNames();
			}
		},
		new TabSuggestor("<town>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				return TownRegistry.inst().getTownsNames();
			}
		});
		
		arguments.add(new SiegeTownArgument());
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "/siege <town>");
		return true;
	}

}
