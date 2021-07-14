package me.felnstaren.espero.module.nations.command.group.infos;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class GroupMembersCommand extends SubCommand {
	
	public GroupMembersCommand() {
		super("members");
		arguments.add(new SubArgument("<group>") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Group group = GroupRegistry.inst().getGroup(args[current]);
				
				if(group == null) {
					Messenger.send(sender, Color.RED + "Invalid Group: " + args[current]);
					return true;
				}
				
				Messenger.send(sender, message(group));
				
				return true;
			}
		});
	}
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send(sender, Color.RED + "Usage: /group members <group>");
		return true;
	}
	
	
	
	public static String message(Group group) {
		return group.neatHeader() 
				+ Format.LABEL_ARG.message("Members", group.getMembers().size() + "") + "\n"
				+ constructMembersList(group) + "\n";
	}
	
	public static String constructMembersList(Group group) {
		ArrayList<EsperoPlayer> members = group.getMembers(); int length = members.size();
		String[] nmembers = new String[length];
		int[] ranks = new int[length];
		
		//Organise group members from highest to lowest rank
		for(EsperoPlayer member : members) {
			int rank = group.relRank(member);
			for(int i = 0; i < length; i++) {
				if(ranks[i] == -1) {
					ranks[i] = rank; nmembers[i] = member.getName();
					break;
				} else if(i == length - 1) {
					ranks[i] = rank; nmembers[i] = member.getName();
					break;
				} else if(ranks[i] <= rank && ranks[i+1] > rank) {
					ranks[i] = rank; nmembers[i] = member.getName();
					break;
				}
			}
		}
		
		String message = Color.TURQUOISE.toString();
		for(int i = 0; i < length; i++)
			message += nmembers[i] + ":" + ranks[i];
		return message;
	}

}
