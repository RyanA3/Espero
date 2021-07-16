package me.felnstaren.espero.module.acoustic.item;

import org.bukkit.Instrument;
import org.bukkit.Material;

import me.felnstaren.felib.item.recipe.CustomRecipe;

public class Flute extends AbstractInstrument {

	public Flute() {
		super("flute", "&fFlute", 516, Material.STICK, Instrument.FLUTE);
		super.recipe = new CustomRecipe("flute", super.stack(), "sss").material('s', Material.STICK);
	}
	
}
