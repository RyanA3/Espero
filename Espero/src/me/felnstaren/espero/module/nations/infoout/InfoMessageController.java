package me.felnstaren.espero.module.nations.infoout;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.felnstaren.felib.chat.Messenger;

public class InfoMessageController {

	private static final int COOLDOWN = 10;
	
	private ArrayList<InfoMessage> messages = new ArrayList<InfoMessage>();
	private ArrayList<Integer> times = new ArrayList<Integer>();
	
	private BukkitRunnable task = new BukkitRunnable() {
		public void run() {
			for(int i = 0; i < times.size(); i++) {
				times.set(i, times.get(i) - 1);
				if(times.get(i) < 0) {
					times.remove(i); messages.remove(i);
					i--;
				}
			}
		}
	};
	
	
	
	public void send(Player player, InfoMessage message) {
		if(isOnCooldown(message)) return;
		Messenger.send(player, message.message());
		set(message, COOLDOWN);
	}
	
	
	
	public InfoMessageController(JavaPlugin plugin) {
		task.runTaskTimer(plugin, 100, 1000);
	}
	
	private void register(InfoMessage message, int time) {
		messages.add(message);
		times.add(time);
	}
	
	public void unregister(InfoMessage message) {
		int index = -1;
		for(int i = 0; i < messages.size(); i++) {
			if(messages.get(i) == message) {
				index = i;
				break;
			}
		}
		
		if(index == -1) return;
		messages.remove(index);
		times.remove(index);
	}
	
	public void set(InfoMessage message, int time) {
		int index = -1;
		for(int i = 0; i < messages.size(); i++) {
			if(messages.get(i) == message) {
				index = i;
				break;
			}
		}
		
		if(index == -1) register(message, time);
		else times.set(index, time);
	}
	
	public boolean isOnCooldown(InfoMessage message) {
		return messages.contains(message);
	}
	
	public int getCooldownTime(InfoMessage message) {
		int index = messages.indexOf(message);
		if(index == -1) return 0;
		return times.get(index);
	}
	
}
