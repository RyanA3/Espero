package me.felnstaren.espero.module.morph.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.morph.MorphManager;
import me.felnstaren.espero.module.morph.morph.EntityMorph;
import me.felnstaren.espero.module.morph.morph.RideableMorph;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.packet.enums.PacketEntityType;

public class MorphCommand implements CommandExecutor {

	private MorphManager mman;
	
	public MorphCommand(MorphManager mman) {
		this.mman = mman;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return true;
		Player player = (Player) sender;
		
		if(args.length == 0) {
			EntityMorph morph = mman.getMorphByPlayer(player.getEntityId());
			if(morph != null) mman.unmorph(morph);
			Messenger.send(player, "#5F5You are no longer disguised");
			return true;
		}
		
		PacketEntityType entity_type = PacketEntityType.WOLF;
		try { entity_type = PacketEntityType.valueOf(args[0]); }
		catch (Exception e) { Messenger.send(player, "#F55The fuck is this shit: " + args[0]); return true; }
		
		EntityMorph morph = mman.getMorphByPlayer(player.getEntityId());
		if(morph != null) {
			Messenger.send(player, "#F55You are already disguised");
			return true;
		}
		
		if(entity_type == PacketEntityType.HORSE) mman.morph(new RideableMorph(player, entity_type));
		else mman.morph(new EntityMorph(player, entity_type));
		Messenger.send(player, "#5F5You are now a " + entity_type.name().replaceAll("HORSE", "HONSE"));
		
		return true;
	}

}
 