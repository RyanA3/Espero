package me.felnstaren.espero.module.nations.town.siege;

import me.felnstaren.espero.module.nations.group.Permission;

public enum SiegeStage {
	
	//3600, 4800, 6600, 7800
	
	STARTING(10),
	PREPARATION(20, Permission.STONE_BUTTON, Permission.DOOR, Permission.LEVER, Permission.BUTTON),
	ACTIVE(30, Permission.STONE_BUTTON, Permission.DOOR, Permission.LEVER, Permission.BUTTON, Permission.BUILD),
	COMPLETE(40, Permission.STONE_BUTTON, Permission.DOOR, Permission.LEVER, Permission.BUTTON);
	
	private Permission[] permissions;
	private int time;
	
	private SiegeStage(int time, Permission... permissions) {
		this.permissions = permissions;
		this.time = time;
	}
	
	public boolean isPermitted(Permission permission) {
		for(Permission p : permissions)
			if(p == permission) return true;
		return false;
	}
	
	public int getTime() {
		return time;
	}

}
