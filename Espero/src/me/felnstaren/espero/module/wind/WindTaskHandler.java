package me.felnstaren.espero.module.wind;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.felnstaren.felib.packet.enums.PacketParticleType;
import me.felnstaren.felib.packet.particle.PacketParticle;

public class WindTaskHandler {
	
	private BukkitRunnable task = new BukkitRunnable() {
		private final Random RANDOM = new Random();
		
		public void run() {
			PacketParticle particle = new PacketParticle(PacketParticleType.SPIT, 1, GlobalWind.PRIMARY.x, 0f, GlobalWind.PRIMARY.z);
			
			for(Player player : Bukkit.getOnlinePlayers()) {
				for(int i = 0; i < 10; i++) {
					particle.build(player.getLocation().getX() + RANDOM.nextInt(50) - 25, player.getLocation().getY() + RANDOM.nextInt(20) - 10, player.getLocation().getZ() + RANDOM.nextInt(50) - 25);
					particle.send(player);
				}
			}
		}
	};
	
	
	public WindTaskHandler(JavaPlugin plugin) {
		task.runTaskTimerAsynchronously(plugin, 100, 5);
	}

}