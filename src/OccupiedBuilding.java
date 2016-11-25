/**
 * A building in which people live or work, and a source of
 * tax revenue.
 * 
 * @author dwgreenidge
 *
 */
abstract public class OccupiedBuilding extends Building {
	
	// all occupied buildings have a tax revenue type
	CitySimulation.TaxSource taxClass;
	
	public CitySimulation.TaxSource getTaxClass() 
	{
		return taxClass;
	}

	public OccupiedBuilding(int constrMonth, int basicCost, int bheight, int bWidth, int capcty, 
			CitySimulation.TaxSource tax_class, String name, String abbrev) 
	{
		super(constrMonth, basicCost, bheight, bWidth, capcty, name, abbrev);
		numberOfOccupants = 0;
		taxClass = tax_class;
	}
	
	// number of people using this building
	protected int numberOfOccupants;
	
	
	/**
	 * Add new occupants to building (can be negative)
	 * 
	 * @param numOccupants desired change to occupancy
	 * @return new numberOfOccupants
	 */
	public int addOccupants(int numOccupants) 
	{
		if (numberOfOccupants + numOccupants >= 0 && numberOfOccupants + numOccupants <= capacity)
			numberOfOccupants += numOccupants;
		return numberOfOccupants;
	}
	
	public int getNumberOfOccupants()
	{
		return numberOfOccupants;
	}
	

	/**
	 * Get real estate valuation  of taxable building
	 * 
	 * Uses standard inflation rate.
	 * 
	 * @param month month of simulation
	 * @return building value
	 */
	public double getValue(int month)
	{
		return getConstructionCost() * Math.pow((1.0 + CitySimulation.MONTHLY_INFLATION_RATE), (month - getConstructionMonth()));

	}
	
	/**
	 * Calculate tax revenue using inflation adjusted value of building for specified month
	 *  
	 * @param rate tax rate
	 * @param month simulation month
	 * @return
	 */
	public double getTaxRevenue(double rate, int month)
	{
		return rate * (capacity - numberOfOccupants)/capacity * getValue(month);
	}
	

}
