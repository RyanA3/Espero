package me.felnstaren.espero.module.nations.command.nation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.command.CommandStub;
import me.felnstaren.espero.command.MasterCommand;
import me.felnstaren.espero.module.nations.command.nation.create.NationCreateSub;
import me.felnstaren.espero.module.nations.command.nation.join.NationJoinSub;
import me.felnstaren.espero.module.nations.command.nation.leave.NationLeaveSub;
import me.felnstaren.espero.util.message.Messenger;
import me.felnstaren.espero.util.message.json.Message;
import me.felnstaren.espero.util.message.json.TextComponent;

public class NationCommandMaster extends MasterCommand {

	public NationCommandMaster() {
		super(new CommandStub() {
			public boolean handle(CommandSender sender, String[] args, int current) {
				Message message = new Message();
				message.addComponent(new TextComponent("-= Nations =-").setColor("#0077FF"));
				message.addComponent(new TextComponent("   by Felns").setColor("#CCCCCC").setItalic(true));
				message.addComponent(new TextComponent("\n /nation create <name>").setColor("#AAAAAA"));
				message.addComponent(new TextComponent("\n /nation join <name>").setColor("#AAAAAA"));
				message.addComponent(new TextComponent("\n /nation leave").setColor("#AAAAAA"));
				message.addComponent(new TextComponent("\n /nation claim <id>").setColor("#AAAAAA"));
				
				Messenger.send((Player) sender, message);
				return true;
			}
		}, "nation", "espero.nation");

		commands.add(new NationCreateSub());
		commands.add(new NationLeaveSub());
		commands.add(new NationJoinSub());
	}
	
}
