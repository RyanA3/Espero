package me.felnstaren.espero.module.nations.town.siege.restore;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import me.felnstaren.espero.Espero;

public class SiegeRestoreData {

	private int datamax;
	private BlockData[] bdata;
	
	//Create new
	public SiegeRestoreData() {
		datamax = 0;
		bdata = new BlockData[256];
	}
	
	//Load from string (data)
	public SiegeRestoreData(String data) {
		datamax = 0;
		bdata = new BlockData[256];
		String[] values = data.split(",");
		for(int i = 0; i < values.length; i++) {
			bdata[i] = Bukkit.createBlockData(values[i]);
			datamax = i;
		}
	}
	
	
	
	public int put(BlockData data) {
		if(datamax > this.bdata.length) {
			Espero.LOGGER.severe("SIEGE RESTORE BLOCK DATA CAP REACHED - NO MORE BLOCK DATA WILL BE SAVED");
			return -1;
		}
		
		this.bdata[datamax] = data;
		datamax++;
		return datamax-1;
	}
	
	public BlockData get(int index) {
		return bdata[index];
	}
	
	public String data() {
		String data = "";
		for(int i = 0; i < datamax; i++) 
			data += bdata[i].getAsString() + ","; 
		if(data.length() > 0) data = data.substring(0, data.length() - 1);
		return data;
	}
	
}
