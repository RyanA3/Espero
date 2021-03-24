package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.item.util.InventoryEditor;
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
		if(!eplayer.hasPermission("coffers", nation)) {
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
					catch (Exception e) { Messenger.send(player, "#F77Error - Expected Integer, Got #AAA" + response); }
					if(amount > nation.getBalance()) amount = nation.getBalance();
					if(amount == 0) { Messenger.send(player, "#F77Cancelled Transaction"); this.expired = true; return; }
					
					InventoryEditor.add(player.getInventory(), new ItemStack(Material.EMERALD), amount, true);
					nation.addBalance(-amount);
					nation.broadcast("#FF0" + player.getName() + " withdrew " + amount + "E from the nation's coffers!");
					this.expired = true;
				}
			});
		} else {
			int amount = expected_value;
			if(amount > nation.getBalance()) amount = nation.getBalance();
			if(amount == 0) { Messenger.send(session.getPlayer(), "#F77Cancelled Transaction"); return; }
			
			InventoryEditor.add(session.getPlayer().getInventory(), new ItemStack(Material.EMERALD), amount, true);
			nation.addBalance(-amount);
			nation.broadcast("#FF0" + session.getPlayer().getName() + " withdrew " + amount + "E from the nation's coffers!");
			((CofferMenu) session.getMenu()).update();
		}
	}

}
