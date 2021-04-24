package me.felnstaren.espero.module.nations.command.town;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.command.nation.unclaim.NationUnclaimSub;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class TownUnclaimCommand extends SubCommand {

	public TownUnclaimCommand() {
		super("unclaim");
	}

	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		if(!eplayer.getNationRank().isPermitted("claim")) {
			Messenger.send(player, Format.ERROR_NATION_PERMISSION.message());
			return true;
		}
		
		Chunk loc = player.getLocation().getChunk();
		int cx = loc.getX(); int cz = loc.getZ();
		ClaimChunk claim = ClaimBoard.inst().getClaim(cx, cz);
		
		if(claim == null) {
			Messenger.send(player, Color.RED + "This chunk is not claimed!");
			return true;
		}
		
		if(!claim.nation.equals(nation.getID())) {
			Messenger.send(player, Format.ERROR_CHUNK_ALREADY_CLAIMED.message());
			return true;
		}
		
		if(NationUnclaimSub.isLegalUnclaim(cx, cz, nation, claim.town)) {
			nation.addTownArea(-1);
			nation.getTown(claim.town).addArea(-1);
			ClaimBoard.inst().claim(cx, cz, nation.getID(), 0);
		}
		else {
			Messenger.send(player, Color.RED + "Unclaiming this would leave two sections of your town disconnected, "
					+ "town claims must remain contiguous, please unclaim the other parts first");
			return true;
		}
		
		return true;
	}

}
