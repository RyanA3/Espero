package me.felnstaren.espero.module.nations.listener;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.claim.OwnerType;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.packet.enums.PacketTitleAction;

public class ClaimChangeListener implements Listener {

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
		if(from != null) {
			if(from.owner_type == OwnerType.TOWN) message += Color.LIGHT_BLUE;
			else message += Color.BLUE;
			message += from.getOwnerName();
		}
		else message += Color.GREEN + "Wilds";
		message += " " + Color.WHEAT + "" + Color.ARROW_RIGHT + " ";
		if(to != null) {
			if(to.owner_type == OwnerType.TOWN) message += Color.LIGHT_BLUE;
			else message += Color.BLUE;
			message += to.getOwnerName();
		}
		else message += Color.GREEN + "Wilds";

		Messenger.sendTitle(player, message, PacketTitleAction.ACTIONBAR, 10, 100, 10);
		
		//Siege relic check
		if(from == null) return;
		if(from.owner_type != OwnerType.TOWN) return;
		Town town = from.getTown();
		if(town == null) return;
		if(!town.isInSiege()) return;
		if(!town.getRelic().isHolding(player)) return;
		town.getSiege().captureRelic();
	}
	
}
