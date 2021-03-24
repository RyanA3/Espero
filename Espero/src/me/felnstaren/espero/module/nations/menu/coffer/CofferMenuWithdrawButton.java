package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.item.util.InventoryEditor;
import me.felnstaren.felib.ui.menu.MenuSession;
import me.felnstaren.felib.ui.menu.MenuSessionHandler;
import me.felnstaren.felib.ui.prompt.PromptHandler;

public class CofferMenuWithdrawButton implements CofferMenuButton {
	
	int expected_value;
	
	public CofferMenuWithdrawButton(int expected_value) {
		this.expected_value = expected_value;
	}

	
	
	public void execute(MenuSession session, ItemStack clicked) {

	}
	
	public void execute(MenuSession session, ItemStack clicked, Nation nation) {
		if(expected_value == -1) {
			session.getMenu().close();
			MenuSessionHandler.inst().closeSession(session.getPlayer());
			
			PromptHandler.inst().register(new CofferMenuChatPrompt(session.getPlayer(), 20, "#4F4How much would you like to withdraw?", nation) {
				public void callback(String response) {
					int amount = 0;
					try { amount = Math.abs(Integer.parseInt(response)); }
					catch (Exception e) { Messenger.send(player, "#FAAError - Expected Integer, Got #AAA" + response); }
					if(amount > nation.getBalance()) amount = nation.getBalance();
					if(amount == 0) { Messenger.send(player, "#FAACancelled Transaction"); this.expired = true; return; }
					
					InventoryEditor.add(player.getInventory(), new ItemStack(Material.EMERALD), amount, true);
					nation.addBalance(-amount);
					nation.broadcast("#FF0" + player.getName() + " withdrew " + amount + "E from the nation's coffers!");
					this.expired = true;
				}
			});
		} else {
			int amount = expected_value;
			if(amount > nation.getBalance()) amount = nation.getBalance();
			if(amount == 0) { Messenger.send(session.getPlayer(), "#FAACancelled Transaction"); return; }
			
			InventoryEditor.add(session.getPlayer().getInventory(), new ItemStack(Material.EMERALD), amount, true);
			nation.addBalance(-amount);
			nation.broadcast("#FF0" + session.getPlayer().getName() + " withdrew " + amount + "E from the nation's coffers!");
			((CofferMenu) session.getMenu()).update();
		}
	}

}
