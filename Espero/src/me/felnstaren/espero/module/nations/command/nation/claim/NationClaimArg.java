package me.felnstaren.espero.module.nations.command.nation.claim;

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
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;

public class NationClaimArg extends SubArgument {

	public NationClaimArg() {
		super("<claimtype>");
	}
	
	//wow might need to rewrite this
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
		
		if(nation.getArea() == 0 || isTouching(cx, cz, nation.getID())) {
			if(nation.getBalance() < Option.CLAIM_PURCHASE_COST + Option.MIN_COFFERS_BALANCE && nation.getArea() > 5) {
				Messenger.send(player, Color.RED + "Your nation cannot afford this!");
				return true;
			}
				
			nation.addBalance(-Option.CLAIM_PURCHASE_COST);
			ClaimBoard.inst().claim(cx, cz, nation.getID());
			nation.broadcast(Color.GREEN + player.getDisplayName() + Color.GREEN + " claimed chunk at (" + loc.getX() + "," + loc.getZ() + ") for nation");
			updateNationArea(cx, cz, 1, nation);
			return true;
		} else {
			Messenger.send(player, Color.RED + "Nation claims cannot be disconnected from eachother!");
			return true;
		}
	}
	
	
	
	/**
	 * Update Nation Perimeter and area based on surrounding 4 chunks
	 * @param cx
	 * @param cz
	 * @param claim_action -1 = unclaim, +1 = claim
	 * @param nation
	 */
	public static void updateNationArea(int cx, int cz, int claim_action, Nation nation) {
		ClaimChunk check = ClaimBoard.inst().getClaim(cx+1, cz);
		if(check != null && check.owner.equals(nation.getID())) nation.addPerimeter(-claim_action);
		else nation.addPerimeter(claim_action);
		check = ClaimBoard.inst().getClaim(cx-1, cz);
		if(check != null && check.owner.equals(nation.getID())) nation.addPerimeter(-claim_action);
		else nation.addPerimeter(claim_action);
		check = ClaimBoard.inst().getClaim(cx, cz+1);
		if(check != null && check.owner.equals(nation.getID())) nation.addPerimeter(-claim_action);
		else nation.addPerimeter(claim_action);
		check = ClaimBoard.inst().getClaim(cx, cz-1);
		if(check != null && check.owner.equals(nation.getID())) nation.addPerimeter(-claim_action);
		else nation.addPerimeter(claim_action);
		nation.addArea(claim_action);
	}
	
	/**
	 * Returns true if the chunk at cx, cz is adjacent to another chunk of the same owner
	 * @param cx
	 * @param cz
	 * @param owner
	 * @return
	 */
	public static boolean isTouching(int cx, int cz, UUID owner) {
		return ClaimBoard.inst().isOwnedBy(cx+1, cz, owner)
				|| ClaimBoard.inst().isOwnedBy(cx-1, cz, owner)
				|| ClaimBoard.inst().isOwnedBy(cx, cz+1, owner)
				|| ClaimBoard.inst().isOwnedBy(cx, cz-1, owner);
	}

}
