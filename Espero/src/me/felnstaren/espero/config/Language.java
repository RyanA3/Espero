package me.felnstaren.espero.config;

import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.util.logger.Level;
import me.felnstaren.espero.util.logger.Logger;
import me.felnstaren.espero.util.message.Messenger;

public class Language {
	
	private static YamlConfiguration config;
	
	public static void load(YamlConfiguration loadof) {
		Logger.log(Level.DEBUG, "Loading language configuration settings");
		config = loadof;
	}
	
	
	
	public static String msg(String key) {
		try {
			return Messenger.color(config.getString(key));
		} catch(Exception e) {
			Logger.log(Level.WARNING, "Error loading language message '" + key + "', is your lang.yml up to date?");
			return Messenger.color("&ePlugin language configuration error whilst loading value '&7" + key + "&c', contact an administrator! This is purely a visual bug.");
		}
	}
	
	public static String msg(String key, ChatVar... vars) {
		String message = msg(key);
		
		for(ChatVar var : vars) 
			message = var.fill(message);
		
		return message;
	}

}
