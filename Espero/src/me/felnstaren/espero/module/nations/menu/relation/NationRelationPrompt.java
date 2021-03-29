package me.felnstaren.espero.module.nations.menu.relation;

import org.bukkit.entity.Player;

import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRelation;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.ui.prompt.ChatOptionPrompt;

public class NationRelationPrompt extends ChatOptionPrompt {

	private Nation own;
	private Nation other;
	
	public NationRelationPrompt(Player player, Nation own, Nation other) {
		super(player, 20, Color.LIGHT_GRAY + "Which relation would you like to request to have with " + other.getDisplayName() + "?", 
				new String[]{ "Ally", "Truce", "Neutral", "Enemy" });
		this.own = own;
		this.other = other;
	}
	
	public void callback(String response) {
		NationRelation prevrelation = other.getRelation(own.getID());
		NationRelation requestrelation = NationRelation.valueOf(response.toUpperCase());
		
		if(requestrelation == prevrelation) {
			Messenger.send(player, "You're already " + response + " with this nation!");
			return;
		}
		
		if(requestrelation.outranks(prevrelation)) {
			other.broadcast(Color.GREEN + own.getDisplayName() + " has requested to " + response + " with you!");
			own.broadcast(Color.GREEN + player.getName() + " has sent a " + response + " request to " + other.getDisplayName());
			
			//TODO: Unscuff this
			//Right now: If its a relation request, requesting nation assumes relation, requested nation keeps previous relation until accepted
			own.setRelation(other.getID(), requestrelation);
			
			this.expired = true;
			return;
		}
		
		//Less friendly relations don't require permission
		other.broadcast(Color.GREEN + "You are now " + response + " with " + own.getDisplayName());
		own.broadcast(Color.GREEN + "You are now " + response + " with " + other.getDisplayName());
		own.setRelation(other.getID(), requestrelation);
		other.setRelation(own.getID(), requestrelation);
		
		this.expired = true;
	}

}
