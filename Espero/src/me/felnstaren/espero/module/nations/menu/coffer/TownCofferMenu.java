package me.felnstaren.espero.module.nations.menu.coffer;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.town.Town;

public class TownCofferMenu extends AbstractCofferMenu {
	
	private Town town;
	
	public TownCofferMenu(Town town) {
		this.town = town;
	}

	public int getBalance()			  { return town.getBalance(); }
	public void setBalance(int value) { town.setBalance(value);   }
	public void addBalance(int value) { town.addBalance(value);   }
	public void notifyGroup(String message) { town.broadcast(message); }
	public boolean playerHasPermission(EsperoPlayer player, Permission permission) { return town.hasPermission(player, permission); }

}
