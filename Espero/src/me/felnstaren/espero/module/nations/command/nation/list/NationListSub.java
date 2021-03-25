package me.felnstaren.espero.module.nations.command.nation.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.nation.Nations;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.command.CommandStub;
import me.felnstaren.felib.command.SubCommand;

public class NationListSub extends SubCommand {

	public NationListSub() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				String message =  " &a-=&8[ &a" + "Espero's Nations" + " &8]&a=- \n";
				for(String name : Nations.inst().getNationNames()) message += "   " + name;
				Messenger.send(player, "#AAA" + Messenger.color(message));
				
				return true;
			}
		}, "list");
		// TODO Auto-generated constructor stub
	}

	
	
}
