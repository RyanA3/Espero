package me.felnstaren.espero.module.clogger;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.fusesource.jansi.Ansi.Color;

import me.felnstaren.felib.chat.Messenger;

public class CombatTimeHandler {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Integer> times = new ArrayList<Integer>();
	
	private BukkitRunnable task = new BukkitRunnable() {
		public void run() {
			for(int i = 0; i < times.size(); i++) {
				times.set(i, times.get(i) - 1);
				if(times.get(i) < 0) {
					if(players.get(i).isOnline()) Messenger.send(players.get(i), Color.GREEN + "You are no longer in combat");
					times.remove(i); players.remove(i);
					i--;
				}
			}
		}
	};
	
	
	
	public CombatTimeHandler(JavaPlugin plugin) {
		task.runTaskTimer(plugin, 100, 1000);
	}
	
	public void register(Player player, int time) {
		players.add(player);
		times.add(time);
	}
	
	public void unregister(Player player) {
		int index = -1;
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getUniqueId().equals(player.getUniqueId())) {
				index = i;
				break;
			}
		}
		
		if(index == -1) return;
		players.remove(index);
		times.remove(index);
	}
	
	public void set(Player player, int time) {
		int index = -1;
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getUniqueId().equals(player.getUniqueId())) {
				index = i;
				break;
			}
		}
		
		if(index == -1) return;
		times.set(index, time);
	}
	
	public boolean isInCombat(Player player) {
		return players.contains(player);
	}
	
	public int getCombatTime(Player player) {
		int index = players.indexOf(player);
		if(index == -1) return 0;
		return times.get(index);
	}

}
