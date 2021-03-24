package me.felnstaren.espero.test;

import me.felnstaren.felib.util.data.SearchObject;

public class IntSearchObject implements SearchObject {

	private int key;
	
	public IntSearchObject(int key) {
		this.key = key;
	}
	
	@Override
	public int searchValue() {
		return key;
	}

}
