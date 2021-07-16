package me.felnstaren.espero.module.acoustic.item;

import org.bukkit.Instrument;
import org.bukkit.Material;

import me.felnstaren.felib.item.recipe.CustomRecipe;

public class SnareDrum extends AbstractInstrument {

	public SnareDrum() {
		super("snare_drum", "&fSnare Drum", 515, Material.STICK, Instrument.SNARE_DRUM);
		super.recipe = new CustomRecipe("guitar", super.stack(), " s ", "sSs", " s ").material('S', Material.SAND).material('s', Material.STICK);
	}
	
}
