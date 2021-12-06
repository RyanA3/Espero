package me.felnstaren.espero.module.nations.menu.town;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.menu.coffer.TownCofferMenu;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.felib.item.util.ItemBuild;
import me.felnstaren.felib.ui.menu.ItemSchematic;
import me.felnstaren.felib.ui.menu.Menu;
import me.felnstaren.felib.ui.menu.MenuButton;
import me.felnstaren.felib.ui.menu.MenuSchematic;
import me.felnstaren.felib.ui.menu.MenuSession;
import other.bananapuncher714.nbt.NBTEditor;

public class TownMenu extends Menu {
	
	private static final MenuButton COFFERS_BUTTON = new MenuButton() {
		public void execute(MenuSession session, ItemStack clicked) {
			TownCofferMenu cfmenu = new TownCofferMenu(((TownMenu) session.getMenu()).getTown());
			session.swapMenus(cfmenu);
		}
	};
	private static final MenuButton GROUPS_BUTTON = new MenuButton() {
		public void execute(MenuSession session, ItemStack clicked) {
			//open groups editor menu
		}
	};
	private static final MenuButton MEMBERS_BUTTON = new MenuButton() {
		public void execute(MenuSession session, ItemStack clicked) {
			//open members editor menu
		}
	};
	private static final MenuButton SIEGE_BUTTON = new MenuButton() {
		public void execute(MenuSession session, ItemStack clicked) {
			//open siege menu
		}
	};
	private static final MenuButton INVITE_BUTTON = new MenuButton() {
		public void execute(MenuSession session, ItemStack clicked) {
			//open invite prompt
		}
	};
	
	private static final HashMap<String, MenuButton> BUTTON_MAP = new HashMap<String, MenuButton>();
	static {
		BUTTON_MAP.put("coffers_button", COFFERS_BUTTON);
		BUTTON_MAP.put("groups_button", GROUPS_BUTTON);
		BUTTON_MAP.put("members_button", MEMBERS_BUTTON);
		BUTTON_MAP.put("siege_button", SIEGE_BUTTON);
		BUTTON_MAP.put("invite_button", INVITE_BUTTON);
	}
	
	private static final MenuSchematic SCHEMATIC = new MenuSchematic(27, "Town Menu",
		new ItemSchematic(new ItemBuild(Material.EMERALD, 1).setName("&aCoffers").setButton("coffers_button").construct(), 1, 0),
		new ItemSchematic(new ItemBuild(Material.IRON_BLOCK, 1).setName("&7Permissions").setButton("groups_button").construct(), 2, 0),
		new ItemSchematic(new ItemBuild(Material.PLAYER_HEAD, 1).setName("&eMembers").setButton("members_button").construct(), 3, 0),
		new ItemSchematic(new ItemBuild(Material.GOLDEN_SWORD, 1).setName("&cSiege").setButton("siege_button").construct(), 4, 0),
		new ItemSchematic(new ItemBuild(Material.WHEAT, 1).setName("&bInvite").setButton("invite_button").construct(), 5, 0)
	);
	
	
	
	
	private Town town;
	private EsperoPlayer player;

	public TownMenu(Town town, EsperoPlayer player) {
		super(SCHEMATIC);
		this.town = town;
		setTitle(town.getDisplayName());
		
		if(!town.hasPermission(player, Permission.MODIFY_PERMISSIONS)) super.inventory.setItem(1, null);
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
	
	
	public Town getTown() {
		return town;
	}


}
