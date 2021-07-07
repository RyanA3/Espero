package me.felnstaren.espero.module.nations.command.nation.unclaim;

import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.command.nation.claim.NationClaimArg;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Color;
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
		
		if(!nation.hasPermission(eplayer, Permission.UNCLAIM)) {
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
		
		if(!claim.owner.equals(nation.getID())) {
			Messenger.send(player, Color.RED + "This chunk does not belong to your nation!");
			return true;
		}
		

		if(isPivotal(cx, cz, nation.getID())) {
			Messenger.send(player, Color.RED + "Unclaiming this would leave two sections of your nation disconnected, "
					+ "nation claims must remain contiguous, please unclaim the other parts first");
			return true;
		}
		
		NationClaimArg.updateNationArea(cx, cz, -1, nation);
		ClaimBoard.inst().unclaim(cx, cz);
		nation.broadcast(Color.GREEN + player.getDisplayName() + Color.GREEN + " unclaimed chunk at (" + cx + "," + cz + ")");
		nation.addBalance(Option.CLAIM_SELL_COST);
		
		return true;
	}
	
	
	
	/**
	 * Oh god refactor somehow
	 * Returns true if unclaiming the specified chunk at cx and cz 
	 * will result in two sections of claims separating
	 * @param cx
	 * @param cz
	 * @param nation
	 * @param town
	 * @return
	 */
	public static boolean isPivotal(int cx, int cz, UUID owner) {
		ClaimChunk[][] chunks = new ClaimChunk[3][3];
		for(int offX = 0; offX < 3; offX++) {
			for(int offZ = 0; offZ < 3; offZ++) {
				chunks[offX][offZ] = ClaimBoard.inst().getClaim(cx + offX-1, cz + offZ-1);
			}
		}
		
		if(ClaimChunk.isOwnedBy( chunks[+1 +1][0  +1], owner)
		&& ClaimChunk.isOwnedBy( chunks[-1 +1][0  +1], owner)
		&& !ClaimChunk.isOwnedBy(chunks[0  +1][+1 +1], owner)
		&& !ClaimChunk.isOwnedBy(chunks[0  +1][-1 +1], owner)
		) return true;
		
		if(!ClaimChunk.isOwnedBy(chunks[+1 +1][0  +1], owner)
		&& !ClaimChunk.isOwnedBy(chunks[-1 +1][0  +1], owner)
		&& ClaimChunk.isOwnedBy( chunks[   +1][+1 +1], owner)
		&& ClaimChunk.isOwnedBy( chunks[0  +1][-1 +1], owner)
		) return true;
		
		
		if(ClaimChunk.isOwnedBy( chunks[+1 +1][0  +1], owner)
		&& ClaimChunk.isOwnedBy( chunks[0  +1][+1 +1], owner)
		&& !ClaimChunk.isOwnedBy(chunks[+1 +1][+1 +1], owner)
		) return true;
		
		if(ClaimChunk.isOwnedBy( chunks[-1 +1][0  +1], owner)
		&& ClaimChunk.isOwnedBy( chunks[0  +1][+1 +1], owner)
		&& !ClaimChunk.isOwnedBy(chunks[-1 +1][+1 +1], owner)
		) return true;
		
		if(ClaimChunk.isOwnedBy( chunks[+1 +1][0  +1], owner)
		&& ClaimChunk.isOwnedBy( chunks[0  +1][-1 +1], owner)
		&& !ClaimChunk.isOwnedBy(chunks[+1 +1][-1 +1], owner)
		) return true;
		
		if(ClaimChunk.isOwnedBy( chunks[-1 +1][0  +1], owner)
		&& ClaimChunk.isOwnedBy( chunks[0  +1][-1 +1], owner)
		&& !ClaimChunk.isOwnedBy(chunks[-1 +1][-1 +1], owner)
		) return true;
		
		return false;
	}
	
}
