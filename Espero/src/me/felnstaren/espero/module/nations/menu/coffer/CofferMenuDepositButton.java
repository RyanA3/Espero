package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.item.util.InventoryEditor;
import me.felnstaren.felib.ui.menu.MenuSession;
import me.felnstaren.felib.ui.menu.MenuSessionHandler;
import me.felnstaren.felib.ui.prompt.PromptHandler;
import me.felnstaren.felib.util.math.Maths;

public class CofferMenuDepositButton implements CofferMenuButton {
	
	int expected_value;
	
	public CofferMenuDepositButton(int expected_value) {
		this.expected_value = expected_value;
	}
	
	

	public void execute(MenuSession session, ItemStack clicked) {

	}
	
	public void execute(MenuSession session, ItemStack clicked, Nation nation) {
		if(expected_value == -1) {
			session.getMenu().close();
			MenuSessionHandler.inst().closeSession(session.getPlayer());
			
			PromptHandler.inst().register(new CofferMenuChatPrompt(session.getPlayer(), 20, "#4F4How much would you like to deposit?", nation) {
				public void callback(String response) {
					int amount = 0;
					int total_economy_items = InventoryEditor.countItems(player.getInventory(), new ItemStack(Material.EMERALD));
					try { amount = Math.abs(Integer.parseInt(response)); }
					catch (Exception e) { Messenger.send(player, "#FAAError - Expected Integer, Got #AAA" + response); }
					if(amount > total_economy_items) amount = total_economy_items;
					if(amount == 0) { Messenger.send(player, "#FAACancelled Transaction"); this.expired = true; return; }
				
					InventoryEditor.remove(player.getInventory(), new ItemStack(Material.EMERALD), amount);
					nation.addBalance(amount);
					nation.broadcast("#FF0" + player.getName() + " deposited " + amount + "E into the nation's coffers!");
					this.expired = true;
				}
			});
		} else {
			int amount = expected_value;
			int total_economy_items = InventoryEditor.countItems(session.getPlayer().getInventory(), new ItemStack(Material.EMERALD));
			if(amount > total_economy_items) amount = total_economy_items;
			if(amount + nation.getBalance() > CofferMenu.MAX_COFFER_BAL) amount = (int) Maths.clamp(CofferMenu.MAX_COFFER_BAL - nation.getBalance(), 0, CofferMenu.MAX_COFFER_BAL);
			if(amount == 0) { Messenger.send(session.getPlayer(), "#FAACancelled Transaction"); return; }
			
			InventoryEditor.remove(session.getPlayer().getInventory(), new ItemStack(Material.EMERALD), amount);
			nation.addBalance(amount);
			nation.broadcast("#FF0" + session.getPlayer().getName() + " deposited " + amount + "E into the nation's coffers!");	
			((CofferMenu) session.getMenu()).update();
		}
	}

}
