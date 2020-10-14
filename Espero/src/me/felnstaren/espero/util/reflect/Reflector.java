package me.felnstaren.espero.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

/**
 * Reflector Class
 * @author Ryan
 * 
 * Adapted from BananaPuncher714's NBTEditor class
 * https://github.com/BananaPuncher714/NBTEditor
 */
public final class Reflector {

	private static final String           VERSION;
	
	public static final Map< String,   Class<?>       >   CLASS_CACHE;
	public static final Map< String,   Method         >   METHOD_CACHE;
	public static final Map< Class<?>, Constructor<?> >   CONSTRUCTOR_CACHE;
	public static final Map< Class<?>, Field          >   FIELD_CACHE;

	static {
		VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		
		CLASS_CACHE = new HashMap<String, Class<?>>();
		try {
			CLASS_CACHE.put("ChatMessageType",       Class.forName( "net.minecraft.server."    + VERSION +   ".ChatMessageType"                   ));
			CLASS_CACHE.put("IChatBaseComponent",    Class.forName( "net.minecraft.server."    + VERSION +   ".IChatBaseComponent"                ));
			CLASS_CACHE.put("ChatSerializer",        Class.forName( "net.minecraft.server."    + VERSION +   ".IChatBaseComponent$ChatSerializer" ));
			CLASS_CACHE.put("PacketPlayOutChat",     Class.forName( "net.minecraft.server."    + VERSION +   ".PacketPlayOutChat"                 ));
			CLASS_CACHE.put("EntityPlayer",          Class.forName( "net.minecraft.server."    + VERSION +   ".EntityPlayer"                      ));
			CLASS_CACHE.put("PlayerConnection",      Class.forName( "net.minecraft.server."    + VERSION +   ".PlayerConnection"                  ));
			CLASS_CACHE.put("IChatMutableComponent", Class.forName( "net.minecraft.server."    + VERSION +   ".IChatMutableComponent"             ));
			CLASS_CACHE.put("Packet",                Class.forName( "net.minecraft.server."    + VERSION +   ".Packet"                            ));
			CLASS_CACHE.put("MinecraftServer",       Class.forName( "net.minecraft.server."    + VERSION +   ".MinecraftServer"                   ));
			//CLASS_CACHE.put("PacketPlayOutPlayerListHeaderFooter", Class.forName( "net.minecraft.server" + VERSION + "PacketPlayOutPlayerListHeaderFooter" ));
			CLASS_CACHE.put("CraftPlayer",           Class.forName( "org.bukkit.craftbukkit."  + VERSION +   ".entity.CraftPlayer"                ));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		METHOD_CACHE = new HashMap<String, Method>();
		try {
			METHOD_CACHE.put("b",            getNMSClass("ChatSerializer")    .getDeclaredMethod("b", String.class));
			METHOD_CACHE.put("sendPacket",   getNMSClass("PlayerConnection")  .getDeclaredMethod("sendPacket", getNMSClass("Packet")));
			METHOD_CACHE.put("getHandle",    getNMSClass("CraftPlayer")       .getDeclaredMethod("getHandle"));
			METHOD_CACHE.put("getServer",    getNMSClass("MinecraftServer")   .getDeclaredMethod("getServer"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		CONSTRUCTOR_CACHE = new HashMap<Class<?>, Constructor<?>>();
		try {
			CONSTRUCTOR_CACHE.put(getNMSClass("PacketPlayOutChat"), getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), getNMSClass("ChatMessageType"), UUID.class));
			CONSTRUCTOR_CACHE.put(getNMSClass("PacketPlayOutPlayerListHeaderFooter"), getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		FIELD_CACHE = new HashMap<Class<?>, Field>();
		try {
			FIELD_CACHE.put(getNMSClass("EntityPlayer"), getNMSClass("EntityPlayer").getField("playerConnection"));
			FIELD_CACHE.put(getNMSClass("MinecraftServer"), getNMSClass("MinecraftServer").getField("recentTps"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	/**
	 * Get NMS class from class cache, or classes if not cached
	 * 
	 * @author BananaPuncher714
	 */
	public static Class<?> getNMSClass(String name) {
		if ( CLASS_CACHE.containsKey( name ) ) {
			return CLASS_CACHE.get( name );
		}

		try {
			return Class.forName("net.minecraft.server." + VERSION + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
