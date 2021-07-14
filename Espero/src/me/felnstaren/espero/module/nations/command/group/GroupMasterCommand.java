package me.felnstaren.espero.module.nations.command.group;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.command.group.infos.GroupListCommand;
import me.felnstaren.espero.module.nations.command.group.infos.GroupMembersCommand;
import me.felnstaren.espero.module.nations.command.group.infos.GroupRanksCommand;
import me.felnstaren.espero.module.nations.command.group.players.GroupRemoveCommand;
import me.felnstaren.espero.module.nations.command.group.players.GroupSetCommand;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.felib.command.MasterCommand;
import me.felnstaren.felib.command.TabSuggestor;

public class GroupMasterCommand extends MasterCommand {

	protected GroupMasterCommand(String label, String permission, TabSuggestor[] tab_suggestors) {
		super("group", "espero.group",
				new TabSuggestor("<player>") {
					public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
						ArrayList<String> players = new ArrayList<String>();
						for(Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());
						return players;
					}
			},
				new TabSuggestor("<group>")  {
					public ArrayList<String> getSuggestions(CommandSender sender, String[] args, int current) {
						return GroupRegistry.inst().getGroupsNames();
					}
			});
		
		commands.add(new GroupListCommand());
		commands.add(new GroupMembersCommand());
		commands.add(new GroupRanksCommand());
		commands.add(new GroupRemoveCommand());
		commands.add(new GroupSetCommand());
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		
		return true;
	}

}
