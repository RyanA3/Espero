package me.felnstaren.espero.module.nations.command.nation.leave;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubCommand;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.espero.util.message.Messenger;

public class NationLeaveSub extends SubCommand {

	public NationLeaveSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(Messenger.color("&cOnly players can use this command!"));
					return true;
				}
				
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F55You aren't in a nation!");
					return true;
				}
				
				NationPlayerRank rank = eplayer.getNationRank();
				if(rank.getLabel().equals("leader") && nation.getMembers().size() > 1) {
					Messenger.send(player, "#F55You must delegate a new leader before you leave!");
					return true;
				}
				
				eplayer.setNation(null);
				eplayer.save();
				
				if(nation.getMembers().size() == 0) {
					Messenger.broadcast("#F22" + nation.getDisplayName() + " #F55has been disbanded!");
					Nations.getInstance().unregister(nation.getID());
					nation.disband();
				}
				
				nation.broadcast("#5F5" + player.getDisplayName() + " has left the nation!");
				Messenger.send(player, "#5F5You've left the nation of #F22" + nation.getDisplayName() + " #5F5behind, good luck on your travels");
				
				return true;
			}
		}, "leave");
	}

	
	
}
