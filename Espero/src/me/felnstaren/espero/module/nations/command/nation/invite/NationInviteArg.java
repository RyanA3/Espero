package me.felnstaren.espero.module.nations.command.nation.invite;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubArgument;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.util.message.Messenger;

public class NationInviteArg extends SubArgument {

	public NationInviteArg() {
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
				
				nation.getInvites().add(invitee.getUniqueId());
				nation.save();
				
				Messenger.send(player, "#5F5Successfully invited " + invitee.getDisplayName() + " #5F5to your nation!");
				Messenger.send(invitee, "#F5FYou've been invited to join " + nation.getDisplayName());
				
				return true;
			}
		}, "<player>");
	}
	
}
