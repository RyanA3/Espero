package me.felnstaren.espero.util.message.json;

public class TextComponent implements IComponent {

	protected String text = "";
	protected String color;
	protected String font;
	protected boolean strikethrough;
	protected boolean underlined;
	protected boolean obfuscated;
	protected boolean italic;
	protected boolean bold;
	protected HoverComponent hover_component;
	protected ClickComponent click_component;
	
	public TextComponent(String text) {
		this.text = text;
	}
	
	
	
	public TextComponent setColor(String color) {
		this.color = color;
		return this;
	}
	
	public TextComponent setStrikethrough(boolean strikethrough) {
		this.strikethrough = strikethrough;
		return this;
	}
	
	public TextComponent setUnderlined(boolean underlined) {
		this.underlined = underlined;
		return this;
	}
	
	public TextComponent setObfuscated(boolean obfuscated) {
		this.obfuscated = obfuscated;
		return this;
	}
	
	public TextComponent setItalic(boolean italic) {
		this.italic = italic;
		return this;
	}
	
	public TextComponent setBold(boolean bold) {
		this.bold = bold;
		return this;
	}
	
	
	public TextComponent setHoverComponent(HoverComponent hover_component) {
		this.hover_component = hover_component;
		return this;
	}
	
	public TextComponent setClickComponent(ClickComponent click_component) {
		this.click_component = click_component;
		return this;
	}
	
	
	
	
	
	public String build() {
		String build = buildNonceComponents();
		
		if(hover_component != null)
			build += ",\"hoverEvent\":{" + hover_component.build() + "}";
		if(click_component != null)
			build += ",\"clickEvent\":{" + click_component.build() + "}";
		
		return build;
	}
	
	public String buildNonceComponents() {
		String build = "\"text\":\"" + text + "\"";
		if(color != null) build += ",\"color\":\"" + color + "\"";
		if(strikethrough) build += ",\"strikethrough\":\"" + strikethrough + "\"";
		if(underlined) build += ",\"underlined\":\"" + underlined + "\"";
		if(obfuscated) build += ",\"obfuscated\":\"" + obfuscated + "\"";
		if(italic) build += ",\"italic\":\"" + italic + "\"";
		if(bold) build += ",\"bold\":\"" + bold + "\"";
		if(font != null) build += ",\"font\":\"" + font + "\"";
		
		return build;
	}
	
	
	
	public String getText() {
		return text;
	}
	
}
