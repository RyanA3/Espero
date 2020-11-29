package me.felnstaren.espero.command.standalone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.util.message.Messenger;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("espero.alpha.test")) {
			Messenger.send((Player) sender, "#F44You do #F00not #F44have access to this command!");
			return true;
		}
		
		Messenger.send((Player) sender, "#7F7The test was #0F0successful#7F7!");
		
		return true;
	}
	
}
