package me.felnstaren.espero.module.nations.listener;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;

public class PlayerClaimInteractHandler implements Listener {
	
	private static final ArrayList<Material> CONTAINERS = new ArrayList<Material>();
	static { CONTAINERS.add(Material.BARREL); CONTAINERS.add(Material.CHEST); CONTAINERS.add(Material.TRAPPED_CHEST); 
	CONTAINERS.add(Material.FURNACE); CONTAINERS.add(Material.BLAST_FURNACE); CONTAINERS.add(Material.SMOKER);
	CONTAINERS.add(Material.DROPPER); CONTAINERS.add(Material.DISPENSER); CONTAINERS.add(Material.BREWING_STAND);
	CONTAINERS.add(Material.SHULKER_BOX); }
	
	
	
	private boolean candoshit(Player player, Location location, String permission) {
		Chunk c = location.getChunk();
		ClaimChunk claim = ClaimBoard.getInstance().getClaim(c.getX(), c.getZ());
		if(claim == null) return true;
		
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
		Nation nation = claim.getNation();
		if(nation == null) return true;
		
		if(eplayer.getNation() == null || !eplayer.getNation().getID().equals(nation.getID())) {
			Messenger.send(player, "#F55You do not have permission to " + permission + " in " + nation.getDisplayName() + "!");
			return false;
		}
		
		if(!eplayer.hasPermission(permission, nation)) {
			Messenger.send(player, "#F55You do not have permission to " + permission + " in " + nation.getDisplayName() + "!");
			return false;
		}
		
		return true;
	}
	
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(!candoshit(event.getPlayer(), event.getBlock().getLocation(), "build")) event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(!candoshit(event.getPlayer(), event.getBlock().getLocation(), "build")) event.setCancelled(true);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if(event.getClickedBlock() == null) return;
		
		String what = null;
		if(event.getClickedBlock().getType() == Material.LEVER) what = "lever";
		else if(event.getClickedBlock().getType().toString().contains("BUTTON")) what = "button";
		else if(event.getClickedBlock().getType().toString().contains("DOOR")) what = "door";
		else if(CONTAINERS.contains(event.getClickedBlock().getType())) what = "container";
		else if(event.getClickedBlock().getType().toString().contains("BOX")) what = "container";
		if(what == null) return;
		
		if(!candoshit(event.getPlayer(), event.getClickedBlock().getLocation(), what)) event.setCancelled(true);
	}
	
}
