package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.menu.coffer.CofferMenu;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.ui.menu.MenuSessionHandler;

public class CofferListener implements Listener {
	
	@EventHandler
	public void onEnderChest(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if(block == null) return;
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(block.getType() != Material.ENDER_CHEST) return;
		
		event.setCancelled(true);
		
		EsperoPlayer player = Espero.PLAYERS.getPlayer(event.getPlayer());
		Nation nation = player.getNation();
		if(nation == null) return;
		
		ClaimChunk claim = ClaimBoard.inst().getClaim(block.getChunk().getX(), block.getChunk().getZ());

		if(claim == null || !nation.getID().equals(claim.getNation().getID())) {
			Messenger.send(event.getPlayer(), Format.ERROR_COFFERS_OUT_NATION.message());
			return;
		}

		CofferMenu menu = new CofferMenu(claim.getNation());
		menu.open(event.getPlayer());
		MenuSessionHandler.inst().startSession(event.getPlayer(), menu);
	}
	
}
