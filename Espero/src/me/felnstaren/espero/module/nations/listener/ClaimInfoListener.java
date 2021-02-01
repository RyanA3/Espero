package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.felnstaren.espero.module.nations.claim.ClaimChunkData;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.espero.module.nations.system.Board;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.felib.chat.Messenger;

public class ClaimInfoListener implements Listener {

	@EventHandler
	public void onChangeChunk(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		Chunk f = event.getFrom().getChunk();
		Chunk t = event.getTo().getChunk();
		if(f.equals(t)) return;
		
		ClaimChunkData from = Board.getInstance().getClaim(f.getX(), f.getZ());
		ClaimChunkData to = Board.getInstance().getClaim(t.getX(), t.getZ());
		
		if(from == null && to == null) return;
		if(from != null && to != null && from.id == to.id) return;

		String message = "";
		Nation nation = null;
		Town town = null;
		
		if(to != null) {
			message = "#2D2Entering ";
			nation = Nations.getInstance().getNation(to.nation);
			town = nation.getTown(to.id);
		}
		else if(from != null) {
			message = "#D22Leaving ";
			nation = Nations.getInstance().getNation(from.nation);
			town = nation.getTown(from.id);
		} 
		
		
		if(town != null) message = message + "#999the town of " + town.name;
		else message = message + "#999the common lands of " + nation.getDisplayName();

		Messenger.send(player, message);
	}
	
}
