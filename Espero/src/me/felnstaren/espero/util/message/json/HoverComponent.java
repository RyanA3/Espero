package me.felnstaren.espero.util.message.json;

public class HoverComponent implements IComponent {

	protected TextComponent hover_value;
	
	public HoverComponent(TextComponent hover_value) {
		this.hover_value = hover_value;
	}
	
	public String build() {
		return "\"action\":\"show_text\",\"contents\":{" + hover_value.buildNonceComponents() + "}";
	}

}
