package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.felib.ui.prompt.ChatPrompt;

public abstract class CofferMenuChatPrompt extends ChatPrompt {

	protected Nation nation;
	
	public CofferMenuChatPrompt(Player player, int time, String formattable_message, Nation nation) {
		super(player, time, formattable_message);
		this.nation = nation;
	}
	
}