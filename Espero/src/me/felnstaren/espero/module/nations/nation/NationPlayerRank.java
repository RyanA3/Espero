package me.felnstaren.espero.module.nations.nation;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.util.logger.Level;
import me.felnstaren.espero.util.logger.Logger;

public class NationPlayerRank {

	private String label;
	private String display_name;
	private String[] permissions;
	private String inheretance_name;
	private NationPlayerRank inheretance;
	private ConfigurationSection data;
	
	public NationPlayerRank(ConfigurationSection data) {
		this.label = data.getName();
		this.display_name = data.getString("display_name");
		this.permissions = (String[]) data.getStringList("permissions").toArray();
		
		if(data.getString("inherits") != null) this.inheretance_name = data.getString("inherits");
		
		this.data = data;
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
	
	
	
	public String getName() {
		return label;
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
		if(inheretance_name != null) data.set("inheretance_name", inheretance_name);
		return data;
	}
	
	public void save(YamlConfiguration config, File file) {
		config.set(data.getCurrentPath(), data);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.log(Level.WARNING, "Oh fuc, Oh shit");
		}
	}
	
	
	
	public boolean isPermitted(String permission) {
		for(String check : permissions)
			if(check.equals(permission)) return true;
		return inheretance != null && inheretance.isPermitted(permission);
	}
	
}
