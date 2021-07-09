package me.felnstaren.espero.module.nations.group;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import me.felnstaren.felib.config.ConfigurationSectionObject;
import me.felnstaren.felib.util.ArrayUtil;

public class Rank implements ConfigurationSectionObject {

	private Permission[] permissions;
	public String display_name;
	public String gaming_name;
	public int weight = 0;
	
	public Rank(String gaming_name) { this.gaming_name = gaming_name; };
	
	public Rank(String display_name, Permission... permissions) {
		this(display_name, 0, permissions);
	}
	
	public Rank(String display_name, int weight, Permission... permissions) {
		this.weight = weight;
		this.permissions = permissions;
		this.display_name = display_name;
		this.gaming_name = display_name.toLowerCase().replaceAll(" ", "_");
	}
	
	
	
	public boolean isPermitted(Permission permission) {
		for(Permission check : permissions)
			if(check == permission) return true;
		return false;
	}
	
	public boolean outranks(Rank rank) {
		return weight > rank.weight;
	}
	
	public void addPermissions(Permission... permissions) {
		this.permissions = ArrayUtil.combine(this.permissions, permissions);
	}
	
	public void remPermissions(Permission... permissions) {
		this.permissions = ArrayUtil.remove(this.permissions, permissions);
	}
	
	public void inherits(Rank rank) {
		addPermissions(rank.permissions);
	}
	
	public Permission[] permissions() {
		return this.permissions;
	}
	
	public ArrayList<String> strpermissions() {
		ArrayList<String> strpermissions = new ArrayList<String>();
		for(int i = 0; i < permissions.length; i++) strpermissions.add(permissions[i].name());
		return strpermissions;
	}
	
	public void save(ConfigurationSection data) {
		data.set("display_name", display_name);
		data.set("weight", weight);
		data.set("permissions", strpermissions());
	}



	@Override
	public ConfigurationSectionObject load(ConfigurationSection data) {
		this.display_name = data.getString("display_name");
		this.weight = data.getInt("weight");
		
		List<String> sperms = data.getStringList("permissions");
		this.permissions = new Permission[sperms.size()];
		int i = 0;
		for(String sperm : sperms) {
			permissions[i] = Permission.valueOf(sperm);
			i++;
		}
		
		return this;
	}



	@Override
	public ConfigurationSectionObject template() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
