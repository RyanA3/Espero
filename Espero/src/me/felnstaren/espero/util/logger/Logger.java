package me.felnstaren.espero.util.logger;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.felnstaren.espero.util.message.Messenger;

public class Logger {
	
	private static Logger LOGGER;
	
	public static void init(JavaPlugin plugin) {
		if(LOGGER != null) return;
		LOGGER = new Logger(plugin.getServer().getConsoleSender(), plugin.getName());
	}
	
	public static Logger getInstance() {
		return LOGGER;
	}
	
	public static void log(Level level, String message) {
		Logger.getInstance().loga(level, message);
	}
	
	

	private ConsoleCommandSender console;
	private String label;
	private Level priority = Level.DEBUG;
	
	private Logger(ConsoleCommandSender console, String label) {
		this.console = console;
		this.label = label;
	}
	
	public void loga(Level level, String message) {
		if(level.weight < priority.weight) return;
		
		String prefix = ChatColor.AQUA + label + "." + level.color + level.name();
		String divider = ChatColor.AQUA + " >> " + ChatColor.GRAY;
		
		console.sendMessage(prefix + divider + Messenger.color(message));
	}
	
	public void setPriority(Level priority) {
		this.priority = priority;
	}
	
}
