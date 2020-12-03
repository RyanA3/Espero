package me.felnstaren.espero.util.message;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.felnstaren.espero.util.ArrayUtil;
import me.felnstaren.espero.util.message.json.Message;
import me.felnstaren.espero.util.message.json.TextComponent;
import me.felnstaren.espero.util.reflect.Packeteer;
import me.felnstaren.espero.util.reflect.Reflector;

/**
 * Messenger Class
 * @author Ryan
 * Last Updated 10-5-2020
 *
 */
public class Messenger {

	private static final String HEX_CHARS = "0123456789ABCDEF";
	private static final String DEFAULT_COLOR_CHARS = "0123456789abcdefklmno";
	
	/**
	 * Replaces & symbols followed by a char indicating a color
	 * code with the symbol Minecraft uses to flag different colors
	 * in messages. This method doesn't return a JSON formatted message,
	 * and shouldn't be sent by you as a packet.
	 * @param message String to be colored
	 * @return A colored string <3
	 */
	public static String color(String message) {
		char[] msg = message.toCharArray();
		
		for(int i = 0; i < msg.length; i++) {
			if(msg[i] != '&') continue;
			if(msg.length <= i + 1) break;
			if(DEFAULT_COLOR_CHARS.contains(Character.toString(msg[i + 1])))
				msg[i] = '§';
		}
		return new String(msg);
	}
	
	
	/**
	 * This method is used to convert a basic string into a JSON formatted
	 * string, which can be sent as a packet to clients. Useful for sending
	 * messages to players which contain #RGB and #RRGGBB codes to indicate
	 * the following characters' color.
	 * @param message String to be converted to JSON formatting
	 * @return A {@link LegacyMessage} object, use {@link LegacyMessage#build()} to build the raw JSON string
	 */
	public static Message colorWithJson(String message) {
		message = color(message);

		int[] possible_color_positions = ArrayUtil.getIndices(message, "#");
		String[] possible_colors = new String[possible_color_positions.length];
		
		//Find colors in message
		for(int i = 0; i < possible_color_positions.length; i++) {
			String color = "";
			for(int j = 1; j < 7; j++) {
				if(message.length() <= possible_color_positions[i] + j) break;
				String char_at = Character.toString(message.charAt(possible_color_positions[i] + j));
				if(HEX_CHARS.contains(char_at)) {
					color += char_at;
					continue;
				} else break;
			}
			
			if(color.length() < 3) continue;
			if(color.length() < 6) color = color.substring(0, 3);
			else color = color.substring(0, 6);
			possible_colors[i] = color;
		}
		
		//Remove invalid color positions
		int color_count = 0;
		for(int i = 0; i < possible_colors.length; i++) if(possible_colors[i] != null) color_count++;
		
		int[] color_positions;
		String[] colors;
		if(color_count == possible_colors.length) {
			color_positions = possible_color_positions;
			colors = possible_colors;
		} else {
			color_positions = new int[color_count];
			for(int i = 0, j = 0; i < possible_colors.length; i++) {
				if(possible_colors[i] == null) continue;
				color_positions[j++] = possible_color_positions[i];
			}
		
			colors = new String[color_count];
			for(int i = 0, j = 0; i < possible_colors.length; i++) {
				if(possible_colors[i] == null) continue;
				colors[j++] = possible_colors[i];
			}
		}
		
		//Split message into components to feed to the message builder
		Message jsonmsg = new Message();
		for(int i = 0; i < colors.length; i++) {
			String component = "";
			
			if(i == colors.length - 1)
				component = message.substring(color_positions[i] + colors[i].length() + 1, message.length());
			else 
				component = message.substring(color_positions[i] + colors[i].length() + 1, color_positions[i + 1]);
			
			//Expand color if length is 3, after clipping out the string color from the message
			if(colors[i].length() == 3) {
				String expanded = "";
				for(int k = 0; k < 3; k++) expanded += Character.toString(colors[i].charAt(k)) + Character.toString(colors[i].charAt(k));
				colors[i] = expanded;
			}
			
			jsonmsg.addComponent(new TextComponent(component).setColor("#" + colors[i]));
		}
		
		return jsonmsg;
	}
	
	
	/**
	 * Sends a JSON formatted string as a chat packet to a player
	 * @param player Player to send chat packet to
	 * @param message JSON Formatted message to be sent as a packet
	 * @return 
	 *  IF 0 success
	 *  IF 1 format failure 
	 *  IF 2 unknown error
	 */
	public static int sendChatPacket(Player player, String message) {
		try {
			//Create the packet
			Object component = Reflector.METHOD_CACHE.get("b").invoke(null , message);
			Object type[] = Reflector.getNMSClass("ChatMessageType").getEnumConstants();
			Object packet = Reflector.CONSTRUCTOR_CACHE.get(Reflector.getNMSClass("PacketPlayOutChat")).newInstance(component, type[0], player.getUniqueId());
			
			//Send the packet
			return Packeteer.sendPacket(player, packet);
		} catch(InvocationTargetException json_exception) {
			//json_exception.printStackTrace();
			//Logger.log(Level.WARNING, "Error sending JSON message, is your plugin up to date?");
			return 1;
		} catch(Exception unknown_exception) {
			unknown_exception.printStackTrace();
			return 2;
		}
	}
	
