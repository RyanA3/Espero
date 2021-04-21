package me.felnstaren.espero.module.nations.command.nation.create;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.economy.Economy;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.ui.prompt.ChatPrompt;
import me.felnstaren.felib.ui.prompt.PromptHandler;

public class NationCreateArg extends SubArgument {

	public NationCreateArg() {
		super("<name>");
	}


	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		final EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player); //new EsperoPlayer(player);
		
		if(eplayer.getNation() != null) {
			Messenger.send(player, Format.ERROR_IN_NATION.message());
			return true;
		}
		
		String name = "";
		for(int i = 1; i < args.length && i < 4; i++) {
			if(i > 1) name += " ";
			name += args[i];
		}				
		final String fname = name;
		
		if(NationRegistry.inst().getNation(name) != null) {
			Messenger.send(player, Format.ERROR_NAME_TAKEN.message());
			return true;
		}
		
		if(args[current].length() > 16) {
			Messenger.send(player, Format.ERROR_TOO_LONG.message().replaceAll("%length%", "16"));
			return true;
		}
		
		
		
		PromptHandler.inst().register(new ChatPrompt(player, 20, 
				Color.WHEAT + "Are you sure you would like to found a nation called"
				+ Format.ARG.message(args[current]) + Color.WHEAT + "?\n"
				+ "It will cost you " + Option.NATION_FOUND_COST + " Mynt"
				) {
			public void callback(String response) {
				if(response.equalsIgnoreCase("no")) {
					this.expired = true;
					Messenger.send(player, Color.RED + "Cancelled nation creation...");
					return;
				} else if(response.equalsIgnoreCase("yes")) {
					this.expired = true;
					
					if(Economy.balance(player) < Option.NATION_FOUND_COST) {
						Messenger.send(player, Color.RED + "You do not have enough money to found a nation!");
						this.expired = true;
						return;
					}
					
					Economy.withdraw(player, Option.NATION_FOUND_COST);
					
					Nation nation = new Nation(fname, eplayer);
					NationRegistry.inst().registerNewNation(nation);
					
					Messenger.broadcast("#5F5The nation of #2F2" + nation.getDisplayName() + " #5F5has risen from the ashes!");
					Messenger.send(player, "#5F5Successfully create a new nation called #7F7" + fname);
				} else {
					Messenger.send(player, Color.RED + "I could not understand that, please respond with yes/no");
				}
			}
		});
		
		return true;
	}

}
