/**
 * Buildings run by the city, requiring tax revenue
 * 
 * @author dwgreenidge
 *
 */
abstract public class MunicipalBuilding extends Building {
	
	// monthly running cost
	protected int monthlyCost;
	
	
	/**
	 * Running cost in specified month
	 * 
	 * Cost rises with inflation
	 * 
	 * @param month month of simulation
	 * @return inflation-adjusted running cost
	 */
	public double getMonthlyCost(int month)
	{
		return monthlyCost * Math.pow((1.0 + CitySimulation.MONTHLY_INFLATION_RATE), (month - getConstructionMonth()));
	}

	
	// number of blocks where in each direction over which
	// building has effect or coverage
	protected int coverage;

	public int getCoverage() {
		return coverage;
	}


	public MunicipalBuilding(int constrMonth, int cover, int basicCost, int mthlyCost) 
	{
		super(constrMonth, basicCost);
		coverage = cover;
		monthlyCost = mthlyCost;
		capacity = 0;
	}
}
