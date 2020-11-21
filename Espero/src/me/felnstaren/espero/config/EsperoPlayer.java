package me.felnstaren.espero.config;

import java.io.File;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.module.nations.system.Nations;
import me.felnstaren.espero.util.logger.Level;
import me.felnstaren.espero.util.logger.Logger;

public class EsperoPlayer {

	private YamlConfiguration data;
	private String path;
	private UUID uuid;
	
	public EsperoPlayer(UUID uuid) {
		init(uuid);
	}
	
	public EsperoPlayer(Player player) {
		init(player.getUniqueId());
	}
	
	private void init(UUID uuid) {
		this.uuid = uuid;
		Logger.log(Level.DEBUG, "Loading player with name " + uuid);
		this.path = "playerdata/" + uuid + ".yml";
		this.data = Loader.readConfig(path, "default_player.yml");
		save();
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
	
	
	
	public Nation getNation() {
		String nation_id = data.getString("nation");
		if(nation_id == null) return null;
		Nation nation = Nations.getInstance().getNation(UUID.fromString(nation_id));
		if(nation.isMember(uuid)) return nation;
		data.set("nation", "");
		return null;
	}
	
	public NationPlayerRank getNationRank() {
		Nation nation = getNation();
		if(nation == null) return null;
		String nation_rank = data.getString("nation-rank", "recruit");
		return nation.getRank(nation_rank);
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
		return new File(Loader.PLUGIN.getDataFolder(), "playerdata/" + uuid + ".yml").exists();
	}
	
}
