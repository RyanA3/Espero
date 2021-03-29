package me.felnstaren.espero.module.clogger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;

public class CombatCommand implements CommandExecutor {

	private CombatTimeHandler chandler;
	
	public CombatCommand(CombatTimeHandler chandler) {
		this.chandler = chandler;
	}
	
	
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return true;
		Player player = (Player) sender;
		
		if(args.length == 0) {
			if(!chandler.isInCombat(player)) Messenger.send(player, Color.GREEN + "You are not in combat");
			else Messenger.send(player, Color.RED + "You are in combat for " + Color.LIGHT_GRAY + chandler.getCombatTime(player) + Color.RED + " seconds");
			return true;
		}
		
		if(!player.hasPermission("espero.combat.set_combat_time")) {
			Messenger.send(player, Color.RED + "You do not have permission to do this!");
			return true;
		}
		
		Player set = Bukkit.getPlayerExact(args[0]);
		int time = -1;
		try { time = Math.abs(Integer.parseInt(args[1])); }
		catch (Exception e) { Messenger.send(player, Format.ERROR_INVALID_ARGUMENT.message(args[1])); return true; }
		
		if(chandler.isInCombat(set)) chandler.set(set, time);
		else chandler.register(set, time);
		Messenger.send(player, Color.GREEN + "Set " + set.getName() + "'s combat time to " + time);
		return true;
	}

}
