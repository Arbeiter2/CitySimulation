/**
 * Buildings run by the city, requiring tax revenue
 * 
 * @author dwgreenidge
 *
 */
abstract public class MunicipalBuilding extends Building {
	
	// monthly running cost
	protected int monthlyCost;
	
	
	public int getMonthlyCost() {
		return monthlyCost;
	}


	/**
	 * Running cost in specified month
	 * 
	 * Cost rises with inflation
	 * 
	 * @param month month of simulation
	 * @return inflation-adjusted running cost
	 */
	public double getAdjustedMonthlyCost(int month)
	{
		return monthlyCost * Math.pow((1.0 + CitySimulation.MONTHLY_INFLATION_RATE), (month - getConstructionMonth()));
	}

	
	// number of blocks where in each direction over which
	// building has effect or coverage
	protected final int coverage;

	public int getCoverage() {
		return coverage;
	}


	/**
	 * @param constrMonth
	 * @param basicCost
	 * @param bheight
	 * @param bWidth
	 * @param capcty
	 * @param cover
	 * @param mthlyCost
	 * @param name
	 */
	public MunicipalBuilding(int constrMonth, int basicCost, int bheight, int bWidth, int capcty, 
			int cover, int mthlyCost, String name, String abbrev) 
	{
		super(constrMonth, basicCost, bheight, bWidth, capcty, name, abbrev);
		coverage = cover;
		monthlyCost = mthlyCost;
	}
}
