package me.felnstaren.espero.command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

public interface Tabbable {
	
	public ArrayList<String> tab(CommandSender sender, String[] args, int current);
	
}
