package me.felnstaren.espero.module.nations.nation;

import java.util.ArrayList;

public class TownRegistry {
	
	private static TownRegistry INSTANCE;
	
	public static void init(NationRegistry nations) {
		INSTANCE = new TownRegistry(nations);
	}
	
	public static TownRegistry inst() {
		return INSTANCE;
	}
	
	
	
	private ArrayList<Town> towns;
	private ArrayList<String> towns_names;
	
	public TownRegistry(NationRegistry nations) {
		towns = new ArrayList<Town>();
		towns_names = new ArrayList<String>();
		
		ArrayList<Nation> nation_list = nations.getNations();
		for(Nation nation : nation_list) {
			towns.addAll(nation.getTowns());
			towns_names.addAll(nation.getTownsNames());
		}
	}
	

}
