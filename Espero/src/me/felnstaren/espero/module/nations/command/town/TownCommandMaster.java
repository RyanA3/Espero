package me.felnstaren.espero.module.nations.command.town;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.command.town.info.TownHelpCommand;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.MasterCommand;
import me.felnstaren.felib.command.TabSuggestor;

public class TownCommandMaster extends MasterCommand {

	public TownCommandMaster() {
		super("town", "", new TabSuggestor("<player>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				ArrayList<String> players = new ArrayList<String>();
				for(Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());
				return players;
			}
		},
		new TabSuggestor("<nation>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				return NationRegistry.inst().getNationNames();
			}
		},
		new TabSuggestor("<claimtype>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				ArrayList<String> claim_types = new ArrayList<String>();
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer((Player) sender); //new EsperoPlayer((Player) sender);
				Nation nation = eplayer.getNation();
				
				if(nation == null) return claim_types;
				claim_types.add("nation");
				for(Town town : nation.getTowns()) 
					claim_types.add(town.name);
				
				return claim_types;
			}
		});
		
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
