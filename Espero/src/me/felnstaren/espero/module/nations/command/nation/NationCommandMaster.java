package me.felnstaren.espero.module.nations.command.nation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.MasterCommand;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.command.nation.create.NationCreateSub;
import me.felnstaren.espero.module.nations.command.nation.demote.NationDemoteSub;
import me.felnstaren.espero.module.nations.command.nation.invite.NationInviteSub;
import me.felnstaren.espero.module.nations.command.nation.join.NationJoinSub;
import me.felnstaren.espero.module.nations.command.nation.kick.NationKickSub;
import me.felnstaren.espero.module.nations.command.nation.leader.NationLeaderSub;
import me.felnstaren.espero.module.nations.command.nation.leave.NationLeaveSub;
import me.felnstaren.espero.module.nations.command.nation.promote.NationPromoteSub;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.util.message.Messenger;
import me.felnstaren.espero.util.message.json.Message;
import me.felnstaren.espero.util.message.json.TextComponent;

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
				}
				
				Messenger.send(player, message);
				return true;
			}
		}, "nation", "espero.nation");

		commands.add(new NationCreateSub());
		commands.add(new NationLeaveSub());
		commands.add(new NationJoinSub());
		commands.add(new NationInviteSub());
		commands.add(new NationLeaderSub());
		commands.add(new NationPromoteSub());
		commands.add(new NationDemoteSub());
		commands.add(new NationKickSub());
	}
	
}
