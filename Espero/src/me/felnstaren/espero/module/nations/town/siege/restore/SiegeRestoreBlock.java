package me.felnstaren.espero.module.nations.town.siege.restore;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SiegeRestoreBlock {
	
	public int x, y, z;
	public int material;
	public int bdata = -1;
	
	public SiegeRestoreBlock(int x, int y, int z, int material) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.material = material;
	}
	
	public SiegeRestoreBlock(int x, int y, int z, int material, int bdata) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.material = material;
		this.bdata = bdata;
	}
	
	//Load from real block
	public SiegeRestoreBlock(Block block, SiegeRestoreData datas) {
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		this.material = block.getType().ordinal();
		if(block.getBlockData() != null) this.bdata = datas.put(block.getBlockData());
	}
	
	//Load from string (stored)
	public SiegeRestoreBlock(String data) {
		String[] values = data.split("\\.");
		x = Integer.parseInt(values[0]);
		y = Integer.parseInt(values[1]);
		z = Integer.parseInt(values[2]);
		material = Integer.parseInt(values[3]);
		if(values.length > 4) bdata = Integer.parseInt(values[4]);
	}
	
	
	
	public void restore(World world, SiegeRestoreData datas) {
		Block block = world.getBlockAt(x, y, z);
		block.setType(Material.values()[material]);
		if(bdata > -1) block.setBlockData(datas.get(bdata));
	}
	
	public String data() {
		String data = x + "\\." + y + "\\." + z + "\\." + material;
		if(this.bdata > -1) data += "\\." + this.bdata;
		return data;
	}

}
