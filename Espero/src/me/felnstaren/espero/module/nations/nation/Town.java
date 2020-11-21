package me.felnstaren.espero.module.nations.nation;

import org.bukkit.configuration.ConfigurationSection;

import me.felnstaren.espero.util.math.Vec2d;

public class Town {

	private Vec2d center = new Vec2d(0, 0);
	private int id;
	private String name;
	
	public Town(ConfigurationSection data) {
		//this.name = data.getName();
		//this.id = data.getInt("id");
		this.id = Integer.parseInt(data.getName());
		this.name = data.getString("display_name");
		this.center.x = data.getInt("cx");
		this.center.y = data.getInt("cy");
	}
	
}
