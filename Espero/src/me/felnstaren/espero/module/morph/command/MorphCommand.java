package me.felnstaren.espero.module.morph.command;

import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.felnstaren.espero.module.morph.MorphManager;
import me.felnstaren.espero.module.morph.morph.EntityMorph;
import me.felnstaren.espero.module.morph.morph.RideableMorph;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.chat.Messenger;
import me.felnstaren.felib.packet.enums.MetadataValue;
import me.felnstaren.felib.packet.enums.PacketEntityPose;
import me.felnstaren.felib.packet.enums.PacketEntityType;
import me.felnstaren.felib.reflect.Reflector;

public class MorphCommand implements CommandExecutor {

	private MorphManager mman;
	
	public MorphCommand(MorphManager mman) {
		this.mman = mman;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return true;
		Player player = (Player) sender;
		
		if(!player.hasPermission("espero.morph") && !player.isOp()) {
			Messenger.send(player, "#F55boog");
			return true;
		}
		
		if(args.length == 0) {
			EntityMorph morph = mman.getMorphByPlayer(player.getEntityId());
			if(morph != null) mman.unmorph(morph);
			Messenger.send(player, "#5F5You are no longer disguised");
			return true;
		}
		
		PacketEntityType entity_type = PacketEntityType.WOLF;
		try { entity_type = PacketEntityType.valueOf(args[0].toUpperCase()); }
		catch (Exception e) { Messenger.send(player, "#F55The fuck is this shit: " + args[0]); return true; }
		
		EntityMorph morph = mman.getMorphByPlayer(player.getEntityId());
		if(morph != null) {
			Messenger.send(player, "#F55You are already disguised");
			return true;
		}
		
		if(entity_type == PacketEntityType.HORSE) morph = new RideableMorph(player, entity_type);
		else morph = new EntityMorph(player, entity_type);
		
		for(int i = 1; i < args.length; i++) {
			String[] split = args[i].split(":");
			MetadataValue type = MetadataValue.valueOf(split[0].toUpperCase());
			if(type.getType() == int.class) morph.getProperties().put(type, Integer.parseInt(split[1]));
			else if(type.getType() == boolean.class) morph.getProperties().put(type, Boolean.parseBoolean(split[1]));
			else if(type.getType() == byte.class) morph.getProperties().put(type, Byte.valueOf(split[1]));
			else if(type.getType() == String.class) morph.getProperties().put(type, split[1]);
			else if(type.getType() == Optional.class) {
				if(type == MetadataValue.ENTITY_CUSTOM_NAME) morph.getProperties().put(type, Optional.of(Reflector.newInstanceOf("ChatComponentText", split[1])));
				else { Messenger.send(player, Color.RED + "unsupported option"); return true; }
			}
			else if(type.getType() == Reflector.getNMSClass("EntityPose")) 
				morph.getProperties().put(type, PacketEntityPose.valueOf(split[1]).getNMSPose());
			else morph.getProperties().put(type, type.getType().cast(split[1]));
		}
		
		mman.morph(morph);
		Messenger.send(player, "#5F5You are now a " + entity_type.name().replaceAll("HORSE", "HONSE"));
		
		return true;
	}

}
 