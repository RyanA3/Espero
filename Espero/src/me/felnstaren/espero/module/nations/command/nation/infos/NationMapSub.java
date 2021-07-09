package me.felnstaren.espero.module.nations.command.nation.infos;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.felib.command.SubCommand;

public class NationMapSub extends SubCommand {

	public NationMapSub() {
		super("map");
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		Chunk location = player.getLocation().getChunk();
		player.sendMessage(ClaimBoard.inst().getRegion(location.getX(), location.getZ()).map(location.getX(), location.getZ()));
		return true;
	}

}
