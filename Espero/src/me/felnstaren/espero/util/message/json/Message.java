package me.felnstaren.espero.util.message.json;

import java.util.ArrayList;

public class Message {

	private ArrayList<TextComponent> components;
	
	public Message() {
		components = new ArrayList<TextComponent>();
	}
	
	
	
	public Message addComponent(TextComponent component) {
		components.add(component);
		return this;
	}
	
	public Message addComponent(TextComponent component, int index) {
		components.add(index, component);
		return this;
	}
	
	public TextComponent getComponent(int index) {
		return components.get(index);
	}
	
	
	
	public String build() {
		String build = "[";
		
		for(int i = 0; i < components.size(); i++) {
			if(i > 0) build += ",";
			build += "{" + components.get(i).build() + "}";
		}
		
		build += "]";
		return build;
	}
	
}
