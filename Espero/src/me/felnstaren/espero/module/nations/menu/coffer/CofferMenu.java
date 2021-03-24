package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.item.util.InventoryEditor;
import me.felnstaren.felib.item.util.ItemBuild;
import me.felnstaren.felib.item.util.ItemEditor;
import me.felnstaren.felib.ui.menu.ItemSchematic;
import me.felnstaren.felib.ui.menu.Menu;
import me.felnstaren.felib.ui.menu.MenuButton;
import me.felnstaren.felib.ui.menu.MenuSchematic;
import me.felnstaren.felib.ui.menu.MenuSession;
import me.felnstaren.felib.ui.menu.MenuSessionHandler;
import me.felnstaren.felib.ui.prompt.PromptHandler;
import other.bananapuncher714.nbt.NBTEditor;

public class CofferMenu extends Menu {

	private static final int MAX_COFFER_BAL = 1000;
	
	public static final MenuSchematic SCHEMATIC = new MenuSchematic(27, "Coffers",
			new ItemSchematic(new ItemBuild(Material.GOLD_BLOCK, 1).setName("&6%balance%").construct(), 7, 0),
			new ItemSchematic(new ItemBuild(Material.RED_STAINED_GLASS_PANE, 1).setName("Withdraw").setButton("coffers_withdraw").construct(), 1, 2),
			new ItemSchematic(new ItemBuild(Material.GREEN_STAINED_GLASS_PANE, 1).setName("Deposit").setButton("coffers_deposit").construct(), 9, 2));
	
	
	
	private Nation nation;
	private MenuButton withdraw_button;
	private MenuButton deposit_button;
	
	public CofferMenu(Nation nation) {
		super(SCHEMATIC);

		this.nation = nation;
		ItemEditor.setName(inventory.getItem(6), "&6" + nation.getBalance());
		for(int i = 2; i < 6; i++) {
			if(nation.getBalance() >= ((MAX_COFFER_BAL / 4) * (i-1))) inventory.setItem(i, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
			else inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
		}
		
		this.withdraw_button = new MenuButton() {
			public void execute(MenuSession session, ItemStack clicked) {
				PromptHandler.inst().register(new CofferMenuChatPrompt(session.getPlayer(), 20, "#4F4How much would you like to withdraw?", nation) {
					public void callback(String response) {
						int amount = 0;
						try { amount = Math.abs(Integer.parseInt(response)); }
						catch (Exception e) { player.sendMessage("Error - Expected Integer"); this.expired = true; return; }
						
						InventoryEditor.add(player.getInventory(), new ItemStack(Material.EMERALD), amount, true);
						nation.addBalance(-amount);
						nation.broadcast("#FF0" + player.getName() + " withdrew " + amount + "E from the nation's coffers!");
						this.expired = true;
					}
				});
				session.getMenu().close();
				MenuSessionHandler.inst().closeSession(session.getPlayer());
			};
				
		};
		
		
		this.deposit_button = new MenuButton() {
			public void execute(MenuSession session, ItemStack clicked) {
				PromptHandler.inst().register(new CofferMenuChatPrompt(session.getPlayer(), 20, "#4F4How much would you like to deposit?", nation) {
					public void callback(String response) {
						int amount = 0;
						try { amount = Math.abs(Integer.parseInt(response)); }
						catch (Exception e) { player.sendMessage("Error - Expected Integer"); this.expired = true; return; }
						
						InventoryEditor.remove(player.getInventory(), new ItemStack(Material.EMERALD), amount);
						nation.addBalance(amount);
						nation.broadcast("#FF0" + player.getName() + " deposited " + amount + "E into the nation's coffers!");
						this.expired = true;
					}
				});
				session.getMenu().close();
				MenuSessionHandler.inst().closeSession(session.getPlayer());
			}
		};
	}
	
	
	
	@Override
	public void click(int slot, MenuSession session) {
		ItemStack clicked = inventory.getItem(slot);
		Bukkit.broadcastMessage("Clicked " + clicked == null ? "AIR" : clicked.getType().name());
		
		if(!NBTEditor.contains(clicked, "button")) return;
		String label = NBTEditor.getString(clicked, "button");
		
		if(label.equals("coffers_withdraw")) withdraw_button.execute(session, clicked);
		else if(label.equals("coffers_deposit")) deposit_button.execute(session, clicked);
	}
	
}
