package me.felnstaren.espero.module.acoustic.item;

import org.bukkit.Instrument;
import org.bukkit.Material;

import me.felnstaren.felib.item.recipe.CustomRecipe;

public class Bellkit extends AbstractInstrument {

	public Bellkit() {
		super("bellkit", "&fBellkit", 513, Material.STICK, Instrument.BELL);
		super.recipe = new CustomRecipe("bellkit", super.stack(), "BS").material('S', Material.STICK).material('B', Material.BELL);
	}
	
}
