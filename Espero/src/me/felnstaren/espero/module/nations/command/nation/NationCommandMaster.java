package me.felnstaren.espero.module.nations.command.nation;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.command.nation.claims.NationClaimCommand;
import me.felnstaren.espero.module.nations.command.nation.claims.NationUnclaimCommand;
import me.felnstaren.espero.module.nations.command.nation.infos.NationListSub;
import me.felnstaren.espero.module.nations.command.nation.infos.NationMapSub;
import me.felnstaren.espero.module.nations.command.nation.infos.help.NationHelpSub;
import me.felnstaren.espero.module.nations.command.nation.infos.info.NationInfoSub;
import me.felnstaren.espero.module.nations.command.nation.infos.invites.NationInvitesSub;
import me.felnstaren.espero.module.nations.command.nation.infos.members.NationMembersSub;
import me.felnstaren.espero.module.nations.command.nation.infos.towns.NationTownsSub;
import me.felnstaren.espero.module.nations.command.nation.players.NationChatCommand;
import me.felnstaren.espero.module.nations.command.nation.players.NationDemoteCommand;
import me.felnstaren.espero.module.nations.command.nation.players.NationInviteCommand;
import me.felnstaren.espero.module.nations.command.nation.players.NationJoinCommand;
import me.felnstaren.espero.module.nations.command.nation.players.NationKickCommand;
import me.felnstaren.espero.module.nations.command.nation.players.NationLeaderCommand;
import me.felnstaren.espero.module.nations.command.nation.players.NationLeaveCommand;
import me.felnstaren.espero.module.nations.command.nation.players.NationPromoteCommand;
import me.felnstaren.espero.module.nations.command.nation.players.NationUninviteCommand;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.MasterCommand;
import me.felnstaren.felib.command.TabSuggestor;

public class NationCommandMaster extends MasterCommand {

	public NationCommandMaster() {
		super("nation", "espero.nation", 
		new TabSuggestor("<player>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				ArrayList<String> players = new ArrayList<String>();
				for(Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());
				return players;
			}
		},
		new TabSuggestor("<komrade>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
				ArrayList<Player> players = player.getNation().getOnlineMembers();
				ArrayList<String> pnames = new ArrayList<String>();
				for(Player p : players) pnames.add(p.getName());
				return pnames;
			}
		},
		new TabSuggestor("<nonkomrade>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				EsperoPlayer player = Espero.PLAYERS.getPlayer((Player) sender);
				ArrayList<Player> komrades = player.getNation().getOnlineMembers();
				ArrayList<String> pnames = new ArrayList<String>();
				int i = 0; for(Player p : Bukkit.getOnlinePlayers()) {
					if(!p.getUniqueId().equals(komrades.get(i).getUniqueId())) 
						pnames.add(p.getName());
				i++;		   }
				return pnames;
			}
		},
		new TabSuggestor("<nation>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				return NationRegistry.inst().getNationNames();
			}
		},
		new TabSuggestor("<town>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				return TownRegistry.inst().getTownNames();
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

		commands.add(new NationHelpSub());
		commands.add(new NationFoundCommand());
		commands.add(new NationLeaveCommand());
		commands.add(new NationJoinCommand());
		commands.add(new NationInviteCommand());
		commands.add(new NationLeaderCommand());
		commands.add(new NationPromoteCommand());
		commands.add(new NationDemoteCommand());
		commands.add(new NationKickCommand());
		commands.add(new NationClaimCommand());
		commands.add(new NationMapSub());
		commands.add(new NationUnclaimCommand());
		commands.add(new NationMembersSub());
		commands.add(new NationListSub());
		commands.add(new NationChatCommand());
		commands.add(new NationInfoSub());
		commands.add(new NationDisbandCommand());
		commands.add(new NationInvitesSub());
		commands.add(new NationTownsSub());
		commands.add(new NationUninviteCommand());
		commands.add(new NationRelationCommand());
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		Messenger.send(player, NationHelpSub.PAGES[0]);
		return true;
	}

}
