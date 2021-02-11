package me.felnstaren.espero.module.nations.nation;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.felib.config.ConfigurationSectionObject;

public class Town implements ConfigurationSectionObject {

	private int x;
	private int z;
	private int id;
	public String name;
	
	public Town(ConfigurationSection data) {
		load(data);
	}
	
	public Town(String name, int id, int x, int z) {
		this.name = name;
		this.id = id;
		this.x = x;
		this.z = z;
	}
	
	
	
	public int getID() {
		return id;
	}
	
	public void save(YamlConfiguration config) {
		config.set("towns." + id + ".cx", x);
		config.set("towns." + id + ".cz", z);
		config.set("towns." + id + ".display_name", name);
	}

	
	
	public ConfigurationSectionObject load(ConfigurationSection data) {
		if(data == null) return this;
		this.id = Integer.parseInt(data.getName());
		this.name = data.getString("display_name");
		this.x = data.getInt("cx");
		this.z = data.getInt("cz");
		return this;
	}
	
	public ConfigurationSectionObject template() {
		return new Town(null);
	}

}