	/**
	 * Sends a string to a player as a chat packet (Automatically Formatted into JSON)
	 * @param player Player to send the chat message to
	 * @param message Message to be sent to the player
	 * @return 
	 *  IF 0 success
	 *  IF 1 format failure 
	 *  IF 2 unknown error
	 */
	public static int send(Player player, String message) {
		return sendChatPacket(player, colorWithJson(message).build());
	}
	
	public static int send(Player player, Message message) {
		return sendChatPacket(player, message.build());
	}
	
	public static void broadcast(String message) {
		for(Player p : Bukkit.getOnlinePlayers())
			send(p, message);
	}
	
	public static void broadcast(Message message) {
		for(Player p : Bukkit.getOnlinePlayers())
			send(p, message);
	}
	
	/**
	 * Sends a player list header / footer update packet to a player
	 * @param player Player to send the player list update to
	 * @param header JSON formatted header to be sent to the player
	 * @param footer JSON formatted footer to be sent to the player
	 * @return 
	 *  IF 0 success
	 *  IF 1 format failure 
	 *  IF 2 unknown error
	 */
	public static int sendHeaderFooterPacket(Player player, String header, String footer) {
		try {
			//Create the packet
			Object header_component = Reflector.METHOD_CACHE.get("b").invoke(null, header);
			Object footer_component = Reflector.METHOD_CACHE.get("b").invoke(null, footer);
			Object packet = Reflector.CONSTRUCTOR_CACHE.get(Reflector.getNMSClass("PacketPlayOutPlayerListHeaderFooter")).newInstance();
			Reflector.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getField("header").set(packet, header_component);
			Reflector.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getField("footer").set(packet, footer_component);
			
			//Send the packet
			return Packeteer.sendPacket(player, packet);
		} catch(InvocationTargetException json_exception) {
			//json_exception.printStackTrace();
			//Logger.log(Level.WARNING, "Error sending JSON message, is your plugin up to date?");
			return 1;
		} catch(Exception unknown_exception) {
			unknown_exception.printStackTrace();
			return 2;
		}
	}
	
	/**
	 * Sends a player list header / footer update packet to a player
	 * @param player Player to send the player list update to
	 * @param header String to be formatted and sent as the header
	 * @param footer String to be formatted and sent as the footer
	 * @return 
	 *  IF 0 success
	 *  IF 1 format failure 
	 *  IF 2 unknown error
	 */
	public static int sendHeaderFooter(Player player, String header, String footer) {
		return sendHeaderFooterPacket(player, colorWithJson(header).build(), colorWithJson(footer).build());
	}
	
	public static int sendHeaderFooter(Player player, Message header, Message footer) {
		return sendHeaderFooterPacket(player, header.build(), footer.build());
	}
	
}
