package me.felnstaren.espero.module.nations.command.nation.disband;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationDisbandSub extends SubCommand {

	public NationDisbandSub() {
		super("disband");
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		NationPlayerRank rank = eplayer.getNationRank();
		if(!rank.getLabel().equals("leader")) {
			Messenger.send(player, Format.ERROR_NATION_PERMISSION.message());
			return true;
		}
		
		ArrayList<UUID> members = new ArrayList<UUID>(nation.getMembers());
		for(UUID member : members) {
			EsperoPlayer emember = Espero.PLAYERS.getPlayer(member);
			emember.setNationRank("recruit");
			emember.setNation(null);
		}
		

		Messenger.broadcast("#F22" + nation.getDisplayName() + " #F55has been disbanded!");
		Nations.inst().unregister(nation.getID());
		nation.disband();
		return true;
	}
	
}
