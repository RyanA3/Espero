package me.felnstaren.espero.module.nations.command.nation;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.command.nation.claim.NationClaimSub;
import me.felnstaren.espero.module.nations.command.nation.create.NationCreateSub;
import me.felnstaren.espero.module.nations.command.nation.demote.NationDemoteSub;
import me.felnstaren.espero.module.nations.command.nation.invite.NationInviteSub;
import me.felnstaren.espero.module.nations.command.nation.join.NationJoinSub;
import me.felnstaren.espero.module.nations.command.nation.kick.NationKickSub;
import me.felnstaren.espero.module.nations.command.nation.leader.NationLeaderSub;
import me.felnstaren.espero.module.nations.command.nation.leave.NationLeaveSub;
import me.felnstaren.espero.module.nations.command.nation.map.NationMapSub;
import me.felnstaren.espero.module.nations.command.nation.promote.NationPromoteSub;
import me.felnstaren.espero.module.nations.command.nation.unclaim.NationUnclaimSub;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.felib.chat.Message;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.chat.TextComponent;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.MasterCommand;
import me.felnstaren.felib.command.TabSuggestor;

public class NationCommandMaster extends MasterCommand {

	public NationCommandMaster() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				Message message = new Message();
				message.addComponent(new TextComponent("-= Nations =-").setColor("#0077FF"));
				message.addComponent(new TextComponent("   by Felns").setColor("#CCCCCC").setItalic(true));
				message.addComponent(new TextComponent("\n /nation").setColor("#AAAAAA"));
				message.addComponent(new TextComponent("\n         map").setColor("#AAAAAA"));
				
				if(nation == null) {
					message.addComponent(new TextComponent("\n         create <name>").setColor("#AAAAAA"));
					message.addComponent(new TextComponent("\n         join <nation>").setColor("#AAAAAA"));
				} else {
					message.addComponent(new TextComponent("\n         leave").setColor("#AAAAAA"));
					
					NationPlayerRank rank = eplayer.getNationRank();
					if(rank.isPermitted("recruit")) 
						message.addComponent(new TextComponent("\n         invite <player>").setColor("#AAAAAA"));
					if(rank.isPermitted("kick"))
						message.addComponent(new TextComponent("\n         kick <player>").setColor("#AAAAAA"));
					if(rank.isPermitted("promote"))
						message.addComponent(new TextComponent("\n         promote <player>").setColor("#AAAAAA"));
					if(rank.isPermitted("demote"))
						message.addComponent(new TextComponent("\n         demote <player>").setColor("#AAAAAA"));
					if(nation.getNextHighestRank(rank) == null)
						message.addComponent(new TextComponent("\n         leader <player>").setColor("#AAAAAA"));
					if(rank.isPermitted("claim"))
						message.addComponent(new TextComponent("\n         claim").setColor("#AAAAAA"));
					if(rank.isPermitted("claim"))
						message.addComponent(new TextComponent("\n         unclaim").setColor("#AAAAAA"));
				}
				
				Messenger.send(player, message);
				return true;
			}
		}, "nation", "espero.nation", 
		new TabSuggestor("<player>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				ArrayList<String> players = new ArrayList<String>();
				for(Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());
				return players;
			}
		},
		new TabSuggestor("<nation>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				return Nations.getInstance().getNationNames();
			}
		},
		new TabSuggestor("<claimtype>") {
			public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
				ArrayList<String> claim_types = new ArrayList<String>();
				EsperoPlayer eplayer = new EsperoPlayer((Player) sender);
				Nation nation = eplayer.getNation();
				
				if(nation == null) return claim_types;
				claim_types.add("nation");
				for(Town town : nation.getTowns()) 
					claim_types.add(town.name);
				
				return claim_types;
			}
		});

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
	}
	
}
