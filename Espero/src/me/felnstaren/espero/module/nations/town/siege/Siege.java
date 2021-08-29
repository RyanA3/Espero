package me.felnstaren.espero.module.nations.town.siege;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.module.nations.town.Town;
import me.felnstaren.espero.module.nations.town.TownRegistry;
import me.felnstaren.espero.module.nations.town.siege.restore.SiegeRestorer;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.config.Loader;
import me.felnstaren.felib.util.data.SearchObject;
import me.felnstaren.felib.util.time.Time;

public class Siege implements SearchObject {
	
	private UUID uuid;
	private Town attacker;
	private Town defender;
	private SiegeStage stage = SiegeStage.STARTING;
	private boolean dead = false;
	
	private SiegeScore attacker_score = new SiegeScore(false);
	private SiegeScore defender_score = new SiegeScore(true);
	private SiegeRestorer restorer;
	
	private int timer;
	private Time start_time;
	
	private YamlConfiguration config;
	private String path;
	
	public Siege(Town attacker, Town defender) {
		this.uuid = UUID.randomUUID();
		this.path = "siegedata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_siege.yml");
		
		this.attacker = attacker;
		this.defender = defender;
		this.restorer = new SiegeRestorer();
		defender.setSiege(uuid);
		start_time = new Time();
		timer = 0;
	}
	
	public Siege(UUID uuid) {
		this.uuid = uuid;
		this.path = "siegedata/" + uuid + ".yml";
		this.config = Espero.LOADER.readConfig(path, "resources/default_siege.yml");
		
		this.attacker = TownRegistry.inst().getTown(UUID.fromString(config.getString("attacker")));
		this.defender = TownRegistry.inst().getTown(UUID.fromString(config.getString("defender")));
		this.stage = SiegeStage.valueOf(config.getString("stage"));
		this.dead = config.getBoolean("dead");
		this.timer = config.getInt("timer");
		this.start_time = new Time(config.getString("start_time"));
		this.restorer = new SiegeRestorer(config.getString("restore_data"));
	}
	
	public int searchValue() {
		return SearchObject.getIndexValue(uuid);
	}
	
	
	
	public Town getAttacker() { return attacker; }
	public Town getDefender() { return defender; }
	public int getRuntime() { return timer; }
	public SiegeScore getDefenderScore() { return defender_score; }
	public SiegeScore getAttackerScore() { return attacker_score; }
	public boolean isAttackerHoldingRelic() { return attacker_score.relic_held; }
	public void setAttackerHoldingRelic(boolean value) {
		this.attacker_score.relic_held = value;
		this.defender_score.relic_held = !value;
	}
	public boolean isDefenderHoldingRelic() { return defender_score.relic_held; }
	public void setDefenderHoldingRelic(boolean value) {
		this.defender_score.relic_held = value;
		this.attacker_score.relic_held = !value;
	}
	
	public void captureRelic() {
		if(stage != SiegeStage.ACTIVE) return;
		attacker_score.winner = true;
		defender_score.winner = false;
		advance();
		attacker.broadcast(Color.AQUA + "Your town successfully captured " + Color.BLUE + defender.getDisplayName() + Color.AQUA + "'s relic!");
		defender.broadcast(Color.BLUE + attacker.getDisplayName() + Color.AQUA + " captured your relic!");
	}
	
	public SiegeStage getStage() { return stage; }	
	public void advance() {
		if(dead) return;
		if(stage == SiegeStage.COMPLETE) { dispose(); return; }
		stage = SiegeStage.values()[stage.ordinal()+1];
		if(stage == SiegeStage.COMPLETE) restore();
	}
	public void tick() { 
		this.timer++; 
		if(timer > stage.getTime()) advance();
	}
	public void restore() {
		if(!defender.getRelic().exists()) defender.getRelic().spawn();
		restorer.restore(Bukkit.getWorld("world"));
	}
	public SiegeRestorer getRestorer() {
		return restorer;
	}
	public UUID getID() { return uuid; }
	public boolean isDead() { return dead; }
	public void dispose() {
		defender.setSiege(null);
		dead = true;
		Espero.LOADER.delete(path);
	}
	
	
	
	public void save() {
		config.set("attacker", attacker.getID().toString());
		config.set("defender", defender.getID().toString());
		config.set("stage", stage.name());
		config.set("dead", dead);
		config.set("timer", timer);
		config.set("start_time", start_time.toString());
		config.set("restore_data", restorer.data());
		
		Espero.LOADER.save(path, config);
	}

}
