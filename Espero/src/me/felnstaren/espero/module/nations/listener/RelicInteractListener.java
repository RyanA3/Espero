package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import other.bananapuncher714.nbt.NBTEditor;

public class RelicInteractListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		if(entity.getType() != EntityType.ENDER_CRYSTAL) return;
		
		Town town = TownRegistry.inst().getTown(entity.getCustomName());
		if(town == null) return;
		
		Messenger.send(event.getPlayer(), Color.AQUA + "This relic belongs to " + Color.BLUE + town.getDisplayName());
	}
	
	@EventHandler
	public void onPickup(EntityDamageByEntityEvent event) {
		if(event.getEntity().getType() != EntityType.ENDER_CRYSTAL) return;
		Entity crystal = event.getEntity();
		
		Town town = TownRegistry.inst().getTown(crystal.getCustomName());
		if(town == null) return;
		
		event.setCancelled(true);
		
		if(!(event.getDamager() instanceof Player)) return;
		Player player = (Player) event.getDamager();
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		
		if(town.getRelic().isHolding(player)) return;
		if(town.isInSiege()) {
			if(town.isMember(eplayer)) return;
			player.getInventory().addItem(town.generateRelicItem());
			town.getRelic().destroy();
		}
		else if(town.hasPermission(eplayer, Permission.RELIC)) {
			player.getInventory().addItem(town.generateRelicItem());
		}
	}
	
	@EventHandler
	public void onPlace(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(event.getBlockFace() != BlockFace.UP) return;
		if(item == null) return;
		if(item.getType() != Material.END_CRYSTAL) return;
		String tag = NBTEditor.getString(item, "town_relic");
		if(tag == null) return;
		event.setCancelled(true);
		
		player.getInventory().setItemInMainHand(null);
		
		Town town = TownRegistry.inst().getTown(tag);
		if(town == null) return;
		
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		if(!town.isMember(eplayer)) return;
		
		Block block = event.getClickedBlock();
		
		ClaimChunk claim = ClaimBoard.inst().getClaim(block.getChunk().getX(), block.getChunk().getZ());
		if(claim == null || !claim.owner.equals(town.getID())) {
			Messenger.send(player, Color.RED + "You can only place your relic in claimed chunks!");
			return;
		}
		
		Messenger.send(player, Color.AQUA + "Moved relic to " + Color.BLUE + block.getX() + ", " + (block.getY() + 1) + ", " + block.getZ());
		town.getRelic().destroy();
		town.getRelic().setLocation(block.getX() + 0.5, block.getY() + 1, block.getZ() + 0.5);
		town.getRelic().spawn();
	}
	
}
