package me.felnstaren.espero.module.nations.command.nation.create.name;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.SubArgument;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.espero.util.message.Messenger;

public class NationCreateNameArgument extends SubArgument {

	public NationCreateNameArgument() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				
				if(!(sender instanceof Player)) {
					sender.sendMessage(Messenger.color("&cYou must be a player to use this command!"));
					return true;
				}
				
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				
				if(eplayer.getNation() != null) {
					Messenger.send(player, "#F55You have to leave your current nation to create your own!");
					return true;
				}
				
				
				Town capital = new Town("Capital", 0, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
				Nation nation = new Nation(args[current], capital, eplayer);
				Nations.getInstance().registerNewNation(nation);
				
				Messenger.broadcast("#5F5The nation of #2F2" + nation.getDisplayName() + " #5F5has risen from the ashes!");
				Messenger.send(player, "#5F5Successfully create a new nation called #7F7" + args[current]);
				return true;
			}
		}, "<name>");
	}

}
