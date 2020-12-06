package me.felnstaren.espero.module.nations.command.nation.invite;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubArgument;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.util.message.Messenger;

public class NationInviteArg extends SubArgument {

	//Poggers for Programming
	//LoveArrow was here
	
	public NationInviteArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F55You must be in a nation to invite someone to it!");
					return true;
				}
				
				if(!eplayer.hasPermission("recruit", nation)) {
					Messenger.send(player, "#F55You do not have permission to invite players into your nation!");
					return true;
				}
				
				Player invitee = Bukkit.getPlayerExact(args[current]);
				if(invitee == null) {
					Messenger.send(player, "#F55" + args[current] + " is not online right now or does not exist!");
					return true;
				}
				
				UUID iid = invitee.getUniqueId();
				if(nation.getInvites().contains(iid) || nation.getMembers().contains(iid)) {
					Messenger.send(player, "#F55" + args[current] + " is already invited or a member of this nation!");
					return true;
				}
				
				nation.getInvites().add(invitee.getUniqueId());
				nation.save();
				
				nation.broadcast("#5F5" + player.getDisplayName() + " has invited " + invitee.getDisplayName() + " to the nation!");
				Messenger.send(invitee, "#5F5You've been invited to join " + nation.getDisplayName());
				
				return true;
			}
		}, "<player>");
	}
	
}
