package me.felnstaren.espero.module.nations.command.nation.create;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Town;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.rilib.chat.Messenger;
import me.felnstaren.rilib.command.CommandStub;
import me.felnstaren.rilib.command.SubArgument;

public class NationCreateArg extends SubArgument {

	public NationCreateArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				EsperoPlayer eplayer = new EsperoPlayer(player);
				
				if(eplayer.getNation() != null) {
					Messenger.send(player, "#F55You have to leave your current nation to create your own!");
					return true;
				}
				
				if(Nations.getInstance().getNation(args[current]) != null) {
					Messenger.send(player, "#F55A nation with this name already exists!");
					return true;
				}
				
				if(args[current].length() > 16) {
					Messenger.send(player, "#F55You nation name may not be more than 16 characters long!");
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
