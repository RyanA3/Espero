package me.felnstaren.espero.module.wind;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.felnstaren.felib.packet.enums.PacketParticleType;
import me.felnstaren.felib.packet.particle.PacketParticle;

public class WindTaskHandler {
	
	private float rotational_speed = 0;
	private float wind_acceleration = 0;
	private int last_change = 0;
	
	private BukkitRunnable task = new BukkitRunnable() {
		private final Random RANDOM = new Random();
		
		public void run() {
			last_change++;
			if(last_change > 500) {
				last_change = 0;
				rotational_speed = RANDOM.nextFloat() / 150;
				wind_acceleration = (RANDOM.nextFloat() - 0.4f) / 100;
			}
			if(GlobalWind.PRIMARY.speed > 0.75 && GlobalWind.PRIMARY.speed < 1.5) GlobalWind.PRIMARY.accelerate(wind_acceleration);
			GlobalWind.PRIMARY.rotate(rotational_speed);
			
			PacketParticle particle = new PacketParticle(PacketParticleType.SPIT, 1, GlobalWind.PRIMARY.x * GlobalWind.PRIMARY.speed, 0f, GlobalWind.PRIMARY.y * GlobalWind.PRIMARY.speed);
			
			for(Player player : Bukkit.getOnlinePlayers()) {
				float px = (float) player.getLocation().getX();
				float py = (float) player.getLocation().getY();
				float pz = (float) player.getLocation().getZ();
				
				for(float i = 2; i < 5.5f; i+=0.5f) {
					particle.build(px + RANDOM.nextInt(50) - 25, py + RANDOM.nextInt(20) - 10, pz + RANDOM.nextInt(50) - 25);
					particle.send(player);
				}
			}
			
			for(Projectile a : Bukkit.getWorlds().get(0).getEntitiesByClass(Projectile.class)) {
				Vector av = a.getVelocity();
				av.setX((av.getX() * 0.95) + (0.01f * GlobalWind.PRIMARY.x * GlobalWind.PRIMARY.speed));
				av.setZ((av.getZ() * 0.95) + (0.01f * GlobalWind.PRIMARY.y * GlobalWind.PRIMARY.speed));
				a.setVelocity(av);
			}
		}
	};
	
	
	public WindTaskHandler(JavaPlugin plugin) {
		task.runTaskTimerAsynchronously(plugin, 100, 1);
	}

}