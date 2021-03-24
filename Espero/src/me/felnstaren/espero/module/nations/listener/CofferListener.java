package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.menu.coffer.CofferMenu;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.ui.menu.MenuSessionHandler;

public class CofferListener implements Listener {
	
	@EventHandler
	public void onEnderChest(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if(block == null) return;
		if(block.getType() != Material.ENDER_CHEST) return;
		event.setCancelled(true);
	    
		ClaimChunk claim = ClaimBoard.getInstance().getClaim(block.getChunk().getX(), block.getChunk().getZ());
		EsperoPlayer player = Espero.PLAYERS.getPlayer(event.getPlayer());

		if(claim == null || !player.getNation().getID().equals(claim.getNation().getID())) {
			Messenger.send(event.getPlayer(), "&cYou can only access your nation's coffers in your nation .");
			return;
		}

		CofferMenu menu = new CofferMenu(claim.getNation());
		menu.open(event.getPlayer());
		MenuSessionHandler.inst().startSession(event.getPlayer(), menu);
	}
	
}
