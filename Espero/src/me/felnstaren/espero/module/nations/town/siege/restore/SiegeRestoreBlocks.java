package me.felnstaren.espero.module.nations.town.siege.restore;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SiegeRestoreBlocks {

	private ArrayList<SiegeRestoreBlock> blocks;
	
	//Create new
	public SiegeRestoreBlocks() {
		blocks = new ArrayList<SiegeRestoreBlock>();
	}
	
	//Load from string (data)
	public SiegeRestoreBlocks(String data) {
		blocks = new ArrayList<SiegeRestoreBlock>();
		String values[] = data.split(",");
		for(String value : values)
			blocks.add(new SiegeRestoreBlock(value));
	}
	
	
	
	public void put(Block block, SiegeRestoreData datas, boolean should_remove) {
		if(should_remove) blocks.add(new SiegeRestoreBlock(block.getX(), block.getY(), block.getZ(), Material.AIR.ordinal()));
		else blocks.add(new SiegeRestoreBlock(block, datas, should_remove));
	}
	
	public void restore(World world, SiegeRestoreData datas) {
		for(SiegeRestoreBlock block : blocks)
			block.restore(world, datas);
	}
	
	public String data() {
		String data = "";
		for(SiegeRestoreBlock block : blocks) 
			data += block.data() + ",";
		if(data.length() > 0) data = data.substring(0, data.length() - 1);
		return data;
	}
	
}
