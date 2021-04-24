package me.felnstaren.espero.module.nations.command.town;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.command.nation.claim.NationClaimArg;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class TownClaimCommand extends SubCommand {

	public TownClaimCommand() {
		super("claim");
		this.arguments.add(new SubArgument("<town>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				Nation nation = eplayer.getNation();
				
				if(nation == null) {
					Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
					return true;
				}
				
				if(!eplayer.getNationRank().isPermitted("claim")) {
					Messenger.send(player, Format.ERROR_NATION_PERMISSION.message());
					return true;
				}
				
				Chunk loc = player.getLocation().getChunk();
				int cx = loc.getX(); int cz = loc.getZ();
				ClaimChunk claim = ClaimBoard.inst().getClaim(cx, cz);
				
				int town_id = 0;
				for(Town town : nation.getTowns()) {
					if(town.name.equals(args[current])) {
						town_id = town.getID(); break; } }
				
				if(town_id == 0) {
					Messenger.send(player, Color.RED + "Invalid Town");
					return true;
				}
				
				if(claim == null) {
					Messenger.send(player, Color.RED + "Towns may only be inside of nation claims!");
					return true;
				}
				
				if(!claim.nation.equals(nation.getID())) {
					Messenger.send(player, Format.ERROR_CHUNK_ALREADY_CLAIMED.message());
					return true;
				}
				
				if(NationClaimArg.isTouching(cx, cz, nation, town_id)) {
					ClaimBoard.inst().claim(cx, cz, nation.getID(), town_id);
					nation.getTown(town_id).addArea(1);
					nation.broadcast(Color.GREEN + player.getDisplayName() + Color.GREEN + " modified chunk at (" + cx + "," + cz + ") from nation to " + args[current]);
					nation.addTownArea(1);
				} else {
					Messenger.send(player, Color.RED + "Town claims cannot be disconnected from their town!");
					return true;
				}
				
				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, "Usage: /town claim <town>");
		return true;
	}

}
