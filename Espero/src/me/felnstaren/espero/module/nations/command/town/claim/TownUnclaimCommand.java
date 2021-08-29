package me.felnstaren.espero.module.nations.command.town.claim;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.claim.OwnerType;
import me.felnstaren.espero.module.nations.command.nation.claims.NationUnclaimCommand;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRelic;
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
		
		Chunk loc = player.getLocation().getChunk();
		int cx = loc.getX(); int cz = loc.getZ();
		ClaimChunk claim = ClaimBoard.inst().getClaim(cx, cz);
		
		if(claim == null) {
			Messenger.send(player, Color.RED + "This chunk is not claimed!");
			return true;
		}
		
		if(claim.owner_type != OwnerType.TOWN) {
			Messenger.send(player, Color.RED + "This chunk is not owned by a town!");
			return true;
		}
		
		Town town = claim.getTown();
		
		if(!town.hasPermission(eplayer, Permission.UNCLAIM)) {
			Messenger.send(player, Color.RED + "You do not have permission to unclaim " + town.getDisplayName());
			return true;
		}
		
		if(town.getRelic().exists()) {
			TownRelic relic = town.getRelic();
			if(relic.getChunkX() == cx && relic.getChunkZ() == cz) {
				Messenger.send(player, Color.RED + "You cannot unclaim the chunk your relic is in!");
				return true;
			}
		}
		
		if(!NationUnclaimCommand.isPivotal(cx, cz, claim.owner)) {
			town.unclaim(cx, cz);
			town.broadcast(player.getName() + " unclaimed " + town.getDisplayName() + " at " + cx + "x, " + cz + "z");
		}
		else {
			Messenger.send(player, Color.RED + "Unclaiming this would leave two sections of your town disconnected, "
					+ "town claims must remain contiguous, please unclaim the other parts first");
			return true;
		}
		
		return true;
	}

}
