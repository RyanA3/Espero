package me.felnstaren.espero.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.magic.rift.Rift;
import me.felnstaren.espero.module.magic.rift.RiftManager;
import me.felnstaren.espero.module.magic.rift.RiftPortal;
import me.felnstaren.espero.module.magic.rift.TemporaryRift;
import me.felnstaren.espero.util.message.Messenger;

public class RiftCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Messenger.color("&cOnly players can use this command!"));
			return true;
		}
		
		if(args.length == 0) {
			Rift.cast((Player) sender, 60);
			Messenger.send((Player) sender, "#44FF44You casted a rift");
			return true;
		}
		
		if(args.length < 8) {
			Messenger.send((Player) sender, "#FF4444Usage: /rift <world1> <x1> <y1> <z1> <world2> <x2> <y2> <z2> (time)"); 
			return true;
		}
		
		try {
			RiftPortal a = new RiftPortal(new Location(Bukkit.getWorld(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])));
			RiftPortal b = new RiftPortal(new Location(Bukkit.getWorld(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7])));
		
			if(args.length > 8)
				RiftManager.getInstance().register(new TemporaryRift(a, b, Integer.parseInt(args[8])));
			else
				RiftManager.getInstance().register(new Rift(a, b));
		} catch (Exception e) {
			Messenger.send((Player) sender, "#FF4444Usage: /rift <world1> <x1> <y1> <z1> <world2> <x2> <y2> <z2> (time)"); 
			return true;
		}
		
		Messenger.send((Player) sender, "#44FF44Successfully created a rift from " + args[0] + "(" + args[1] + "," + args[2] + "," + args[3] + ") to " + args[4] + "(" + args[5] + "," + args[6] + "," + args[7] + ")");
		return true;
	}

	
	
}
