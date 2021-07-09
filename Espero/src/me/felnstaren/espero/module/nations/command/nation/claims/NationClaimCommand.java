package me.felnstaren.espero.module.nations.command.nation.claims;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubCommand;

public class NationClaimCommand extends SubCommand {

	public NationClaimCommand() {
		super("claim");
	}
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(nation == null) {
			Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
			return true;
		}
		
		if(!eplayer.getNation().hasPermission(eplayer, Permission.CLAIM)) {
			Messenger.send(player, Format.ERROR_NATION_PERMISSION.message());
			return true;
		}
		
		Chunk loc = player.getLocation().getChunk();
		int cx = loc.getX(); int cz = loc.getZ();
		ClaimChunk claim = ClaimBoard.inst().getClaim(cx, cz);
		
		if(claim != null) {
			Messenger.send(player, Format.ERROR_CHUNK_ALREADY_CLAIMED.message());
			return true;
		}
		
		if(nation.getArea() == 0 || ClaimBoard.inst().isAdjacent(cx, cz, nation.getID())) {
			if(nation.getBalance() < Option.NATION_CLAIM_PURCHASE_COST + Option.MIN_COFFERS_BALANCE && nation.getArea() > 5) {
				Messenger.send(player, Color.RED + "Your nation cannot afford this!");
				return true;
			}
				
			nation.claim(cx, cz);
			nation.broadcast(Color.GREEN + player.getDisplayName() + Color.GREEN + " claimed chunk at (" + loc.getX() + "," + loc.getZ() + ") for nation");
			return true;
		} else {
			Messenger.send(player, Color.RED + "Nation claims cannot be disconnected from eachother!");
			return true;
		}
	}

}
