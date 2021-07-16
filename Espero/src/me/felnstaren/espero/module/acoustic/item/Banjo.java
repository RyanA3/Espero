package me.felnstaren.espero.module.acoustic.item;

import org.bukkit.Instrument;
import org.bukkit.Material;

import me.felnstaren.felib.item.recipe.CustomRecipe;

public class Banjo extends AbstractInstrument {

	public Banjo() {
		super("banjo", "&fBanjo", 512, Material.STICK, Instrument.BANJO);
		super.recipe = new CustomRecipe("banjo", super.stack(), "S ", "Ss", "Ls").material('S', Material.STICK).material('L', Material.LEATHER).material('s', Material.STRING);
	}
	
}
