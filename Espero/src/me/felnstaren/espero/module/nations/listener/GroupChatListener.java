package me.felnstaren.espero.module.nations.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.felib.chat.Color;

public class GroupChatListener implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		EsperoPlayer player = Espero.PLAYERS.getPlayer(event.getPlayer());
		if(player.getActiveGroupChat() == null) return;
		Group group = GroupRegistry.inst().getGroup(player.getActiveGroupChat());
		if(group == null) return;
		
		event.setCancelled(true);

		String message = Color.LIGHT_GRAY + "[" + Color.TURQUOISE + group.getDisplayName() + Color.LIGHT_GRAY + "] " + Color.AQUA + event.getPlayer().getDisplayName() + "   " + Color.LIGHT_GRAY + event.getMessage();
		group.broadcast(message);
	}
	
}
