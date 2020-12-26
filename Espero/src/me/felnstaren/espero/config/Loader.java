package me.felnstaren.espero.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import me.felnstaren.espero.Espero;
import me.felnstaren.rilib.logger.Level;

public class Loader {
	
	public static final Plugin PLUGIN = Espero.getPlugin(Espero.class);
	
	public static YamlConfiguration readConfig(String name, String defalt) {
		File file = new File(PLUGIN.getDataFolder(), name);
		if(file.exists()) return YamlConfiguration.loadConfiguration(file);

		if(!create(file)) return null;
		if(defalt != null) copy(file, defalt);
		
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public static String readData(String name, String defalt) {
		File file = new File(PLUGIN.getDataFolder(), name);
		if(!file.exists()) {
			if(!create(file)) return null;
			copy(file, defalt);
		}
		
		try {
			InputStream initial_stream = new FileInputStream(file);
			byte[] buffer = new byte[initial_stream.available()];
			initial_stream.read(buffer);
			initial_stream.close();
			return new String(buffer, StandardCharsets.US_ASCII);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static File load(String name) {
		File file = new File(PLUGIN.getDataFolder(), name);
		if(!file.exists()) create(file);
		return file;
	}
		
	private static File copy(File copy, String original) {
		if(original == null) return copy;

		try {
			InputStream initial_stream = Loader.class.getResourceAsStream("resources/" + original);
			byte[] buffer = new byte[initial_stream.available()];
			initial_stream.read(buffer);
			initial_stream.close();
				
			FileOutputStream out_stream = new FileOutputStream(copy);
			out_stream.write(buffer);
			out_stream.close();
				
			Espero.LOGGER.log(Level.DEBUG, "Copied file; " + copy.getPath() + " from plugin resource file; " + original);
		} catch (Exception e) { 
			e.printStackTrace(); 
			Espero.LOGGER.log(Level.SEVERE, "An error occured while copying to this file; " + copy.getPath());
			return null;
		}
		
		return copy;
	}
	
	
	
	public static boolean create(File file) {
		try { 
			file.createNewFile(); 
			Espero.LOGGER.log(Level.DEBUG, "Created file; " + file.getPath());
			return true;
		} 
		catch (IOException e) { 
			e.printStackTrace(); 
			Espero.LOGGER.log(Level.SEVERE, "A fatal error occured while creating this file; " + file.getPath());
			return false;
		}
	}
	
	public static boolean delete(String path) {
		try {
			File file = new File(PLUGIN.getDataFolder(), path);
			file.delete();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Espero.LOGGER.log(Level.SEVERE, "A fatal error occured while deleting this file; " + path);
			return false;
		}
	}
	
	
	
	public static void save(String data, String name) {
		save(data, new File(PLUGIN.getDataFolder(), name));
	}
	
	public static void save(YamlConfiguration config, String name) {
		save(config, new File(PLUGIN.getDataFolder(), name));
	}
	
	public static void save(String data, File file) {
		try {
			FileOutputStream out_stream = new FileOutputStream(file);
			out_stream.write(data.getBytes());
			out_stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void save(YamlConfiguration config, File file) {
		try { 
			config.save(file); 
			Espero.LOGGER.log(Level.DEBUG, "Saved file; " + file.getPath());
		} 
		catch (IOException e) { 
			e.printStackTrace(); 
			Espero.LOGGER.log(Level.WARNING, "An error occured saving this file; " + file.getPath());
		}
	}
	
	
	
	public static void mkDirs() {
		Espero.LOGGER.log(Level.DEBUG, "Marking non-existant directories");
		
		File data_folder = PLUGIN.getDataFolder();
		File player_folder = new File(data_folder.getPath().concat("/playerdata/"));
		File chunk_folder = new File(data_folder.getPath().concat("/chunkdata/"));
		File nation_folder = new File(data_folder.getPath().concat("/nationdata/"));
		if (!data_folder.exists()) data_folder.mkdirs(); 
		if (!player_folder.exists()) player_folder.mkdirs();
		if(!chunk_folder.exists()) chunk_folder.mkdirs();
		if(!nation_folder.exists()) nation_folder.mkdirs();
	}
	
}
