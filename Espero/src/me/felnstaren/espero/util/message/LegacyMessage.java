package me.felnstaren.espero.util.message;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class LegacyMessage {

	private ArrayList<String> components;
	
	public LegacyMessage() { this.components = new ArrayList<String>(); };
	public LegacyMessage(ArrayList<String> components) { this.components = components; };
	

	public LegacyMessage add(String add) {
		if(add.equals("")) return this;
		components.add("{\"text\":\"" + add + "\"}");
		return this;
	}
	
	public LegacyMessage add(String add, ChatColor color) {
		if(add.equals("")) return this;
		components.add("{\"text\":\" + \"" + add + "\",\"color\":\"" + color + "\"");
		return this;
	}
	
	public LegacyMessage add(String add, String hex_color) {
		if(add.equals("")) return this;
		hex_color = hex_color.replace("#", "");
		components.add("{\"text\":\"" + add + "\",\"color\":\"#" + hex_color + "\"}");
		return this;
	}
	
	public LegacyMessage insert(String add, String hex_color, int index) {
		if(add.equals("")) return this;
		hex_color = hex_color.replace("#", "");
		components.add(index, "{\"text\":\"" + add + "\",\"color\":\"#" + hex_color + "\"}");
		return this;
	}
	
	public LegacyMessage append(LegacyMessage message) {
		return copy().addAll(message.getComponents());
	}
	
	public LegacyMessage addAll(List<String> components) {
		this.components.addAll(components);
		return this;
	}

	@SuppressWarnings("unchecked")
	public LegacyMessage copy() {
		return new LegacyMessage((ArrayList<String>) components.clone());
	}
	
	public String build() {
		String message = "[";
		for(int i = 0; i < components.size(); i++) {
			if(i == components.size() - 1) message += components.get(i) + "]";
			else message += components.get(i) + ",";
		}
			
		return message;
	}
	
	public ArrayList<String> getComponents() {
		return components;
	}
	
}
