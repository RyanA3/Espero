package me.felnstaren.espero.module.nations.command.group.infos;

import org.bukkit.command.CommandSender;

import me.felnstaren.felib.command.SubCommand;

public class GroupHelpCommand extends SubCommand {

	public GroupHelpCommand() {
		super("help");
		
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		
		return true;
	}

}
