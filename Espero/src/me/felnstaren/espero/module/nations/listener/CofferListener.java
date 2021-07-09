package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.fusesource.jansi.Ansi.Color;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.claim.OwnerType;
import me.felnstaren.espero.module.nations.menu.coffer.AbstractCofferMenu;
import me.felnstaren.espero.module.nations.menu.coffer.NationCofferMenu;
import me.felnstaren.espero.module.nations.menu.coffer.TownCofferMenu;
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
		ClaimChunk claim = ClaimBoard.inst().getClaim(block.getChunk().getX(), block.getChunk().getZ());

		if(claim == null) { Messenger.send(event.getPlayer(), Color.RED + "Coffers can only be accessed within a nation or town of yours"); return; }

		AbstractCofferMenu menu = null;
		if(claim.owner_type == OwnerType.TOWN) {
			if(claim.getTown().isMember(player)) menu = new TownCofferMenu(claim.getTown());
			else { Messenger.send(event.getPlayer(), Color.RED + "You cannot access this town's coffers!"); return; }
		} else if(claim.owner_type == OwnerType.NATION) {
			if(claim.getNation().isMember(player)) menu = new NationCofferMenu(claim.getNation());
			else { Messenger.send(event.getPlayer(), Color.RED + "You cannot access this nation's coffers!"); return; }
		} else return; 
		
		menu.open(event.getPlayer());
		MenuSessionHandler.inst().startSession(event.getPlayer(), menu);
	}
	
}
