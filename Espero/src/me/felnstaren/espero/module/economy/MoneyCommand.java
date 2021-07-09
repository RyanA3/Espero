package me.felnstaren.espero.module.economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;

public class MoneyCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return true;
		if(!sender.hasPermission("espero.money")) {
			Messenger.send((Player) sender, Color.RED + "You do not have permission to do this!");
			return true;
		}
		
		if(args.length < 1) {
			Messenger.send((Player) sender, Color.RED + "Specify an amount");
			return true;
		}
		
		Economy.deposit((Player) sender, Integer.parseInt(args[0]));
		return true;
	}
	
}
