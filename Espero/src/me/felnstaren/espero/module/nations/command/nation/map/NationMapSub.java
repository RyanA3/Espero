package me.felnstaren.espero.module.nations.command.nation.map;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationMapSub extends SubCommand {

	public NationMapSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				Chunk location = player.getLocation().getChunk();
				player.sendMessage(ClaimBoard.getInstance().getRegion(location.getX(), location.getZ()).map(location.getX(), location.getZ()));
				return true;
			}
		}, "map");
	}

}