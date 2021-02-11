package me.felnstaren.espero.module.nations.nation;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.felib.config.ConfigurationSectionObject;
import me.felnstaren.felib.util.ArrayUtil;

public class NationPlayerRank implements ConfigurationSectionObject {

	private String label;
	private String display_name;
	private String[] permissions;
	private String inheretance_name;
	private NationPlayerRank inheretance;
	private int weight;
	private ConfigurationSection data;
	
	public NationPlayerRank(ConfigurationSection data) {
		load(data);
	}
	
	
	
	public void setInheretance(NationPlayerRank inheretance) {
		this.inheretance = inheretance;
	}
	
	public void setPermissions(String... permissions) {
		this.permissions = permissions;
	}
	
	public void setDisplayName(String label) {
		this.display_name = label;
	}
	
	
	
	public int getWeight() {
		return weight;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getDisplayName() {
		return display_name;
	}
	
	public String getInheretanceName() {
		return inheretance_name;
	}
	
	
	public ConfigurationSection getData() {
		data.set("display_name", display_name);
		data.set("permissions", permissions);
		data.set("weight", weight);
		if(inheretance_name != null) data.set("inheretance_name", inheretance_name);
		return data;
	}
	
	public void save(YamlConfiguration config) {
		config.set(data.getCurrentPath(), data);
	}
	
	
	
	public boolean isPermitted(String permission) {
		for(String check : permissions)
			if(check.equals(permission)) return true;
		return inheretance != null && inheretance.isPermitted(permission);
	}



	public ConfigurationSectionObject load(ConfigurationSection data) {
		if(data == null) return this;
		this.label = data.getName();
		this.display_name = data.getString("display_name");
		this.permissions = ArrayUtil.stringver(data.getStringList("permissions").toArray());
		this.weight = data.getInt("weight", 0);
		if(data.getString("inherits") != null) this.inheretance_name = data.getString("inherits");
		this.data = data;
		return this;
	}
	
	public ConfigurationSectionObject template() {
		return new NationPlayerRank(null);
	}
	
}
