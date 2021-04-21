package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.nation.Town;
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
		if(from != null && to != null && from.town == to.town && from.nation.equals(to.nation)) return;

		String message = "";
		Nation nation = null;
		Town town = null;
		
		if(to != null) {
			message = Color.GREEN + "Entering ";
			nation = NationRegistry.inst().getNation(to.nation);
			if(nation != null) town = nation.getTown(to.town);
		}
		else if(from != null) {
			message = Color.RED + "Leaving ";
			nation = NationRegistry.inst().getNation(from.nation);
			if(nation != null) town = nation.getTown(from.town);
		}
		
		
		if(town != null) message = message + Color.LIGHT_GRAY + "the town of " + town.name;
		else if(nation != null) message = message + Color.LIGHT_GRAY + "the common lands of " + nation.getDisplayName();

		if(nation != null) Messenger.send(player, message);
	}
	
}
