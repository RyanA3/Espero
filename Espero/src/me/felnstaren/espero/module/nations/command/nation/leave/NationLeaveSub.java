package me.felnstaren.espero.module.nations.command.nation.leave;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.Nations;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationLeaveSub extends SubCommand {

	public NationLeaveSub() {
		super("leave");
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		NationPlayerRank rank = eplayer.getNationRank();
		if(rank.getLabel().equals("leader") && nation.getMembers().size() > 1) {
			Messenger.send(player, "#F55You must delegate a new leader before you leave!");
			return true;
		}
		
		Nations.setNation(eplayer, null);

		if(nation.getMembers().size() == 0) {
			Messenger.broadcast("#F22" + nation.getDisplayName() + " #F55has been disbanded!");
			Nations.disband(nation);
		}
		
		nation.broadcast("#5F5" + player.getDisplayName() + " has left the nation!");
		Messenger.send(player, "#5F5You've left the nation of #F22" + nation.getDisplayName() + " #5F5behind, good luck on your travels");
		
		return true;
	}

	
	
}
