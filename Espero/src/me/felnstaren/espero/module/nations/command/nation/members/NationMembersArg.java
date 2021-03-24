package me.felnstaren.espero.module.nations.command.nation.members;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubArgument;

public class NationMembersArg extends SubArgument {

	public NationMembersArg() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Nation nation = Nations.getInstance().getNation(args[current]);
				if(nation == null) {
					Messenger.send((Player) sender, "#F77Invalid Nation: #777" + args[current]);
					return true;
				}
				
				Messenger.send((Player) sender, "#AAA" + NationMembersSub.constructMembersList(nation));
				return true;
			}
		}, "<nation>");
	}

}
