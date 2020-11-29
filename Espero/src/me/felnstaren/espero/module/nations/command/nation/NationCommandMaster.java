package me.felnstaren.espero.module.nations.command.nation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.MasterCommand;
import me.felnstaren.espero.module.nations.command.nation.create.NationCreateSub;
import me.felnstaren.espero.util.message.Messenger;
import me.felnstaren.espero.util.message.json.Message;
import me.felnstaren.espero.util.message.json.TextComponent;

public class NationCommandMaster extends MasterCommand {

	public NationCommandMaster() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Message message = new Message();
				message.addComponent(new TextComponent("-= Nations =-").setColor("#07F"));
				message.addComponent(new TextComponent("   by Felns").setColor("#777").setItalic(true));
				message.addComponent(new TextComponent("\n /nation create <name>").setColor("#999"));
				message.addComponent(new TextComponent("\n /nation join <name>").setColor("#999"));
				message.addComponent(new TextComponent("\n /nation leave").setColor("#999"));
				message.addComponent(new TextComponent("\n /nation claim <id>").setColor("#999"));
				
				Messenger.send((Player) sender, message);
				return true;
			}
		}, "nation", "espero.nation");

		commands.add(new NationCreateSub());
	}
	
}
