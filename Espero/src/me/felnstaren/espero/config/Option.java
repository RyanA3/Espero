package me.felnstaren.espero.config;

import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.Espero;

public class Option {
	
	public static YamlConfiguration MAIN_CONFIG;
	
	public static void init() {
		MAIN_CONFIG = Espero.LOADER.readConfig("data.yml", null);
	}
	
	public static int NATION_FOUND_COST           = 500;
	public static int TOWN_FOUND_COST             = 100;
	public static int TOWN_CLAIM_PURCHASE_COST    = 5;
	public static int TOWN_CLAIM_SELL_COST        = 3;
	public static int NATION_CLAIM_PURCHASE_COST  = 5;
	public static int NATION_CLAIM_SELL_COST      = 3;
	public static int MIN_COFFERS_BALANCE         = 500;
	public static int NATION_STARTING_BALANCE     = 50;
	public static int SIEGE_START_COST			  = 100;
	public static int RELIC_NOT_PLACED_PENALTY    = 100;
	
	public static double NATION_CLAIM_BASE_TAX    = 1/32;
	public static double TOWN_CLAIM_BASE_TAX      = 1/4;
	public static double TOWN_PERIMETER_DEVIATION_TAX = 1/16;
	public static double NATION_PERIMETER_DEVIATION_TAX = 1/32;
	public static int MAX_NATION_DEBT = -100;
	
	public static String DEFAULT_WORLD = "world";
	
	
}
