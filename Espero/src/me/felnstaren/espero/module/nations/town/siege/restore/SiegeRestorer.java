package me.felnstaren.espero.module.nations.town.siege.restore;

import org.bukkit.World;
import org.bukkit.block.Block;

public class SiegeRestorer {

	private SiegeRestoreData siege_block_data;
	private SiegeRestoreBlocks siege_blocks;
	
	//Create new
	public SiegeRestorer() {
		siege_block_data = new SiegeRestoreData();
		siege_blocks = new SiegeRestoreBlocks();
	}
	
	//Load from string (data)
	public SiegeRestorer(String data) {
		String values[] = data.split("\n");
		if(values.length > 0) {
			siege_blocks = new SiegeRestoreBlocks(values[0]);
			siege_block_data = new SiegeRestoreData(values[1]);
		} else {
			siege_blocks = new SiegeRestoreBlocks();
			siege_block_data = new SiegeRestoreData();
		}
	}
	
	
	
	
	public void put(Block block, boolean should_remove) {
		siege_blocks.put(block, siege_block_data, should_remove);
	}
	
	public void restore(World world) {
		siege_blocks.restore(world, siege_block_data);
	}
	
	
	
	public String data() {
		return siege_blocks.data() + "\n" + siege_block_data.data();
	}
	
}
