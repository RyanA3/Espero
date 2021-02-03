package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.espero.module.nations.newclaimsystem.ClaimBoard;
import me.felnstaren.espero.module.nations.newclaimsystem.ClaimChunk;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.felib.chat.Messenger;

public class ClaimInfoListener implements Listener {

	@EventHandler
	public void onChangeChunk(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		Chunk f = event.getFrom().getChunk();
		Chunk t = event.getTo().getChunk();
		if(f.equals(t)) return;
		//player.sendMessage("\n" + ClaimBoard.getInstance().getRegion(t.getX(), t.getZ()).map(t.getX(), t.getZ()));
		
		ClaimChunk from = ClaimBoard.getInstance().getClaim(f.getX(), f.getZ());
		ClaimChunk to = ClaimBoard.getInstance().getClaim(t.getX(), t.getZ());
		
		if(from == null && to == null) return;
		if(from != null && to != null && from.town == to.town) return;

		String message = "";
		Nation nation = null;
		Town town = null;
		
		if(to != null) {
			message = "#2D2Entering ";
			nation = Nations.getInstance().getNation(to.nation);
			if(nation != null) town = nation.getTown(to.town);
		}
		else if(from != null) {
			message = "#D22Leaving ";
			nation = Nations.getInstance().getNation(from.nation);
			if(nation != null) town = nation.getTown(from.town);
		} 
		
		
		if(town != null) message = message + "#999the town of " + town.name;
		else if(nation != null) message = message + "#999the common lands of " + nation.getDisplayName();

		if(nation != null) Messenger.send(player, message);
	}
	
}
