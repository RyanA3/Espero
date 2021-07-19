package me.felnstaren.espero.module.acoustic.item;

import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Jukebox;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.item.custom.CustomMaterialData;
import me.felnstaren.felib.item.custom.function.MaterialInteractFunction;
import me.felnstaren.felib.item.recipe.CustomRecipe;
import me.felnstaren.felib.packet.enums.PacketTitleAction;
import other.bananapuncher714.nbt.NBTEditor;

public class TuningFork extends CustomMaterialData implements MaterialInteractFunction {

	public TuningFork() {
		super("tuning_fork", "&fTuning Fork", 501, Material.STICK);
		super.recipe = new CustomRecipe("tuning_fork", super.stack(), "I I", " I ", " I ").material('I', Material.IRON_INGOT);
	}

	
	
	public void execute(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		Block block = event.getClickedBlock();
		int tone = 0;
		int octave = 0;
		try { tone = NBTEditor.getInt(item, "tone"); octave = NBTEditor.getInt(item, "octave"); }
		catch (Exception e) { e.printStackTrace(); }
		if(tone > 6) tone = 6;
		else if(tone < 0) tone = 0;
		if(octave > 1) octave = 1;
		else if(octave < 0) octave = 0;
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR) {
			tone++;
			if(tone >= Tone.values().length) {
				if(octave < 1) {
					octave++;
					tone = 0;
				} else tone = Tone.values().length-1;
			}
			Messenger.sendTitle(player, Color.AQUA.toString() + octave + ":" + Tone.values()[tone].name(), PacketTitleAction.ACTIONBAR, 20, 100, 20);
			item = NBTEditor.set(item, tone, "tone");
			item = NBTEditor.set(item, octave, "octave");
			player.getInventory().setItemInMainHand(item);
		}
		else if(event.getAction() == Action.LEFT_CLICK_AIR) {
			tone--;
			if(tone < 0) {
				if(octave > 0) {
					octave--;
					tone = Tone.values().length-1;
				} else tone = 0;
			}
			Messenger.sendTitle(player, Color.AQUA.toString() + octave + ":" + Tone.values()[tone].name(), PacketTitleAction.ACTIONBAR, 20, 100, 20);
			item = NBTEditor.set(item, tone, "tone");
			item = NBTEditor.set(item, octave, "octave");
			player.getInventory().setItemInMainHand(item);
		}
		else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(block == null) return;
			if(block.getType() == Material.NOTE_BLOCK) {
				event.setCancelled(true);
				NoteBlock data = (NoteBlock) block.getBlockData();
				data.setNote(Note.natural(octave, Tone.values()[tone]));
				block.setBlockData(data);
				Messenger.sendTitle(player, Color.LIME.toString() + "Set block to " + octave + ":" + Tone.values()[tone].name(), PacketTitleAction.ACTIONBAR, 20, 100, 20);
			} else if(block.getType() == Material.JUKEBOX) {
				event.setCancelled(true);
			}
		}
		else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(block == null) return;
			if(block.getType() == Material.NOTE_BLOCK) {
				event.setCancelled(true);
				NoteBlock data = (NoteBlock) block.getBlockData();
				Note note = data.getNote();
				Messenger.sendTitle(player, Color.LIME.toString() + "Block is set to " + note.getOctave() + ":" + note.getTone().name(), PacketTitleAction.ACTIONBAR, 20, 100, 20);
			} else if(block.getType() == Material.JUKEBOX) {
				event.setCancelled(true);
				Jukebox data = (Jukebox) block.getBlockData();
				Material record = data.getMaterial();
				String record_name = "nothing";
				if(record != null) record_name = record.name();
				Messenger.sendTitle(player, Color.LIME.toString() + "Jukebox contains " + record_name, PacketTitleAction.ACTIONBAR, 20, 100, 20);
			}
		}
	}

}
