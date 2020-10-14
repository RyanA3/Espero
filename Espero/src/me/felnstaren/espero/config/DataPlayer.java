package me.felnstaren.espero.config;

import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.felnstaren.espero.util.logger.Level;
import me.felnstaren.espero.util.logger.Logger;

public class DataPlayer {

	private YamlConfiguration data;
	private String path;
	private UUID uuid;
	
	public DataPlayer(UUID uuid) {
		init(uuid);
	}
	
	public DataPlayer(Player player) {
		init(player.getUniqueId());
	}
	
	private void init(UUID uuid) {
		this.uuid = uuid;
		Logger.log(Level.DEBUG, "Loading player with name " + uuid);
		this.path = "playerdata/" + uuid + ".yml";
		load(Loader.loadOrDefault(path, "default_player.yml"));

		
		
		save();
	}
	
	private void load(YamlConfiguration data) {
		this.data = data;
	}
	
	public void save() {
		Loader.save(data, path);
	}
	
	public void set(String key, Object value) {
		data.set(key, value);
	}
	
	
	
	public YamlConfiguration getData() {
		return data;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public void addRift() {
		int rifts = data.getInt("rift-count");
		rifts++;
		updateRifts(rifts);
	}
	
	public void delRift() {
		int rifts = data.getInt("rift-count");
		rifts--;
		updateRifts(rifts);
	}
	
	private void updateRifts(int rifts) {
		int max_sanity = 100 - (20 * rifts);
		
		data.set("rift-count", rifts);
		data.set("sanity.max-sanity", max_sanity);
		
		int sanity = data.getInt("sanity.cur-sanity");
		if(sanity > max_sanity) sanity = max_sanity;
		data.set("sanity.cur-sanity", sanity);
		
		save();
	}
	
	
	
	public static boolean hasGenerated(UUID uuid) {
		return Loader.fileExists("playerdata/" + uuid + ".yml");
	}
	
}
