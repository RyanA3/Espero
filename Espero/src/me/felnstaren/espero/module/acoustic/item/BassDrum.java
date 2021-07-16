package me.felnstaren.espero.module.acoustic.item;

import org.bukkit.Instrument;
import org.bukkit.Material;

import me.felnstaren.felib.item.recipe.CustomRecipe;

public class BassDrum extends AbstractInstrument {

	public BassDrum() {
		super("bass_drum", "&fBass Drum", 514, Material.STICK, Instrument.BASS_DRUM);
		super.recipe = new CustomRecipe("bass_drum", super.stack(), " s ", "sLs", " s ").material('s', Material.STICK).material('L', Material.LEATHER);
	}
	
}
