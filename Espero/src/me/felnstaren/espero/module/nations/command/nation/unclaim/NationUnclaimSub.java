package me.felnstaren.espero.module.nations.command.nation.unclaim;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.command.nation.claim.NationClaimArg;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationUnclaimSub extends SubCommand {

	public NationUnclaimSub() {
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
			Messenger.send(player, "#F55This chunk is not claimed!");
			return true;
		}
		
		if(!claim.getNation().getID().equals(nation.getID())) {
			Messenger.send(player, "#F55This chunk does not belong to your nation!");
			return true;
		}
		
		if(claim.town != 0) nation.addTownArea(-1);
		NationClaimArg.updateNationArea(cx, cz, -1, nation);
		
		ClaimBoard.inst().unclaim(cx, cz);
		nation.broadcast("#5F5" + player.getDisplayName() + " #5F5unclaimed chunk at (" + cx + "," + cz + ")");
		
		return true;
	}
	
}
