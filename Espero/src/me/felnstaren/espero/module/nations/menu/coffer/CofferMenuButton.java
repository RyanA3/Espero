package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.inventory.ItemStack;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.ui.menu.MenuButton;
import me.felnstaren.felib.ui.menu.MenuSession;

public interface CofferMenuButton extends MenuButton {

	public void execute(MenuSession session, ItemStack clicked, Nation nation);
	
}
