package me.felnstaren.espero.module.nations.command.nation.claim;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;

public class NationClaimArg extends SubArgument {

	public NationClaimArg() {
		super("<claimtype>");
	}
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
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
		
		int claim_type = 0;
		for(Town town : nation.getTowns()) {
			if(town.name.equals(args[current])) {
				claim_type = town.getID(); break; } }
		
		if(claim == null) {
			if(nation.getArea() == 0 || isTouching(cx, cz, nation, -1)) {
				ClaimBoard.inst().claim(cx, cz, nation.getID(), 0);
				nation.broadcast(Color.GREEN + player.getDisplayName() + Color.GREEN + " claimed chunk at (" + loc.getX() + "," + loc.getZ() + ") for nation");
				updateNationArea(cx, cz, 1, nation);
				return true;
			} else {
				Messenger.send(player, Color.RED + "Nation claims cannot be disconnected from eachother!");
				return true;
			}
		}
		
		if(!claim.nation.equals(nation.getID())) {
			Messenger.send(player, Format.ERROR_CHUNK_ALREADY_CLAIMED.message());
			return true;
		}
		
		if(claim.town == 0) {
			if(claim_type == 0) {
				Messenger.send(player, Format.ERROR_CHUNK_ALREADY_CLAIMED.message());
				return true;
			}
			
			if(isTouching(cx, cz, nation, claim_type)) {
				ClaimBoard.inst().claim(cx, cz, nation.getID(), claim_type);
				nation.getTown(claim_type).addArea(1);
				nation.broadcast(Color.GREEN + player.getDisplayName() + Color.GREEN + " modified chunk at (" + cx + "," + cz + ") from nation to " + args[current]);
				nation.addTownArea(1);
			} else {
				Messenger.send(player, Color.RED + "Town claims cannot be disconnected from their town!");
				return true;
			}
		} else {
			if(claim_type == 0) {
					ClaimBoard.inst().claim(cx, cz, nation.getID(), claim_type);
					nation.broadcast(Color.GREEN + player.getDisplayName() + Color.GREEN + " modified chunk at (" + cx + "," + cz + ") from town to nation");
					nation.addTownArea(-1);
					return true;
			}
			
			Messenger.send(player, Format.ERROR_CHUNK_ALREADY_CLAIMED.message());				
		}
		
		return true;
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
		if(check != null && check.nation.equals(nation.getID())) nation.addPerimeter(-claim_action);
		else nation.addPerimeter(claim_action);
		check = ClaimBoard.inst().getClaim(cx-1, cz);
		if(check != null && check.nation.equals(nation.getID())) nation.addPerimeter(-claim_action);
		else nation.addPerimeter(claim_action);
		check = ClaimBoard.inst().getClaim(cx, cz+1);
		if(check != null && check.nation.equals(nation.getID())) nation.addPerimeter(-claim_action);
		else nation.addPerimeter(claim_action);
		check = ClaimBoard.inst().getClaim(cx, cz-1);
		if(check != null && check.nation.equals(nation.getID())) nation.addPerimeter(-claim_action);
		else nation.addPerimeter(claim_action);
		nation.addArea(claim_action);
	}
	
	public static boolean isTouching(int cx, int cz, Nation nation, int town) {
		return ClaimBoard.inst().isTown(cx+1, cz, nation, town)
				|| ClaimBoard.inst().isTown(cx-1, cz, nation, town)
				|| ClaimBoard.inst().isTown(cx, cz+1, nation, town)
				|| ClaimBoard.inst().isTown(cx, cz-1, nation, town);
	}

}
