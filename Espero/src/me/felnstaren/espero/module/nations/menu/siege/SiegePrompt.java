package me.felnstaren.espero.module.nations.menu.siege;

import org.bukkit.entity.Player;

import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.siege.SiegeRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.ui.prompt.ChatOptionPrompt;

public class SiegePrompt extends ChatOptionPrompt {

	private Town attacker;
	private Town defender;
	
	public SiegePrompt(Player player, Town attacker, Town defender) {
		super(player, 20, 
				Color.LIGHT_GRAY + "Are you sure you want to declare a siege on " + defender.getDisplayName() + "?" 
						+ "\nIt will cost " + Option.SIEGE_START_COST + " Mynt"
						+ "\n#EEEEEE[#00AA00%option1#EEEEEE] #EEEEEE[#AA0000%option2#EEEEEE]", "Yes", "No");
		this.attacker = attacker;
		this.defender = defender;
	}

	@Override
	public void callback(String response) {
		if(response.toLowerCase().equals("yes")) {
			if(attacker.getBalance() < Option.MIN_COFFERS_BALANCE + Option.SIEGE_START_COST) {
				Messenger.send(player, Color.RED + attacker.getDisplayName() + " does not have enough money to start a siege!");
				return;
			}
			
			if(defender.isInSiege()) {
				Messenger.send(player, Color.RED + defender.getDisplayName() + " is already under siege!");
				return;
			}
			
			attacker.addBalance(-Option.SIEGE_START_COST);
			SiegeRegistry.inst().startSiege(attacker, defender);
			Messenger.broadcast(Color.WHEAT + "A Siege has been Declared! " + Format.LABEL_ARG.message(attacker.getDisplayName(), defender.getDisplayName()));
		} else {
			Messenger.send(player, Color.RED + "Cancelled siege declaration");
			return;
		}
		
		this.expired = true;
	}

}
