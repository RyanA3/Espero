package me.felnstaren.espero.module.nations.menu.coffer;

import org.bukkit.entity.Player;

import me.felnstaren.felib.ui.prompt.ChatPrompt;

public abstract class CofferMenuChatPrompt extends ChatPrompt {

	protected AbstractCofferMenu menu;
	
	public CofferMenuChatPrompt(Player player, int time, String formattable_message, AbstractCofferMenu menu) {
		super(player, time, formattable_message);
		this.menu = menu;
	}
	
}