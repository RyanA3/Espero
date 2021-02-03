package me.felnstaren.espero.module.nations.command.nation.claim;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.espero.module.nations.newclaimsystem.ClaimBoard;
import me.felnstaren.espero.module.nations.newclaimsystem.ClaimChunk;
import me.felnstaren.espero.module.nations.system.Board;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubArgument;

public class NationClaimArg extends SubArgument {

	public NationClaimArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, "#F55You must be in a nation to use this command!");
					return true;
				}
				
				if(!eplayer.getNationRank().isPermitted("claim")) {
					Messenger.send(player, "#F55You do not have permission to do this in your nation!");
					return true;
				}
				
				Chunk loc = player.getLocation().getChunk();
				ClaimChunk claim = ClaimBoard.getInstance().getClaim(loc.getX(), loc.getZ());
				
				int claim_type = 0;
				for(Town town : nation.getTowns()) {
					if(town.name.equals(args[current])) {
						claim_type = town.getID(); break; } }
				
				if(claim == null) {
					ClaimBoard.getInstance().claim(loc.getX(), loc.getZ(), nation.getID(), 0);
					nation.broadcast("#5F5" + player.getDisplayName() + " #5F5claimed chunk at (" + (loc.getX() * 16) + "," + (loc.getZ() * 16) + ") for nation");
					return true;
				}
				
				if(!claim.nation.equals(nation.getID())) {
					Messenger.send(player, "#F55This chunk has already been claimed!");
					return true;
				}
				
				if(claim.town == 0) {
					if(claim_type == 0) {
						Messenger.send(player, "#F55This chunk is already claimed for your nation!");
						return true;
					}
					
					ClaimBoard.getInstance().claim(loc.getX(), loc.getZ(), nation.getID(), claim_type);
					nation.broadcast("#5F5" + player.getDisplayName() + " #5F5modified chunk at (" + (loc.getX() * 16) + "," + (loc.getZ() * 16) + ") from nation to " + args[current]);
				} else {
					if(claim_type == 0) {
						ClaimBoard.getInstance().claim(loc.getX(), loc.getZ(), nation.getID(), claim_type);
						nation.broadcast("#5F5" + player.getDisplayName() + " #5F5modified chunk at (" + (loc.getX() * 16) + "," + (loc.getZ() * 16) + ") from town to nation");
						return true;
					}
					
					Messenger.send(player, "#F55This chunk is already claimed for a town, and should first be moved to a nation claim!");				
				}
				
				return true;
			}
		}, "<claimtype>");
	}

}
