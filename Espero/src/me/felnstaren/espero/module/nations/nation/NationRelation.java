package me.felnstaren.espero.module.nations.nation;

import me.felnstaren.felib.chat.Color;

public enum NationRelation {

	ALLY(Color.BLUE.toString(), 6),
	TRUCE(Color.AQUA.toString(), 4),
	NEUTRAL(Color.LIGHT_GRAY.toString(), 2),
	ENEMY(Color.RED.toString(), 0);
	
	
	
	private String color;
	private int weight;
	
	private NationRelation(String color, int weight) {
		this.color = color;
		this.weight = weight;
	}
	
	public String getDisplayColor() {
		return color;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public boolean outranks(NationRelation other) {
		return weight > other.getWeight();
	}
	
}
