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
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;
import me.felnstaren.felib.ui.prompt.ChatPrompt;
import me.felnstaren.felib.ui.prompt.PromptHandler;
import me.felnstaren.felib.util.StringUtil;

public class TownFoundCommand extends SubCommand {

	public TownFoundCommand() {
		super("found");
		this.arguments.add(new SubArgument("<name>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
				Nation nation = eplayer.getNation();
				
				String name = "";
				for(int i = 1; i < args.length; i++) {
					name += args[i];
					if(i < args.length - 1) name += " ";
				}
				final String fname = name;
				
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
				
				if(claim != null) {
					Messenger.send(player, Color.RED + "You cannot found a town inside already claimed chunks!");
					return true;
				}
				
				if(nation.getBalance() < Option.TOWN_FOUND_COST + Option.MIN_COFFERS_BALANCE) {
					Messenger.send(player, Color.RED + "Your nation cannot afford this!");
					return true;
				}

				
				
				PromptHandler.inst().register(new ChatPrompt(player, 20, 
						Color.WHEAT + "Are you sure you would like to found the town of "
						+ Format.ARG.message(name) + Color.WHEAT + "?\n"
						+ "It will cost you " + Option.TOWN_FOUND_COST + " Mynt"
				) {
					public void callback(String response) {
						if(response.equalsIgnoreCase("no")) {
							this.expired = true;
							Messenger.send(player, Color.RED + "Cancelled town creation...");
							return;
						} else if(response.equalsIgnoreCase("yes")) {
							//Found that shit
							Messenger.broadcast(Color.GREEN + "The town of " + fname + " has formed!");
							Town town = new Town(nation.getID(), fname, cx, cz, eplayer);
							TownRegistry.inst().register(town);
							town.claim(cx, cz);
							nation.broadcast(Color.GREEN + player.getDisplayName() + " has founded the town of " + fname);
							this.expired = true;
						} else {
							Messenger.send(player, Color.RED + "I could not understand that, please respond with yes/no");
						}
					}
				});
				
				return true;
			}
		});
	}

	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, Color.RED + "Usage: /town found <name>");
		return true;
	}

}
