package me.felnstaren.espero.module.nations.command.group;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.group.Rank;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class GroupPermissionCommand extends SubCommand {

	public GroupPermissionCommand() {
		super("permission");
		arguments.add(new SubArgument("<group>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Messenger.send(sender, Color.RED + "Usage: /group permission <group> <rank> <permission>");
				return true;
			}
		});
		arguments.get(0).addArgument(new SubArgument("<rank_index>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Messenger.send(sender, Color.RED + "Usage: /group permission <group> <rank> <permission>");
				return true;
			}
		});
		arguments.get(0).getArgument(0).addArgument(new SubArgument("<permission>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Group group = GroupRegistry.inst().getGroup(args[1]);
				if(group == null) {
					Messenger.send(sender, Color.RED + "Invalid Group: " + args[1]);
					return true;
				}
				
				Rank r = group.getRank(args[2]);
				if(r == null) {
					try { 
						r = group.getRanks()[Integer.parseInt(args[2])]; 
					} catch (Exception e) {};
				}
				if(r == null) {
					Messenger.send(sender, Color.RED + "Invalid Rank: " + args[2]);
					String listnames = "";
					ArrayList<String> rnames = group.getModel().getRanksNames();
					for(String rname : rnames) listnames += rname + ", ";
					Messenger.send(sender, Color.RED + "Valid ranks for this group are " + listnames);
					return true;
				}
				
				Permission p = null;
				try { p = Permission.valueOf(args[3].toUpperCase()); } catch (Exception e) {};
				if(p == null) {
					Messenger.send(sender, Color.RED + "Invalid Permission: " + args[3]);
					String listperms = "";
					for(Permission pname : Permission.values()) listperms += pname.name() + ", ";
					Messenger.send(sender, Color.RED + "Valid permissions are " + listperms);
					return true;
				}
				
				if(r.isPermitted(p)) {
					r.remPermissions(p);
					Messenger.send(sender, Color.WHEAT + "Toggled " + Color.LIGHT_GRAY + p.name() + Color.WHEAT + " in group " + Color.LIGHT_GRAY + group.getName() + Color.WHEAT + " for subgroup " + Color.LIGHT_GRAY + r.gaming_name + Color.WHEAT + " to " + Color.RED + "DENY");
				} else {
					r.addPermissions(p);
					Messenger.send(sender, Color.WHEAT + "Toggled " + Color.LIGHT_GRAY + p.name() + Color.WHEAT + " in group " + Color.LIGHT_GRAY + group.getName() + Color.WHEAT + " for subgroup " + Color.LIGHT_GRAY + r.gaming_name + Color.WHEAT + " to " + Color.LIME + "ALLOW");
				}
				
				return true;
			}
		});
	}


	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /group permission <group> <rank> <permission>");
		return true;
	}

}
