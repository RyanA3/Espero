package me.felnstaren.espero.command;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.felnstaren.espero.util.message.Messenger;

public abstract class MasterCommand extends CommandContinuator implements CommandExecutor, TabCompleter {
	
	protected String permission;
	
	protected MasterCommand(CommandStub stub, String label, String permission) {
		super(stub, label);
		this.permission = permission;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return handle(sender, args, -1);
	}
	
	public ArrayList<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return tab(sender, args, -1);
	}


	
	public boolean handle(CommandSender sender, String[] args, int current) {
		if(!sender.hasPermission(this.permission) && !sender.isOp()) {
			Messenger.send((Player) sender, "#F55You do #F00not #F55have permission to #FAA" + permission);
			return true;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Messenger.color("&cYou must be a player to use this command!"));
			return true;
		}
		
		if(!forward(sender, args, current)) 
			sender.sendMessage(Messenger.color("&cAn error occured performing this command"));

		return true;
	}

	public ArrayList<String> tab(CommandSender sender, String[] args, int current) {
		ArrayList<String> tabs = forwardTab(sender, args, current);
		if(tabs.contains("<player>")) {
			ArrayList<String> remove = new ArrayList<String>();
			for(String str : tabs) if(str.equals("<player>")) remove.add(str);
			tabs.removeAll(remove);
			
			for(Player player : Bukkit.getOnlinePlayers()) tabs.add(player.getName());
		}
		
		ArrayList<String> done = new ArrayList<String>();
		StringUtil.copyPartialMatches(args[args.length - 1], tabs, done);
		return done;
	}
	
}
