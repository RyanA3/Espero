package me.felnstaren.espero.config;

public class Option {
	
	// ((4root(area) - perimeter)^2 * very_small_deviation_penalty) + (area * chunk_cost)
	// Balanced for a median 25-50 Mynt / day, 25 Mynt / hour
	// 2-3 days/week 
	
	public static int NATION_FOUND_COST    = 500;
	public static int TOWN_FOUND_COST      = 100;
	public static int CLAIM_PURCHASE_COST  = 5;
	public static int CLAIM_SELL_COST      = 3;
	public static int MIN_COFFERS_BALANCE  = 500;
	
	public static double NATION_CLAIM_BASE_TAX   = 1/32;
	public static double TOWN_CLAIM_BASE_TAX     = 1/4;
	public static double NATION_PERIMETER_DEVIATION_TAX = 1/32;
	
	// 20% Deviation from optimal perimeter, 1000 claims
	// ( 152 - 4root(1000) )^2 * 1/32 + (1000 * 1/32)
	// ( 152 - 127 )^2         * 1/32 + ( 31.25 )
	// ( 25 ) ^ 2              * 1/32 + ( 31.25 )
	// 625                     * 1/32 + ( 31.25 )
	// 19.5                           + ( 31.25 )
	// 50.75
	
}
