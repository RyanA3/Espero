package me.felnstaren.espero.module.nations.menu.coffer;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.felib.item.util.ItemBuild;
import me.felnstaren.felib.ui.menu.ItemSchematic;
import me.felnstaren.felib.ui.menu.Menu;
import me.felnstaren.felib.ui.menu.MenuButton;
import me.felnstaren.felib.ui.menu.MenuSchematic;
import me.felnstaren.felib.ui.menu.MenuSession;
import other.bananapuncher714.nbt.NBTEditor;

public abstract class AbstractCofferMenu extends Menu {

	public static final int MAX_COFFER_BAL = 5000;
	private static final CofferMenuWithdrawButton WITHDRAW_BUTTON = new CofferMenuWithdrawButton(-1);
	private static final CofferMenuWithdrawButton WITHDRAW_100_BUTTON = new CofferMenuWithdrawButton(100);
	private static final CofferMenuWithdrawButton WITHDRAW_10_BUTTON = new CofferMenuWithdrawButton(10);
	private static final CofferMenuWithdrawButton WITHDRAW_1_BUTTON = new CofferMenuWithdrawButton(1);
	private static final CofferMenuDepositButton DEPOSIT_BUTTON = new CofferMenuDepositButton(-1);
	private static final CofferMenuDepositButton DEPOSIT_100_BUTTON = new CofferMenuDepositButton(100);
	private static final CofferMenuDepositButton DEPOSIT_10_BUTTON = new CofferMenuDepositButton(10);
	private static final CofferMenuDepositButton DEPOSIT_1_BUTTON = new CofferMenuDepositButton(1);
	private static final HashMap<String, MenuButton> BUTTON_MAP = new HashMap<String, MenuButton>();
	static {
		BUTTON_MAP.put("coffers_withdraw_custom", WITHDRAW_BUTTON);
		BUTTON_MAP.put("coffers_withdraw_100", WITHDRAW_100_BUTTON);
		BUTTON_MAP.put("coffers_withdraw_10", WITHDRAW_10_BUTTON);
		BUTTON_MAP.put("coffers_withdraw_1", WITHDRAW_1_BUTTON);
		BUTTON_MAP.put("coffers_deposit_custom", DEPOSIT_BUTTON);
		BUTTON_MAP.put("coffers_deposit_100", DEPOSIT_100_BUTTON);
		BUTTON_MAP.put("coffers_deposit_10", DEPOSIT_10_BUTTON);
		BUTTON_MAP.put("coffers_deposit_1", DEPOSIT_1_BUTTON);
	}
	
	
	public static final MenuSchematic SCHEMATIC = new MenuSchematic(27, "Coffers",
			new ItemSchematic(new ItemBuild(Material.RED_STAINED_GLASS_PANE, 1).setName("&cWithdraw").setLore("&e&oCustom Value...", 0).setButton("coffers_withdraw_custom").construct(), 1, 2),
			new ItemSchematic(new ItemBuild(Material.PINK_STAINED_GLASS_PANE, 1).setName("&cWithdraw").setLore("&e&o100", 0).setButton("coffers_withdraw_100").construct(), 2, 2),
			new ItemSchematic(new ItemBuild(Material.PINK_STAINED_GLASS_PANE, 1).setName("&cWithdraw").setLore("&e&o10", 0).setButton("coffers_withdraw_10").construct(), 3, 2),
			new ItemSchematic(new ItemBuild(Material.PINK_STAINED_GLASS_PANE, 1).setName("&cWithdraw").setLore("&e&o1", 0).setButton("coffers_withdraw_1").construct(), 4, 2),
			new ItemSchematic(new ItemBuild(Material.GREEN_STAINED_GLASS_PANE, 1).setName("&aDeposit").setLore("&e&oCustom Value...", 0).setButton("coffers_deposit_custom").construct(), 9, 2),
			new ItemSchematic(new ItemBuild(Material.LIME_STAINED_GLASS_PANE, 1).setName("&aDeposit").setLore("&e&o100", 0).setButton("coffers_deposit_100").construct(), 8, 2),
			new ItemSchematic(new ItemBuild(Material.LIME_STAINED_GLASS_PANE, 1).setName("&aDeposit").setLore("&e&o10", 0).setButton("coffers_deposit_10").construct(), 7, 2),
			new ItemSchematic(new ItemBuild(Material.LIME_STAINED_GLASS_PANE, 1).setName("&aDeposit").setLore("&e&o1", 0).setButton("coffers_deposit_1").construct(), 6, 2),
			new ItemSchematic(new ItemBuild(Material.WRITABLE_BOOK, 1).setName("&f&oNation Coffers").setLore("&7&oYou can deposit and withdraw money from", "&7&oyour nation's central coffers here.", "&7&oThe nation will be dissolved if it doesn't", "&7&ohave sufficient funding in its coffers!").construct(), 5, 2),
			new ItemSchematic(new ItemBuild(Material.REDSTONE_TORCH, 1).setName("&f&oMinimum Balance").setLore("&7&oYour nation's minimum balance is " + Option.MIN_COFFERS_BALANCE + ".", "&7&oYour transaction options are severely limited when", "&7&oyour nation's coffers are below this.").construct(), 5, 1));
	
	
	
	public AbstractCofferMenu() {
		super(SCHEMATIC);
		update();
	}
	
	
	
	@Override
	public void click(int slot, MenuSession session) {
		ItemStack clicked = inventory.getItem(slot);
		if(clicked == null) return;
		
		if(!NBTEditor.contains(clicked, "button")) return;
		String label = NBTEditor.getString(clicked, "button");
		
		MenuButton button = BUTTON_MAP.get(label);
		if(button != null) button.execute(session, clicked);
	}
	
	public void update() {
		String balname = "&6" + getBalance() + "/" + MAX_COFFER_BAL;
		inventory.setItem(1, new ItemBuild(Material.GREEN_TERRACOTTA, 1).setName(balname).construct());
		inventory.setItem(7, new ItemBuild(Material.EMERALD_BLOCK, 1).setName(balname).construct());
		for(int i = 2; i < 7; i++) {
			if(getBalance() >= ((MAX_COFFER_BAL / 5) * (i-1))) 
				inventory.setItem(i, new ItemBuild(Material.GREEN_STAINED_GLASS_PANE).setName(balname).construct());
			else if((getBalance() - ((MAX_COFFER_BAL / 5) * (i-2))) > 0) 
				inventory.setItem(i, new ItemBuild(Material.LIME_STAINED_GLASS_PANE).setName(balname).construct());
			else 
				inventory.setItem(i, new ItemBuild(Material.GRAY_STAINED_GLASS_PANE).setName(balname).construct());
		}
	}
	
	
	
	public abstract int getBalance();
	public abstract void setBalance(int value);
	public abstract void addBalance(int value);
	public abstract void notifyGroup(String message);
	public abstract boolean playerHasPermission(EsperoPlayer player, Permission permission);
	
}
