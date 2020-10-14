package me.felnstaren.espero.config;

public class ChatVar {

	private String key;
	private String value;
	
	public ChatVar(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	
	
	public String fill(String message) {
		return message.replace(key, value);
	}
	
	
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
}
