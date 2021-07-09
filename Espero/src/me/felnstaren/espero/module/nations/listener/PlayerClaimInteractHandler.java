package me.felnstaren.espero.module.nations.listener;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.claim.OwnerType;
import me.felnstaren.espero.module.nations.group.Permission;

public class PlayerClaimInteractHandler implements Listener {
	
	private static final ArrayList<Material> CONTAINERS = new ArrayList<Material>();
	static { CONTAINERS.add(Material.BARREL); CONTAINERS.add(Material.CHEST); CONTAINERS.add(Material.TRAPPED_CHEST); 
	CONTAINERS.add(Material.FURNACE); CONTAINERS.add(Material.BLAST_FURNACE); CONTAINERS.add(Material.SMOKER);
	CONTAINERS.add(Material.DROPPER); CONTAINERS.add(Material.DISPENSER); CONTAINERS.add(Material.BREWING_STAND);
	CONTAINERS.add(Material.SHULKER_BOX); }
	
	
	
	private boolean candoshit(Player player, Location location, Permission permission) {
		Chunk c = location.getChunk();
		ClaimChunk claim = ClaimBoard.inst().getClaim(c.getX(), c.getZ());
		if(claim == null) return true;
		
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //wow thats old
		if(claim.owner_type == OwnerType.TOWN)
			return claim.getTown().hasPermission(eplayer, permission);
		else 
			return claim.getNation().hasPermission(eplayer, permission);
	}
	
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(!candoshit(event.getPlayer(), event.getBlock().getLocation(), Permission.BUILD)) event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(!candoshit(event.getPlayer(), event.getBlock().getLocation(), Permission.BUILD)) event.setCancelled(true);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if(event.getClickedBlock() == null) return;
		
		Permission what = null;
		if(event.getClickedBlock().getType() == Material.LEVER) what = Permission.LEVER;
		else if(event.getClickedBlock().getType().toString().contains("BUTTON")) what = Permission.BUTTON;
		else if(event.getClickedBlock().getType().toString().contains("DOOR")) what = Permission.DOOR;
		else if(CONTAINERS.contains(event.getClickedBlock().getType())) what = Permission.CONTAINER;
		else if(event.getClickedBlock().getType().toString().contains("BOX")) what = Permission.CONTAINER;
		else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) what = Permission.BUILD;
		if(what == null) return;
		
		if(!candoshit(event.getPlayer(), event.getClickedBlock().getLocation(), what)) event.setCancelled(true);
	}
	
}
