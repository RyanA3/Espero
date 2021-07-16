package me.felnstaren.espero.module.acoustic.item;

import org.bukkit.Instrument;
import org.bukkit.Material;

import me.felnstaren.felib.item.recipe.CustomRecipe;

public class Guitar extends AbstractInstrument {

	public Guitar() {
		super("guitar", "&fGuitar", 511, Material.STICK, Instrument.GUITAR);
		super.recipe = new CustomRecipe("guitar", super.stack(), "S ", "Ss", "Ss").material('S', Material.STICK).material('s', Material.STRING);
	}

}
