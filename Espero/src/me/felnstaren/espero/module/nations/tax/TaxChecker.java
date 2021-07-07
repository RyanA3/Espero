package me.felnstaren.espero.module.nations.tax;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.Option;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.felib.util.time.Time;

public class TaxChecker {
	
	private Time last_tax;
	
	private BukkitRunnable tax_task = new BukkitRunnable() {
		public void run() {
			if(new Time().add(last_tax.copy().invert()).isLaterThan(new Time(0, 1, 0, 0, 0))) {
				last_tax = new Time();
				taxAll();
				Option.MAIN_CONFIG.set("last-tax", last_tax.toString());
			}
		}
	};
	
	public TaxChecker(JavaPlugin plugin) {
		if(Option.MAIN_CONFIG.getString("last-tax", null) == null) {
			last_tax = new Time();
			taxAll();
			Option.MAIN_CONFIG.set("last-tax", last_tax.toString());
		} else {
			last_tax = new Time(Option.MAIN_CONFIG.getString("last-tax"));
		}
		
		tax_task.runTaskTimer(plugin, 1000, 3600000);
	}
	
	// ((4root(area) - perimeter)^2 * very_small_deviation_penalty) + (area * chunk_cost)
	// Balanced for a median 25-50 Mynt / day, 25 Mynt / hour
	// 2-3 days/week 
	
		// 20% Deviation from optimal perimeter, 1000 claims
		// ( 152 - 4root(1000) )^2 * 1/32 + (1000 * 1/32)
		// ( 152 - 127 )^2         * 1/32 + ( 31.25 )
		// ( 25 ) ^ 2              * 1/32 + ( 31.25 )
		// 625                     * 1/32 + ( 31.25 )
		// 19.5                           + ( 31.25 )
		// 50.75
	
	public int getDailyTax(Nation nation) {
		int optimal_perimeter = (int) (4 * Math.sqrt(nation.getArea()));
		int perimeter_deviation = nation.getPerimeter() - optimal_perimeter;
		int perimeter_penalty = (int) (Math.pow(perimeter_deviation, 2) * Option.NATION_PERIMETER_DEVIATION_TAX);
		int area_penalty = (int) (nation.getArea() * Option.NATION_CLAIM_BASE_TAX);
		int town_area_penalty = (int) (nation.getTownArea() * Option.TOWN_CLAIM_BASE_TAX);
		
		return
				perimeter_penalty + area_penalty + town_area_penalty;
	}
	
	public void taxAll() {
		Espero.LOGGER.info("Taxing all nations... <daily tax>");
		ArrayList<Nation> nations = NationRegistry.inst().getNations();
		
		for(Nation nation : nations) {
			nation.addBalance(nation.getBalance() - getDailyTax(nation));
			if(nation.getBalance() < Option.MAX_NATION_DEBT) {
				nation.disband();
			}
		}
	}

}
