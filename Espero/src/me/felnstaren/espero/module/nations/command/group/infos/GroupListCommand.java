package me.felnstaren.espero.module.nations.command.group.infos;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class GroupListCommand extends SubCommand {

	public GroupListCommand() {
		super("list");
		commands.add(new SubCommand("all") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				String message =  Format.HEADER.message("Espero's Groups") + "\n" + Color.TURQUOISE;
				ArrayList<String> gnames = GroupRegistry.inst().getGroupsDisplayNames();
				for(String name : gnames) message += "   " + name;
				Messenger.send(sender, message);
				return true;
			}
		});
		arguments.add(new SubArgument("<player>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				UUID playerid = Espero.OFFLINE_PLAYERS.getID(args[current]);
				
				if(playerid == null) {
					Messenger.send(sender, Color.RED + "Invalid Player: " + args[current]);
					return true;
				}
				
				EsperoPlayer player = Espero.PLAYERS.getPlayer(playerid);
				ArrayList<Group> groups = player.getGroups();
				
				String message = Format.HEADER.message(args[current] + "'s Groups") + "\n" + Color.TURQUOISE;
				for(Group g : groups) message += "    " + g.getDisplayName() + ":" + g.relRank(player);
				Messenger.send(sender, message);
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		ArrayList<Group> groups = eplayer.getGroups();
		
		String message =  Format.HEADER.message("Your Groups") + "\n" + Color.TURQUOISE;
		for(Group g : groups) message += "   " + g.getDisplayName() + ":" + g.relRank(eplayer);
		Messenger.send(player, Messenger.color(message));
		return true;
	}
	
}
