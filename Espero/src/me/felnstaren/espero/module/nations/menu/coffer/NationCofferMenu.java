package me.felnstaren.espero.module.nations.menu.coffer;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.chat.Color;
import net.md_5.bungee.api.ChatColor;

public class NationCofferMenu extends AbstractCofferMenu {
	
	private Nation nation;
	
	public NationCofferMenu(Nation nation) {
		this.nation = nation;
		setTitle(ChatColor.DARK_GRAY + nation.getDisplayName() + " " + Color.ARROW_RIGHT + " COFFERS");
		update();
	}

	public int getBalance()			  { return nation.getBalance(); }
	public void setBalance(int value) { nation.setBalance(value);   }
	public void addBalance(int value) { nation.addBalance(value);   }
	public void notifyGroup(String message) { nation.broadcast(message); }
	public boolean playerHasPermission(EsperoPlayer player, Permission permission) { return nation.hasPermission(player, permission); }

}
