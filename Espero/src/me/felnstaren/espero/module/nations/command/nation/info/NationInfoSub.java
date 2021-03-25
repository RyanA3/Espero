package me.felnstaren.espero.module.nations.command.nation.info;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.command.nation.members.NationMembersSub;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationInfoSub extends SubCommand {

	public NationInfoSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F77Usage: /nation info <nation>");
					return true;
				}
				
				Messenger.send(player, constructNationInfo(nation));
				return true;
			}
		}, "info");
		
		arguments.add(new NationInfoArg());
	}
	
	
	
	public static String constructNationInfo(Nation nation) {
		String message = Color.LIGHT_GRAY + "&a-=&8[ &7" + nation.getDisplayName() + " &8]&a=- \n";
		message += Color.LIGHT_BLUE + "Claim[" + nation.getArea() + "] Perimeter[" + nation.getPerimeter() + "] Town[" + nation.getTownArea() + "]\n";
		message += Color.LIGHT_BLUE + "Balance[" + nation.getBalance() + "] Members[" + nation.getMembers().size() + "]";
		message += Color.LIGHT_GRAY + "\n   [Members]\n#6D6";
		message += NationMembersSub.constructMembersList(nation);
		message += Color.LIGHT_GRAY + "\n   [Towns]\n#6D6";
		for(Town t : nation.getTowns()) message += " " + t.name;
		
		return message;
	}
	
}
