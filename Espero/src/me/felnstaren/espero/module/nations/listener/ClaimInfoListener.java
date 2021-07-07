package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.claim.OwnerType;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;

public class ClaimInfoListener implements Listener {

	@EventHandler
	public void onChangeChunk(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		Chunk f = event.getFrom().getChunk();
		Chunk t = event.getTo().getChunk();
		if(f.equals(t)) return;
		//player.sendMessage("\n" + ClaimBoard.getInstance().getRegion(t.getX(), t.getZ()).map(t.getX(), t.getZ()));
		
		ClaimChunk from = ClaimBoard.inst().getClaim(f.getX(), f.getZ());
		ClaimChunk to = ClaimBoard.inst().getClaim(t.getX(), t.getZ());
		
		if(from == null && to == null) return;
		if(from != null && to != null && from.owner.equals(to.owner)) return;

		String message = "";
		Nation nation = null;
		Town town = null;
		
		if(to != null) {
			message = Color.GREEN + "Entering ";
			if(to.owner_type == OwnerType.NATION) nation = to.getNation();
			else town = to.getTown();
		}
		else if(from != null) {
			message = Color.RED + "Leaving ";
			if(from.owner_type == OwnerType.NATION) nation = from.getNation();
			else town = from.getTown();
		}
		
		
		if(town != null) message = message + Color.LIGHT_GRAY + "the town of " + town.name;
		else if(nation != null) message = message + Color.LIGHT_GRAY + "the common lands of " + nation.getDisplayName();

		if(nation != null) Messenger.send(player, message);
	}
	
}
