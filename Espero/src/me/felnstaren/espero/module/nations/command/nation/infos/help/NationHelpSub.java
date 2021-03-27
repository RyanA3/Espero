package me.felnstaren.espero.module.nations.command.nation.infos.help;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.chat.ClickComponent;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Message;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.chat.TextComponent;
import me.felnstaren.felib.command.SubCommand;

public class NationHelpSub extends SubCommand {

	public NationHelpSub() {
		super("help");
		arguments.add(new NationHelpArg());
	}


	public boolean stub(CommandSender sender, String[] args, int current) {
		Player player = (Player) sender;
		Messenger.send(player, PAGES[0]);
		return true;
	}
	
	
	
	private static final String HELP_HEADER = Format.HEADER_VALUE.message("Espero", "Nations");
	
	public static final Message[] PAGES = new Message[3];
	static {
		PAGES[0] = Messenger.colorWithJson(
				HELP_HEADER + " " + Format.LABEL_ARG.message("Page", "0") + Color.LIGHT_GRAY
				+ "\n/nation"
				+ "\n  help " + Format.LABEL_ARG.message("page", "0") + Color.LIGHT_GRAY
				+ "\n  info " + Format.LABEL_ARG.message("nation", "self") + Color.LIGHT_GRAY
				+ "\n  towns " + Format.LABEL_ARG.message("nation", "self") + Color.LIGHT_GRAY
				+ "\n  members " + Format.LABEL_ARG.message("nation", "self") + Color.LIGHT_GRAY
				+ "\n  invites " + Format.LABEL_ARG.message("nation", "self") + Color.LIGHT_GRAY
				+ "\n  list"
				+ "\n  map"
				)
				.addComponent(new TextComponent("\n          <}-").setColor(Color.GRAY + "").setBold(true))
				.addComponent(new TextComponent("   |   ").setColor(Color.LIGHT_GRAY + ""))
				.addComponent(new TextComponent("-{>\n").setColor(Color.WHEAT + "").setBold(true).setClickComponent(new ClickComponent("/nation help 1")));
		PAGES[1] = Messenger.colorWithJson(
				HELP_HEADER + " " + Format.LABEL_ARG.message("Page", "1") + Color.LIGHT_GRAY
				+ "\n/nation"
				+ "\n  create " + Format.FORCE_ARG.message("name") + Color.LIGHT_GRAY
				+ "\n  disband"
				+ "\n  invite " + Format.FORCE_ARG.message("player") + Color.LIGHT_GRAY
				+ "\n  uninvite " + Format.FORCE_ARG.message("player") + Color.LIGHT_GRAY
				+ "\n  promote " + Format.FORCE_ARG.message("player") + Color.LIGHT_GRAY
				+ "\n  demote " + Format.FORCE_ARG.message("player") + Color.LIGHT_GRAY
				+ "\n  kick " + Format.FORCE_ARG.message("player")
				)
				.addComponent(new TextComponent("\n          <}-").setColor(Color.WHEAT + "").setBold(true).setClickComponent(new ClickComponent("/nation help 0")))
				.addComponent(new TextComponent("   |   ").setColor(Color.LIGHT_GRAY + ""))
				.addComponent(new TextComponent("-{>\n").setColor(Color.WHEAT + "").setBold(true).setClickComponent(new ClickComponent("/nation help 2")));
		PAGES[2] = Messenger.colorWithJson(
				HELP_HEADER + " " + Format.LABEL_ARG.message("Page", "2") + Color.LIGHT_GRAY
				+ "\n/nation"
				+ "\n  join " + Format.FORCE_ARG.message("nation") + Color.LIGHT_GRAY
				+ "\n  leave"
			    + "\n  claim " + Format.FORCE_ARG.message("type") + Color.LIGHT_GRAY
			    + "\n  unclaim"
				+ "\n  chat"
				+ "\n  leader " + Format.FORCE_ARG.message("player")
				)
				.addComponent(new TextComponent("\n          <}-").setColor(Color.WHEAT + "").setBold(true).setClickComponent(new ClickComponent("/nation help 1")))
				.addComponent(new TextComponent("   |   ").setColor(Color.LIGHT_GRAY + ""))
				.addComponent(new TextComponent("-{>\n").setColor(Color.GRAY + "").setBold(true));
		
	}

}
