package me.felnstaren.espero.module.nations.command.town;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.command.town.claim.TownClaimCommand;
import me.felnstaren.espero.module.nations.command.town.claim.TownUnclaimCommand;
import me.felnstaren.espero.module.nations.command.town.info.TownHelpCommand;
import me.felnstaren.espero.module.nations.command.town.info.TownInfoCommand;
import me.felnstaren.espero.module.nations.command.town.info.TownInvitesCommand;
import me.felnstaren.espero.module.nations.command.town.info.TownListCommand;
import me.felnstaren.espero.module.nations.command.town.info.TownMembersCommand;
import me.felnstaren.espero.module.nations.command.town.players.TownChatCommand;
import me.felnstaren.espero.module.nations.command.town.players.TownInviteCommand;
import me.felnstaren.espero.module.nations.command.town.players.TownJoinCommand;
import me.felnstaren.espero.module.nations.command.town.players.TownKickCommand;
import me.felnstaren.espero.module.nations.command.town.players.TownLeaveCommand;
import me.felnstaren.espero.module.nations.command.town.players.TownUninviteCommand;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.town.TownRegistry;
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
				return NationRegistry.inst().getNationsNames();
			}
		},
		new TabSuggestor("<town>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				return TownRegistry.inst().getTownsNames();
			}
		});
		
		
		
		commands.add(new TownClaimCommand());
		commands.add(new TownUnclaimCommand());
		
		commands.add(new TownHelpCommand());
		commands.add(new TownInfoCommand());
		commands.add(new TownInvitesCommand());
		commands.add(new TownListCommand());
		commands.add(new TownMembersCommand());

		commands.add(new TownChatCommand());
		commands.add(new TownInviteCommand());
		commands.add(new TownJoinCommand());
		commands.add(new TownKickCommand());
		commands.add(new TownLeaveCommand());
		commands.add(new TownUninviteCommand());
		
		commands.add(new TownFoundCommand());
		commands.add(new TownDisbandCommand());
		
		commands.add(new TownRelicCommand());
		
		commands.add(new TownMenuCommand());	
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, TownHelpCommand.PAGES[0]);
		return true;
	}

}
