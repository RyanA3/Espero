package me.felnstaren.espero.module.nations.infoout;

import me.felnstaren.felib.chat.Color;

public enum InfoMessage {

	CANT_COMBAT_NATION(Color.RED + "You cannot damage players in your own nation!"),
	CANT_COMBAT_ALLY(Color.RED + "You cannot damage players in allied nations!");
	
	
	private String message;
	
	private InfoMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
	
	public String message() {
		return message;
	}
	
}
