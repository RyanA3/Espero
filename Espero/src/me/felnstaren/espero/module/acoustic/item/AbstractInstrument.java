package me.felnstaren.espero.module.acoustic.item;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.item.custom.CustomMaterialData;
import me.felnstaren.felib.packet.enums.PacketTitleAction;
import other.bananapuncher714.nbt.NBTEditor;

public abstract class AbstractInstrument extends CustomMaterialData {
	
	private Instrument instrument;

	public AbstractInstrument(String label, String name, int texture_data, Material material, Instrument instrument) {
		super(label, name, texture_data, material);
		this.instrument = instrument;
	}
	
	public Instrument getInstrument() {
		return instrument;
	}

	public void execute(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInOffHand();
		
		int slot = event.getNewSlot();
		int octave = NBTEditor.getInt(item, "octave");
		int prev_tone = NBTEditor.getInt(item, "prevtone");

		Tone tone;
		Note note;
		if(slot == 7) {
			tone = Tone.values()[prev_tone];
			note = Note.natural(octave, tone);
		} else if(slot == 8) {
			String message = "";
			if(octave == 1) { octave = 0; message += "--"; }
			else { octave = 1; message += "++"; }
			item = NBTEditor.set(item, octave, "octave");
			player.getInventory().setItemInOffHand(item);
			message = Color.LIME.toString() + "octave" + message;
			Messenger.sendTitle(player, message, PacketTitleAction.ACTIONBAR, 10, 50, 10);
			return;
		} else {
			tone = Tone.values()[slot];
			note = Note.natural(octave, tone);
			item = NBTEditor.set(item, slot, "prevtone");
			player.getInventory().setItemInOffHand(item);
		}
		
		Messenger.sendTitle(player, Color.AQUA.toString() + tone.name() + ":" + octave, PacketTitleAction.ACTIONBAR, 10, 50, 10);
		player.playNote(player.getEyeLocation(), instrument, note);
		
	}
	
}
