package me.felnstaren.espero.module.nations.command.nation.uninvite;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.Nations;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;

public class NationUninviteArg extends SubArgument {

	public NationUninviteArg() {
		super("<player>");
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, "#F55You must be in a nation to revoke an invitation to it!");
			return true;
		}
		
		if(!Nations.isPermitted(eplayer, nation, "recruit")) {
			Messenger.send(player, "#F55You do not have permission to revoke invitations in your nation!");
			return true;
		}
		
		Player invitee = Bukkit.getPlayerExact(args[current]);
		EsperoPlayer eother;
		if(invitee == null)
			eother = Espero.PLAYERS.getPlayer(Espero.OFFLINE_PLAYERS.getID(args[current]));
		else 
			eother = Espero.PLAYERS.getPlayer(invitee);
		if(eother == null) {
			Messenger.send(player, Format.ERROR_PLAYER_NOT_ONLINE.message());
			return true;
		}
		
		UUID iid = eother.getUniqueId();
		if(!nation.getInvites().contains(iid) || nation.getMembers().contains(iid)) {
			Messenger.send(player, "#F55" + args[current] + " is not invited to your nation!");
			return true;
		}
		
		nation.getInvites().remove(eother.getUniqueId());
		nation.broadcast("#5F5" + player.getDisplayName() + " has revoked " + args[current] + "'s invitation to the nation!");
		if(invitee != null) Messenger.send(invitee, "#5F5Your invitation to " + nation.getDisplayName() + " was revoked!");
		
		return true;
	}

}
