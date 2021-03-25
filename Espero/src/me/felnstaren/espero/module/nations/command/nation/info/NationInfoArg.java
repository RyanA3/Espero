package me.felnstaren.espero.module.nations.command.nation.info;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubArgument;

public class NationInfoArg extends SubArgument {

	public NationInfoArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Nation nation = Nations.inst().getNation(args[current]);
				if(nation == null) {
					Messenger.send((Player) sender, "#F77Invalid Nation: #777" + args[current]);
					return true;
				}
				
				Messenger.send((Player) sender, NationInfoSub.constructNationInfo(nation));
				return true;
			}
		}, "<nation>");
	}
	
}
