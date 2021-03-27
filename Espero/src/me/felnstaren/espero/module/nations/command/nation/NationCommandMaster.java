package me.felnstaren.espero.module.nations.command.nation;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.command.nation.chat.NationChatSub;
import me.felnstaren.espero.module.nations.command.nation.claim.NationClaimSub;
import me.felnstaren.espero.module.nations.command.nation.create.NationCreateSub;
import me.felnstaren.espero.module.nations.command.nation.demote.NationDemoteSub;
import me.felnstaren.espero.module.nations.command.nation.disband.NationDisbandSub;
import me.felnstaren.espero.module.nations.command.nation.infos.help.NationHelpSub;
import me.felnstaren.espero.module.nations.command.nation.infos.info.NationInfoSub;
import me.felnstaren.espero.module.nations.command.nation.infos.invites.NationInvitesSub;
import me.felnstaren.espero.module.nations.command.nation.infos.members.NationMembersSub;
import me.felnstaren.espero.module.nations.command.nation.infos.towns.NationTownsSub;
import me.felnstaren.espero.module.nations.command.nation.invite.NationInviteSub;
import me.felnstaren.espero.module.nations.command.nation.join.NationJoinSub;
import me.felnstaren.espero.module.nations.command.nation.kick.NationKickSub;
import me.felnstaren.espero.module.nations.command.nation.leader.NationLeaderSub;
import me.felnstaren.espero.module.nations.command.nation.leave.NationLeaveSub;
import me.felnstaren.espero.module.nations.command.nation.list.NationListSub;
import me.felnstaren.espero.module.nations.command.nation.map.NationMapSub;
import me.felnstaren.espero.module.nations.command.nation.promote.NationPromoteSub;
import me.felnstaren.espero.module.nations.command.nation.unclaim.NationUnclaimSub;
import me.felnstaren.espero.module.nations.command.nation.uninvite.NationUninviteSub;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.espero.module.nations.nation.Town;
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
		new TabSuggestor("<nation>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				return Nations.inst().getNationNames();
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
		commands.add(new NationCreateSub());
		commands.add(new NationLeaveSub());
		commands.add(new NationJoinSub());
		commands.add(new NationInviteSub());
		commands.add(new NationLeaderSub());
		commands.add(new NationPromoteSub());
		commands.add(new NationDemoteSub());
		commands.add(new NationKickSub());
		commands.add(new NationClaimSub());
		commands.add(new NationMapSub());
		commands.add(new NationUnclaimSub());
		commands.add(new NationMembersSub());
		commands.add(new NationListSub());
		commands.add(new NationChatSub());
		commands.add(new NationInfoSub());
		commands.add(new NationDisbandSub());
		commands.add(new NationInvitesSub());
		commands.add(new NationTownsSub());
		commands.add(new NationUninviteSub());
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		Messenger.send(player, NationHelpSub.PAGES[0]);
		return true;
	}

}
