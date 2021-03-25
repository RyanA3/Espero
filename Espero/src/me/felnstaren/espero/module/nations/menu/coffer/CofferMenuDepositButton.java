package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.PlayerMessage;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.item.util.InventoryEditor;
import me.felnstaren.felib.ui.menu.MenuButton;
import me.felnstaren.felib.ui.menu.MenuSession;
import me.felnstaren.felib.ui.menu.MenuSessionHandler;
import me.felnstaren.felib.ui.prompt.PromptHandler;
import me.felnstaren.felib.util.math.Maths;

public class CofferMenuDepositButton implements MenuButton {
	
	int expected_value;
	
	public CofferMenuDepositButton(int expected_value) {
		this.expected_value = expected_value;
	}
	
	
	
	public void execute(MenuSession session, ItemStack clicked) {
		Player player = session.getPlayer();
		EsperoPlayer eplayer = Espero.PLAYERS.getPlayer(player);
		Nation nation = eplayer.getNation();
		
		if(expected_value == -1) {
			session.getMenu().close();
			MenuSessionHandler.inst().closeSession(session.getPlayer());
			
			PromptHandler.inst().register(new CofferMenuChatPrompt(session.getPlayer(), 20, "#5F5How much would you like to deposit?", nation) {
				public void callback(String response) {
					int amount = 0;
					int total_economy_items = InventoryEditor.countItems(player.getInventory(), new ItemStack(Material.EMERALD));
					try { amount = Math.abs(Integer.parseInt(response)); }
					catch (Exception e) { Messenger.send(player, PlayerMessage.ERROR_INVALID_ARGUMENT.message().replaceAll("%argument%", response)); }
					if(amount > total_economy_items) amount = total_economy_items;
					if(amount == 0) { Messenger.send(player, PlayerMessage.ERROR_TRANSACTION_CANCELLED.message()); this.expired = true; return; }
				
					InventoryEditor.remove(player.getInventory(), new ItemStack(Material.EMERALD), amount);
					nation.addBalance(amount);
					nation.broadcast("#FF4" + player.getName() + " deposited " + amount + "E into the nation's coffers!");
					this.expired = true;
				}
			});
		} else {
			int amount = expected_value;
			int total_economy_items = InventoryEditor.countItems(session.getPlayer().getInventory(), new ItemStack(Material.EMERALD));
			if(amount > total_economy_items) amount = total_economy_items;
			if(amount + nation.getBalance() > CofferMenu.MAX_COFFER_BAL) amount = (int) Maths.clamp(CofferMenu.MAX_COFFER_BAL - nation.getBalance(), 0, CofferMenu.MAX_COFFER_BAL);
			if(amount == 0) { Messenger.send(session.getPlayer(), PlayerMessage.ERROR_TRANSACTION_CANCELLED.message()); return; }
			
			InventoryEditor.remove(session.getPlayer().getInventory(), new ItemStack(Material.EMERALD), amount);
			nation.addBalance(amount);
			nation.broadcast("#FF4" + session.getPlayer().getName() + " deposited " + amount + "E into the nation's coffers!");	
			((CofferMenu) session.getMenu()).update();
		}
	}

}
