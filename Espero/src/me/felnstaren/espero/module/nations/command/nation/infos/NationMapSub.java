package me.felnstaren.espero.module.nations.command.nation.infos;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.command.SubCommand;

public class NationMapSub extends SubCommand {

	public NationMapSub() {
		super("map");
	}
	
	
	
	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		Chunk location = player.getLocation().getChunk();
		float yaw = player.getLocation().getYaw();
		Color direction = Color.ARROW_UP;
		if(yaw > 0 && yaw < 45) direction = Color.ARROW_DOWN;
		if(yaw > 45 && yaw < 135) direction = Color.ARROW_LEFT;
		if(yaw > 135 && yaw < 225) direction = Color.ARROW_UP;
		if(yaw > 225 && yaw < 315) direction = Color.ARROW_RIGHT;
		if(yaw > 315) direction = Color.ARROW_DOWN;
		
		player.sendMessage(ClaimBoard.map(location.getX(), location.getZ(), direction));
		return true;
	}

}
