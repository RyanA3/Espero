package me.felnstaren.espero.module.nations.command.town.info;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.felib.chat.ClickComponent;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Message;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.chat.TextComponent;
import me.felnstaren.felib.command.SubArgument;
import me.felnstaren.felib.command.SubCommand;

public class TownHelpCommand extends SubCommand {

	public TownHelpCommand() {
		super("help");
		arguments.add(new SubArgument("[page]") {
			public boolean stub(CommandSender sender, String[] args, int current) {
				Player player = (Player) sender;
				
				int page = 0;
				try { page = Math.abs(Integer.parseInt(args[current])); }
				catch (Exception e) { Messenger.send(player, Format.ERROR_INVALID_ARGUMENT.message().replaceAll("%argument%", args[current])); return true; }
				
				if(page >= PAGES.length) {
					Messenger.send(player, Format.ERROR_INVALID_ARGUMENT.message().replaceAll("%argument%", args[current]));
					return true;
				}
				
				Messenger.send(player, PAGES[page]);

				return true;
			}
		});
	}

	public boolean stub(CommandSender sender, String[] args, int current) {
		Messenger.send((Player) sender, PAGES[0]);
		return true;
	}
	
	
	
	private static final String HELP_HEADER = Format.HEADER_VALUE.message("Espero", "Towns");
	
	public static final Message[] PAGES = new Message[3];
	static {
		PAGES[0] = Messenger.colorWithJson(
				HELP_HEADER + " " + Format.LABEL_ARG.message("Page", "0") + Color.LIGHT_GRAY
				+ "\n/town"
				+ "\n  help " + Format.LABEL_ARG.message("page", "0") + Color.LIGHT_GRAY
				+ "\n  found " + Format.FORCE_ARG.message("name") + Color.LIGHT_GRAY
				+ "\n  claim " + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				+ "\n  unclaim " + Color.LIGHT_GRAY
				+ "\n  info " + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				+ "\n  list"
				)
				.addComponent(new TextComponent("\n          <}-").setColor(Color.GRAY + "").setBold(true))
				.addComponent(new TextComponent("   |   ").setColor(Color.LIGHT_GRAY + ""))
				.addComponent(new TextComponent("-{>\n").setColor(Color.WHEAT + "").setBold(true).setClickComponent(new ClickComponent("/town help 1")));
				
		PAGES[1] = Messenger.colorWithJson(
				HELP_HEADER + " " + Format.LABEL_ARG.message("Page", "1") + Color.LIGHT_GRAY
				+ "\n/town"
				+ "\n  invite " + Format.FORCE_ARG.message("player") + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				+ "\n  uninvite " + Format.FORCE_ARG.message("player") + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				+ "\n  invites " + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				+ "\n  members " + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				+ "\n  join " + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				+ "\n  leave " + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				)
				.addComponent(new TextComponent("\n          <}-").setColor(Color.WHEAT + "").setBold(true).setClickComponent(new ClickComponent("/town help 0")))
				.addComponent(new TextComponent("   |   ").setColor(Color.LIGHT_GRAY + ""))
				.addComponent(new TextComponent("-{>\n").setColor(Color.WHEAT + "").setBold(true).setClickComponent(new ClickComponent("/town help 2")));
		
		PAGES[2] = Messenger.colorWithJson(
				HELP_HEADER + " " + Format.LABEL_ARG.message("Page", "2") + Color.LIGHT_GRAY
				+ "\n/town"
				+ "\n  kick " + Format.FORCE_ARG.message("player") + Format.FORCE_ARG.message("town") + Color.LIGHT_GRAY
				+ "\n  disband " + Format.FORCE_ARG.message("<town>") + Color.LIGHT_GRAY
				+ "\n  "
				+ "\n  "
				+ "\n  " 
				+ "\n  " 
				)
				.addComponent(new TextComponent("\n          <}-").setColor(Color.WHEAT + "").setBold(true).setClickComponent(new ClickComponent("/town help 1")))
				.addComponent(new TextComponent("   |   ").setColor(Color.LIGHT_GRAY + ""))
				.addComponent(new TextComponent("-{>\n").setColor(Color.GRAY + "").setBold(true));
	}

}
