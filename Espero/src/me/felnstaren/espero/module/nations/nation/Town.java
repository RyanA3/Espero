package me.felnstaren.espero.module.nations.nation;

import org.bukkit.configuration.ConfigurationSection;

import me.felnstaren.espero.util.math.Vec2d;

public class Town {

	private Vec2d center = new Vec2d(0, 0);
	private int id;
	public String name;
	
	public Town(ConfigurationSection data) {
		this.id = Integer.parseInt(data.getName());
		this.name = data.getString("display_name");
		this.center.x = data.getInt("cx");
		this.center.y = data.getInt("cy");
	}
	
	
	
	public int getID() {
		return id;
	}
	
	public String data() {
		String data = id + ": \n"
				+ "name: " + name + " \n"
				+ "cx: " + center.x + " \n"
				+ "cy: " + center.y + " \n";
		return data;
	}

}
