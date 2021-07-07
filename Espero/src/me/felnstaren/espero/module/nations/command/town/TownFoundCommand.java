package me.felnstaren.espero.module.nations.command.town;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.claim.ClaimChunk;
import me.felnstaren.espero.module.nations.claim.OwnerType;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;
import me.felnstaren.felib.util.StringUtil;

public class TownFoundCommand extends SubCommand {

	public TownFoundCommand() {
		super("found");
		this.arguments.add(new SubArgument("<name>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				Nation nation = eplayer.getNation();
				String name = args[current];
				
				if(nation == null) {
					Messenger.send(player, Format.ERROR_NOT_IN_NATION.message());
					return true;
				}
				
				if(!nation.hasPermission(eplayer, Permission.TOWN_CREATE)) {
					Messenger.send(player, Format.ERROR_NATION_PERMISSION.message());
					return true;
				}
				
				if(name.length() > 16) {
					Messenger.send(player, Format.ERROR_TOO_LONG.message().replaceAll("%length%", "16"));
					return true;
				}
				
				if(!StringUtil.isAlphaNumeric(name)) {
					Messenger.send(player, Color.RED + "Town names must be alpha/numeric");
					return true;
				}
				
				if(TownRegistry.inst().getTown(name) != null) {
					Messenger.send(player, Color.RED + "A town with this name already exists!");
					return true;
				}
				
				Chunk loc = player.getLocation().getChunk();
				int cx = loc.getX(); int cz = loc.getZ();
				ClaimChunk claim = ClaimBoard.inst().getClaim(cx, cz);
				
				if(claim.owner_type == OwnerType.TOWN) {
					Messenger.send(player, Color.RED + "You cannot found a town inside another");
					return true;
				}
				
				if(nation.getBalance() < Option.TOWN_FOUND_COST + Option.MIN_COFFERS_BALANCE) {
					Messenger.send(player, Color.RED + "Your nation cannot afford this!");
					return true;
				}

				
				//Found that shit
				Messenger.broadcast(Color.GREEN + "The town of " + name + " has formed!");
				Town town = new Town(nation.getID(), name, cx, cz);
				town.addArea(1);
				nation.addTownArea(1);
				nation.addBalance(-Option.TOWN_FOUND_COST);
				TownRegistry.inst().register(town);
				ClaimBoard.inst().claim(cx, cz, town.getID());
				
				return true;
			}
		});
	}

	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, Color.RED + "Usage: /town found <name>");
		return true;
	}

}
