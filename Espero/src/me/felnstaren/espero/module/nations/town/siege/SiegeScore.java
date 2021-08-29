package me.felnstaren.espero.module.nations.town.siege;

public class SiegeScore {

	public int kills = 0;
	public int deaths = 0;
	public boolean relic_held = false;
	public boolean winner = false;
	
	public SiegeScore() {};
	public SiegeScore(boolean relic_held) { this.relic_held = relic_held; }
	
}
