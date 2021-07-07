package me.felnstaren.espero.module.nations.town;

import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.nations.group.Group;
import me.felnstaren.espero.module.nations.group.GroupRegistry;
import me.felnstaren.espero.module.nations.group.Permission;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.util.data.SearchObject;

public class Town implements SearchObject {

	private UUID group;
	private UUID nation;
	private UUID uuid; //Replace id with uuid in claim system
	
	private int x;
	private int z;
	public String name;
	private int area;
	
	private YamlConfiguration config;
	private String path;
	
	public Town(UUID uuid) {
		this.uuid = uuid;
		this.path = "towndata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_town.yml");
		
		this.name = config.getString("display_name");
		this.x = config.getInt("cx");
		this.z = config.getInt("cz");
		this.area = config.getInt("area");
		this.nation = UUID.fromString(config.getString("nation"));
	}
	
	public Town(UUID nation, String name, int x, int z) {
		this.name = name;
		this.x = x;
		this.z = z;
		this.area = 0;
		this.nation = nation;
		
		this.uuid = UUID.randomUUID();
		this.path = "towndata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_town.yml");
	}
	
	
	
	public UUID    getID()             { return uuid; 		 }
	public int     getArea()           { return area;	     }
	public void    setArea(int value)  { this.area = value;   }
	public void    addArea(int value)  { this.area += value;  }
	public Nation  getNation()         { return NationRegistry.inst().getNation(nation); }
	public Group   getGroup()          { return GroupRegistry.inst().getGroup(group);    }
	public boolean hasPermission(EsperoPlayer player, Permission permission)			 {
		return getGroup().hasPermission(player, permission);
	}
	
	public void save() {
		config.set("cx", x);
		config.set("cz", z);
		config.set("display_name", name);
		config.set("area", area);
		config.set("nation", nation.toString());
	}


	
	public int searchValue() {
		return SearchObject.getIndexValue(uuid);
	}

}
