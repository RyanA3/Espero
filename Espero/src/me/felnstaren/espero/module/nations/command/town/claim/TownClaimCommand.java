package me.felnstaren.espero.module.nations.command.town.claim;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
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
				Town town = TownRegistry.inst().getTown(args[current]);
				
				if(town == null) {
					Messenger.send(player, Color.RED + "Town not found: " + args[current]);
					return true;
				}
				
				Chunk loc = player.getLocation().getChunk();
				int cx = loc.getX(); int cz = loc.getZ();
				ClaimChunk claim = ClaimBoard.inst().getClaim(cx, cz);
				
				if(!town.hasPermission(eplayer, Permission.CLAIM)) {
					Messenger.send(player, Format.ERROR_NATION_PERMISSION.message());
					return true;
				}

				if(claim != null && !claim.owner.equals(town.getID())) {
					Messenger.send(player, Format.ERROR_CHUNK_ALREADY_CLAIMED.message());
					return true;
				}
				
				if(ClaimBoard.inst().isAdjacent(cx, cz, town.getID())) {
					town.claim(cx, cz);
					town.broadcast(Color.GREEN + player.getDisplayName() + Color.GREEN + " claimed " + town.getDisplayName() + " at " + cx + "x, " + cz + "z");
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
