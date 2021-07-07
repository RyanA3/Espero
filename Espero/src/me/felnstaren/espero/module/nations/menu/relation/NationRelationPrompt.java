package me.felnstaren.espero.module.nations.menu.relation;

import org.bukkit.entity.Player;

import me.felnstaren.espero.messaging.Format;
import me.felnstaren.espero.module.nations.Nations;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRelation;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.ui.prompt.ChatOptionPrompt;

//Unused (for now?)
public class NationRelationPrompt extends ChatOptionPrompt {

	private Nation own;
	private Nation other;
	
	public NationRelationPrompt(Player player, Nation own, Nation other) {
		super(player, 20, Format.HEADER_VALUE.message("Relation Request", other.getDisplayName()) + Color.LIGHT_GRAY
				+ "\n" + Format.ARG.message("%option1") + Color.LIGHT_GRAY
				+ " " + Format.ARG.message("%option2") + Color.LIGHT_GRAY
				+ " " + Format.ARG.message("%option3") + Color.LIGHT_GRAY
				+ " " + Format.ARG.message("%option4"), 
				new String[]{ "Ally", "Truce", "Neutral", "Enemy" });
		this.own = own;
		this.other = other;
	}
	
	public void callback(String response) {
		//NationRelation requestrelation = NationRelation.valueOf(response.toUpperCase());
		//Nations.requestRelation(own, other, requestrelation);	
		this.expired = true;
	}

}
