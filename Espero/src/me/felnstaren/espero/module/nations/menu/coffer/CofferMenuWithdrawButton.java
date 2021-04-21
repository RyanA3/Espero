package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.economy.Economy;
import me.felnstaren.espero.module.nations.Nations;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.ui.menu.MenuButton;
import me.felnstaren.felib.ui.menu.MenuSession;
import me.felnstaren.felib.ui.menu.MenuSessionHandler;
import me.felnstaren.felib.ui.prompt.PromptHandler;

public class CofferMenuWithdrawButton implements MenuButton {
	
	int expected_value;
	
	public CofferMenuWithdrawButton(int expected_value) {
		this.expected_value = expected_value;
	}


	
	public void execute(MenuSession session, ItemStack clicked) {
		Player player = session.getPlayer();
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		Nation nation = eplayer.getNation();
		if(!Nations.isPermitted(eplayer, nation, "coffers")) {
			Messenger.send(player, "#F77You do not have permission to withdraw from this nations coffers!");
			return;
		}
		
		if(expected_value == -1) {
			session.getMenu().close();
			MenuSessionHandler.inst().closeSession(player);
			
			PromptHandler.inst().register(new CofferMenuChatPrompt(player, 20, "#4F4How much would you like to withdraw?", nation) {
				public void callback(String response) {
					int amount = 0;
					try { amount = Math.abs(Integer.parseInt(response)); }
					catch (Exception e) { Messenger.send(player, Format.ERROR_INVALID_ARGUMENT.message().replaceAll("%argument%", response)); }
					if(amount + Option.MIN_COFFERS_BALANCE > nation.getBalance()) amount = nation.getBalance() - Option.MIN_COFFERS_BALANCE;
					if(amount <= 0) { Messenger.send(player, Format.ERROR_TRANSACTION_CANCELLED.message()); this.expired = true; return; }
					
					Economy.deposit(player, amount);
					nation.addBalance(-amount);
					nation.broadcast("#FF4" + player.getName() + " withdrew " + amount + "E from the nation's coffers!");
					this.expired = true;
				}
			});
		} else {
			int amount = expected_value;
			if(amount + Option.MIN_COFFERS_BALANCE > nation.getBalance()) amount = nation.getBalance() - Option.MIN_COFFERS_BALANCE;
			if(amount <= 0) { Messenger.send(session.getPlayer(), Format.ERROR_TRANSACTION_CANCELLED.message()); return; }
			
			Economy.deposit(player, amount);
			nation.addBalance(-amount);
			nation.broadcast("#FF4" + session.getPlayer().getName() + " withdrew " + amount + "E from the nation's coffers!");
			((CofferMenu) session.getMenu()).update();
		}
	}

}
