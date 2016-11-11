/**
 * Buildings run by the city, requiring tax revenue
 * 
 * @author dwgreenidge
 *
 */
abstract public class MunicipalBuilding extends Building {
	
	// monthly running cost
	private int monthlyCost; 
	public int getMonthlyCost()
	{
		return monthlyCost;
	}

	
	// number of blocks where in each direction over which
	// building has effect or coverage
	private int coverage;

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
